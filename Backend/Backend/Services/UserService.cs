using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Backend.Dtos.Authentication;
using Backend.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Configuration;

namespace Backend.Services
{
	public class UserService : IUserService
	{
		private readonly UserManager<User> _userManager;
		private readonly IConfiguration _configuration;

		public UserService(UserManager<User> userManager, IConfiguration configuration)
		{
			this._userManager = userManager;
			this._configuration = configuration;
		}
		
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
	}
}