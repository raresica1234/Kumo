using System.Text;
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
		public Startup(IConfiguration configuration)
		{
			Configuration = configuration;
		}

		public IConfiguration Configuration { get; }

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
		}

		// This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
		public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
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
	}
}