using System;
using System.Threading.Tasks;
using Backend.Dtos.Role;
using Backend.Models;
using Backend.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	[Authorize(Roles = AspRole.Administrator)]
	public class RoleController : ControllerBase
	{
		private readonly IRoleService _service;

		public RoleController(IRoleService service)
		{
			_service = service;
		}

		[HttpGet]
		public async Task<IActionResult> GetRoles()
		{
			var results = await _service.GetRoles();

			return Ok(results);
		}

		[HttpDelete("{roleId:guid}")]
		public async Task<IActionResult> DeleteRole(Guid roleId)
		{
			var deleted = await _service.DeleteRole(roleId);

			if (deleted)
				return Ok();

			return NotFound();
		}

		[HttpPut]
		public async Task<IActionResult> UpdateRole(RoleDto roleDto)
		{
			var updated = await _service.UpdateRole(roleDto);

			// TODO: Decide if I want to check for identical paths in update

			if (updated)
				return Ok();

			return NotFound();
		}

		[HttpPost]
		public async Task<IActionResult> CreateRole(RoleCreateDto roleCreateDto)
		{
			var created = await _service.CreateRole(roleCreateDto);

			if (created != null)
				return Ok(created);

			return Conflict("Item already exists");
		}
	}
}