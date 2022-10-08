using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Backend.Dtos.Authentication;
using Backend.Exceptions;
using Backend.Services;
using Backend.Utils;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class AuthenticateController : ControllerBase
	{
		private readonly IUserService _userService;

		public AuthenticateController(IUserService userService)
		{
			this._userService = userService;
		}

		[HttpGet("isAdministrator")]
		public async Task<IActionResult> IsAdministrator()
		{
			var result = await _userService.IsAdministrator();

			return Ok(result);
		}

		[HttpPost("register")]
		public async Task<IActionResult> RegisterUser(RegisterUserDto registerUserDto)
		{
			KumoException.ValidateModel(ModelState);

			await _userService.RegisterAsync(registerUserDto);
			return Ok();
		}

		[HttpPost("login")]
		public async Task<IActionResult> LoginUser(LoginUserDto loginUserDto)
		{
			KumoException.ValidateModel(ModelState);

			var loginResult = await _userService.LoginUser(loginUserDto);
			return Ok(new
			{
				Token = loginResult
			});
		}
	}
}