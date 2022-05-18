using System;
using System.Threading.Tasks;
using Backend.Dtos.UserRole;
using Backend.Models;
using Backend.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
	[ApiController]
	[Authorize]
	[Route("api/[controller]")]
	public class UserController : ControllerBase
	{
		private readonly IUserService _service;
		
		public UserController(IUserService service)
		{
			_service = service;
		} 

		[Authorize(Roles = AspRole.Administrator)]
		[HttpGet]
		public async Task<IActionResult> GetAllUsers()
		{
			var results = await _service.GetUserRoles();
			
			return Ok(results);
		}
		
		[Authorize(Roles = AspRole.Administrator)]
		[HttpPost]
		public async Task<IActionResult> CreateUserRole(UserRoleDto userRoleDto)
		{
			var created = await _service.CreateUserRole(userRoleDto);

			if (created != null)
				return Ok(created);
			
			return Conflict("Item already exists");
		}
		
		[Authorize(Roles = AspRole.Administrator)]
		[HttpDelete("{userId}/{roleId:guid}")]
		public async Task<IActionResult> DeleteUserRole(string userId, Guid roleId)
		{
			var deleted = await _service.DeleteUserRole(userId, roleId);

			if (deleted)
				return Ok();

			return NotFound();
		}
	}
}