using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Dtos.Explorer;
using Backend.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	[Authorize]
	public class ExploreController : ControllerBase
	{
		private readonly IExploreService _exploreService;

		public ExploreController(IExploreService exploreService)
		{
			_exploreService = exploreService;
		}

		[HttpGet]
		public async Task<IActionResult> ExplorePath(string? path = null)
		{
			List<ExploreResultDto> result;
			if (path == null)
			{
				result = await _exploreService.GetRootPathPoints();
			}
			else
			{
				result = await _exploreService.Explore(path);
			}

			return Ok(result);
		}

	}
}