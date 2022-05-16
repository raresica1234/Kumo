using System;
using System.Threading.Tasks;
using Backend.Dtos.PathPoint;
using Backend.Models;
using Backend.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	[Authorize(Roles = AspRole.Administrator)]
	public class PathPointController : ControllerBase
	{
		private readonly IPathPointService _service;

		public PathPointController(IPathPointService service)
		{
			_service = service;
		}

		[HttpGet]
		public async Task<IActionResult> GetPathPoints()
		{
			var results = await _service.GetPathPoints();

			return Ok(results);
		}

		[HttpDelete("{pathPointId:guid}")]
		public async Task<IActionResult> DeletePathPoint(Guid pathPointId)
		{
			var deleted = await _service.DeletePathPoint(pathPointId);

			if (deleted)
				return Ok();

			return NotFound();
		}

		[HttpPost]
		public async Task<IActionResult> AddPathPoint(PathPointCreateDto pathPointDto)
		{
			var created = await _service.CreatePathPoint(pathPointDto);

			if (created != null)
				return Ok(created);
			
			return Conflict("Item already exists");
		}

		[HttpPut]
		public async Task<IActionResult> UpdatePathPoint(PathPointDto pathPointDto)
		{
			var updated = await _service.UpdatePathPoint(pathPointDto);

			// TODO: Decide if I want to check for identical paths in update
			
			if (updated)
				return Ok();

			return NotFound();
		}
	}
}