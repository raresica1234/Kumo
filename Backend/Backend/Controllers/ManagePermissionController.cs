using Backend.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class ManagePermissionController : ControllerBase
	{
		[HttpGet("one")]
		[Authorize(Roles = Role.Administrator)]
		public IActionResult TestOne()
		{
			return Ok("Hello world!");
		}
		
		[HttpGet("two")]
		[Authorize]
		public IActionResult TestTwo()
		{
			return Ok("Hello world!");
		}
	}
}