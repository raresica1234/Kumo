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
	public class PermissionController : ControllerBase
	{
		private readonly IPermissionService _service;

		public PermissionController(IPermissionService service)
		{
			_service = service;
		}

		[HttpGet]
		public async Task<IActionResult> GetPermissions()
		{
			var results = await _service.GetPermissions();

			return Ok(results);
		}

		[HttpDelete("{roleId:guid}/{pathPointId:guid}")]
		public async Task<IActionResult> DeletePermission(Guid roleId, Guid pathPointId)
		{
			var deleted = await _service.DeletePermission(roleId, pathPointId);

			if (deleted)
				return Ok();

			return NotFound();
		}

		[HttpPut]
		public async Task<IActionResult> UpdatePermission(PermissionDto permissionDto)
		{
			var updated = await _service.UpdatePermission(permissionDto);

			// TODO: Decide if I want to check for identical paths in update

			if (updated)
				return Ok();

			return NotFound();
		}

		[HttpPost]
		public async Task<IActionResult> CreatePermission(PermissionDto permissionDto)
		{
			var created = await _service.CreatePermission(permissionDto);

			if (created != null)
				return Ok(created);

			return Conflict("Item already exists");
		}
	}
}