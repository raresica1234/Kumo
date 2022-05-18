using System;
using System.Collections;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using Backend.Context;
using Backend.Dtos.Authentication;
using Backend.Dtos.User;
using Backend.Dtos.UserRole;
using Backend.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;

namespace Backend.Services
{
	public class UserService : IUserService
	{
		private readonly DataContext _dataContext;
		private readonly UserManager<User> _userManager;
		private readonly RoleManager<IdentityRole> _roleManager;
		private readonly IConfiguration _configuration;

		private readonly IHttpContextAccessor _httpAccessor;

		public UserService(DataContext dataContext, 
			UserManager<User> userManager,
			RoleManager<IdentityRole> roleManager,
			IConfiguration configuration, 
			IHttpContextAccessor httpAccessor)
		{
			_dataContext = dataContext;
			_userManager = userManager;
			_roleManager = roleManager;
			_configuration = configuration;
			_httpAccessor = httpAccessor;
		}

		public string GetCurrentUserId() => _httpAccessor.HttpContext?.User.FindFirstValue(ClaimTypes.NameIdentifier);

		public async Task RegisterAsync(RegisterUserDto registerUserDto)
		{
			var user = new User
			{
				UserName = registerUserDto.Email,
				Email = registerUserDto.Email
			};

			IdentityResult result = await _userManager.CreateAsync(user, registerUserDto.Password);

			if (!result.Succeeded)
			{
				Console.WriteLine(result.Errors.ToString());
				IEnumerable<string> errorList = result.Errors.ToList().Select(error => error.Description);
				string formattedErrors = string.Join("\n", errorList);
				throw new ApplicationException(formattedErrors);
			}
		}

		public async Task<string?> LoginUser(LoginUserDto loginUserDto)
		{
			var user = await _userManager.FindByEmailAsync(loginUserDto.Email);
			if (user == null)
				throw new ApplicationException("Username does not exist.");

			var isPasswordCorrect = await _userManager.CheckPasswordAsync(user, loginUserDto.Password);
			if (!isPasswordCorrect)
				throw new ApplicationException("Password incorrect.");

			// TODO: with this approach only the first role is taken
			var roles = await _userManager.GetRolesAsync(user);

			var authenticationClaims = new List<Claim>
			{
				new Claim(ClaimTypes.NameIdentifier, user.Id),
				new Claim(ClaimTypes.Email, user.Email),
				new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
			};

			if (roles.Count > 0)
			{
				authenticationClaims.Add(new Claim(ClaimTypes.Role, roles[0]));
			}

			var authenticationSingingKey =
				new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]));

			var token = new JwtSecurityToken(
				issuer: _configuration["JWT:ValidIssuer"],
				audience: _configuration["JWT:ValidAudience"],
				expires: DateTime.Now.AddMonths(1),
				claims: authenticationClaims,
				signingCredentials: new SigningCredentials(
					authenticationSingingKey,
					SecurityAlgorithms.HmacSha256
				)
			);

			return new JwtSecurityTokenHandler().WriteToken(token);
		}

		public async Task<bool> IsAdministrator()
		{
			var id = GetCurrentUserId();

			var user = await _userManager.FindByIdAsync(id);

			if (user == null)
				return false;

			var roles = await _userManager.GetRolesAsync(user);

			return roles.Contains(AspRole.Administrator);
		}

		public async Task<List<UserDto>> GetUsers()
		{
			var results = new List<UserDto>();

			await _dataContext.Users.ForEachAsync(user =>
			{
				results.Add(new UserDto(user));
			});

			return results;
		}

		public async Task<bool> DeleteUserRole(string userId, Guid roleId)
		{
			var userRole = await _dataContext.KumoUserRoles.FindAsync(userId, roleId);

			if (userRole == null)
				return false;

			_dataContext.KumoUserRoles.Remove(userRole);
			
			await _dataContext.SaveChangesAsync();

			return true;
		}

		public async Task<UserRoleDto> CreateUserRole(UserRoleDto userRoleDto)
		{
			var userRole = await _dataContext.KumoUserRoles.FindAsync(userRoleDto.UserId, userRoleDto.RoleId);

			if (userRole != null)
				return null;

			userRole = new UserRole
			{
				UserId = userRoleDto.UserId,
				RoleId = userRoleDto.RoleId
			};

			await _dataContext.KumoUserRoles.AddAsync(userRole);
			await _dataContext.SaveChangesAsync();
			
			return userRoleDto;
		}

		public async Task<List<UserRoleDto>> GetUserRoles()
		{
			var results = new List<UserRoleDto>();

			await _dataContext.KumoUserRoles.ForEachAsync(userRole =>
			{
				results.Add(new UserRoleDto(userRole));
			});

			return results;
		}
	}
}