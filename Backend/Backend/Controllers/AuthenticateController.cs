using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Backend.Dtos.Authentication;
using Backend.Services;
using Backend.Utils;
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

		[HttpPost("register")]
		public async Task<IActionResult> RegisterUser(RegisterUserDto registerUserDto)
		{
			var errors = ModelErrorParser.GetErrors(ModelState);
			if (errors.Length != 0)
			{
				return BadRequest(errors);
			}

			try
			{
				await _userService.RegisterAsync(registerUserDto);
				return Ok();
			}
			catch (ApplicationException exception)
			{
				return BadRequest(exception.Message);
			}
		}
	}
}