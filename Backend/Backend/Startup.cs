using System;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Backend.Context;
using Backend.Models;
using Backend.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;

namespace Backend
{
	public class Startup
	{
		public IConfiguration Configuration { get; }

		public Startup(IConfiguration configuration)
		{
			Configuration = configuration;
		}

		// This method gets called by the runtime. Use this method to add services to the container.
		public void ConfigureServices(IServiceCollection services)
		{
			ConfigureAuthentication(services);

			services.AddCors(config =>
				config.AddDefaultPolicy(options => options.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader()));

			services.AddControllers().ConfigureApiBehaviorOptions(options =>
			{
				options.SuppressModelStateInvalidFilter = true;
			});
			services.AddSwaggerGen(c => { c.SwaggerDoc("v1", new OpenApiInfo {Title = "Backend", Version = "v1"}); });
			services.AddDbContext<DataContext>(options =>
			{
				options.UseSqlite(Configuration.GetConnectionString(nameof(DataContext)));
			});

			services.AddTransient<IUserService, UserService>();
			services.AddTransient<IExploreService, ExploreService>();
			services.AddTransient<IPathPointService, PathPointService>();
			services.AddTransient<IRoleService, RoleService>();
		}

		// This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
		public void Configure(IApplicationBuilder app, IWebHostEnvironment env, IServiceProvider serviceProvider)
		{
			if (env.IsDevelopment())
			{
				app.UseDeveloperExceptionPage();
				app.UseSwagger();
				app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "Kumo Backend v1"));
			}

			app.UseHttpsRedirection();

			app.UseRouting();

			app.UseCors();

			app.UseAuthentication();
			app.UseAuthorization();

			CreateRoles(serviceProvider).Wait();
			AddRootPathPoints(serviceProvider).Wait();

			app.UseEndpoints(endpoints => { endpoints.MapControllers(); });
		}

		private void ConfigureAuthentication(IServiceCollection services)
		{
			services.AddIdentity<User, IdentityRole>(options =>
				{
					options.Password.RequiredLength = 8;
					options.Password.RequireLowercase = false;
					options.Password.RequireUppercase = false;
					options.Password.RequireNonAlphanumeric = false;
					options.Password.RequireDigit = false;
				})
				.AddRoles<IdentityRole>()
				.AddEntityFrameworkStores<DataContext>()
				.AddDefaultTokenProviders();

			services.AddAuthentication(options =>
			{
				options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
				options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
				options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;
			}).AddJwtBearer(options =>
			{
				options.SaveToken = true;
				options.RequireHttpsMetadata = true;
				options.TokenValidationParameters = new TokenValidationParameters
				{
					ValidateIssuer = true,
					ValidateAudience = true,
					ValidAudience = Configuration["Jwt:ValidAudience"],
					ValidIssuer = Configuration["JWT:ValidIssuer"],
					IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Configuration["JWT:Secret"]))
				};
			});
		}

		private async Task CreateRoles(IServiceProvider serviceProvider)
		{
			var roleManager = serviceProvider.GetRequiredService<RoleManager<IdentityRole>>();
			var userManager = serviceProvider.GetRequiredService<UserManager<User>>();

			string[] roleNames = {AspRole.Administrator};
			IdentityResult result;

			foreach (var roleName in roleNames)
			{
				var roleExist = await roleManager.RoleExistsAsync(roleName);
				if (!roleExist)
				{
					result = await roleManager.CreateAsync(new IdentityRole(roleName));
				}
			}

			var powerUser = new User
			{
				Email = Configuration["Admin:Email"],
				UserName = Configuration["Admin:Email"]
			};

			var userPassword = Configuration["Admin:Password"];
			var user = await userManager.FindByEmailAsync(Configuration["Admin:Email"]);

			if (user == null)
			{
				var createPowerUser = await userManager.CreateAsync(powerUser, userPassword);

				if (createPowerUser.Succeeded)
				{
					await userManager.AddToRoleAsync(powerUser, "Administrator");
				}
			}
		}

		private async Task AddRootPathPoints(IServiceProvider serviceProvider)
		{
			var dataContext = serviceProvider.GetService<DataContext>();

			if (dataContext == null)
			{
				Console.WriteLine("Data context is null");
				return;
			}

			var pathPoints = await dataContext.PathPoints.ToListAsync();
			var rootPathPoints = Configuration.GetSection("RootPathPoints").Get<string[]>();

			foreach (var rootPathPoint in rootPathPoints)
			{
				if (!pathPoints.Exists(pathPoint => pathPoint.Path == rootPathPoint))
				{
					var pathPoint = new PathPoint
					{
						Path = rootPathPoint,
						IsRoot = true
					};

					await dataContext.PathPoints.AddAsync(pathPoint);
				}
			}

			await dataContext.SaveChangesAsync();
		}
	}
}