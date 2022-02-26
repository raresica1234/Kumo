using System.Threading.Tasks;
using Backend.Dtos.Authentication;
using Backend.Services;
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
			await _userService.RegisterAsync(registerUserDto);

			return Ok();
		}
	}
}