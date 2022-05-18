using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Backend.Context;
using Backend.Dtos.Explorer;
using Backend.Models;

namespace Backend.Services
{
	public class ExploreService : IExploreService
	{
		private readonly DataContext _dataContext;
		private readonly IUserService _userService;

		public ExploreService(DataContext dataContext, IUserService userService)
		{
			_dataContext = dataContext;
			_userService = userService;
		}

		public async Task<List<ExploreResultDto>> Explore(string path)
		{
			var isAdministrator = await _userService.IsAdministrator();

			if (!isAdministrator)
			{
				// TODO: Actually check for permission
				return null;
			}

			List<ExploreResultDto> results = new List<ExploreResultDto>();

			string[] directories = Directory.GetDirectories(path);

			foreach (var directory in directories)
			{
				results.Add(new ExploreResultDto(
					Path.GetFileName(directory),
					Path.GetFullPath(directory),
					FileSystemEntryType.Directory,
					true,
					true
				));
			}

			string[] files = Directory.GetFiles(path);

			foreach (var file in files)
			{
				results.Add(new ExploreResultDto(
					Path.GetFileName(file),
					Path.GetFullPath(file),
					FileSystemEntryType.File,
					true,
					true
				));
			}

			return results;
		}

		public async Task<List<ExploreResultDto>> GetRootPathPoints()
		{
			var pathPoints = await GetAccessibleRootPathPoints();

			List<ExploreResultDto> results = new List<ExploreResultDto>();

			foreach (var pathPoint in pathPoints)
			{
				results.Add(new ExploreResultDto(
					pathPoint.Path,
					Path.GetFullPath(pathPoint.Path),
					FileSystemEntryType.Directory,
					true,
					true
				));
			}

			return results;
		}

		private async Task<List<PathPoint>> GetAccessibleRootPathPoints()
		{
			var isAdministrator = await _userService.IsAdministrator();

			if (!isAdministrator)
			{
				var user = await _dataContext.Users.Include(user => user.Roles)
					.ThenInclude(role => role.Permissions)
					.ThenInclude(permission => permission.PathPoint)
					.FirstOrDefaultAsync(user => user.Id == _userService.GetCurrentUserId());

				if (user == null)
					return null;


				var pathPoints = new List<PathPoint>();

				foreach (var role in user.Roles)
				{
					role.Permissions.ForEach(permission =>
					{
						if (permission.Read && permission.PathPoint.IsRoot)
							pathPoints.Add(permission.PathPoint);
					});
				}


				return pathPoints.Distinct().ToList();
			}


			return await _dataContext.PathPoints.Where(pathPoint => pathPoint.IsRoot).ToListAsync();
		}
	}
}