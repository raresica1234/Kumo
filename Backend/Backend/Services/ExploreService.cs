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
			var isAdministrator = await _userService.IsAdministrator();

			if (!isAdministrator)
			{
				// TODO: Actually check for permission
				return null;
			}

			List<ExploreResultDto> results = new List<ExploreResultDto>();

			var pathPoints = await _dataContext.PathPoints.Where(pathPoint => pathPoint.IsRoot).ToListAsync();

			// TODO: Actually check for permissions

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
	}
}