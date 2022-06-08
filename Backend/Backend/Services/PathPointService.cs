using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Backend.Context;
using Backend.Dtos.PathPoint;
using Backend.Exceptions;
using Backend.Models;
using Microsoft.EntityFrameworkCore;

namespace Backend.Services
{
	public class PathPointService : IPathPointService
	{
		private readonly DataContext _dataContext;

		public PathPointService(DataContext dataContext)
		{
			_dataContext = dataContext;
		}

		public async Task<List<PathPointDto>> GetPathPoints()
		{
			var results = new List<PathPointDto>();

			await _dataContext.PathPoints.ForEachAsync(pathPoint =>
			{
				results.Add(new PathPointDto(
					pathPoint.Id,
					pathPoint.Path,
					pathPoint.IsRoot
				));
			});

			return results;
		}

		public async Task<bool> DeletePathPoint(Guid id)
		{
			var pathPoint = await _dataContext.PathPoints.FindAsync(id);

			if (pathPoint == null)
				return false;

			_dataContext.PathPoints.Remove(pathPoint);

			await _dataContext.SaveChangesAsync();

			return true;
		}

		public async Task<bool> UpdatePathPoint(PathPointDto pathPointDto)
		{
			var pathPoint = await _dataContext.PathPoints.FindAsync(pathPointDto.Id);

			// TODO: Check if the path actually exists

			if (pathPoint == null)
				return false;

			pathPoint.Path = pathPointDto.Path;
			pathPoint.IsRoot = pathPointDto.IsRoot;

			_dataContext.PathPoints.Update(pathPoint);

			await _dataContext.SaveChangesAsync();

			return true;
		}

		public async Task<PathPointDto> CreatePathPoint(PathPointCreateDto pathPointCreateDto)
		{
			var pathPoint =
				await _dataContext.PathPoints.FirstOrDefaultAsync(pathPoint => pathPoint.Path == pathPointCreateDto.Path);
			
			// TODO: Check if the path actually exists
			
			if (pathPoint != null)
				return null;

			if (!Directory.Exists(pathPointCreateDto.Path))
				throw new KumoException("Invalid path");

			
			pathPoint = new PathPoint
			{
				Id = Guid.NewGuid(),
				IsRoot = pathPointCreateDto.IsRoot,
				Path = pathPointCreateDto.Path
			};

			await _dataContext.PathPoints.AddAsync(pathPoint);

			await _dataContext.SaveChangesAsync();

			return new PathPointDto(pathPoint.Id, pathPoint.Path, pathPoint.IsRoot);
		}
	}
}