using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Dtos.PathPoint;

namespace Backend.Services
{
	public interface IPathPointService
	{
		public Task<List<PathPointDto>> GetPathPoints();

		public Task<bool> DeletePathPoint(Guid id);

		public Task<bool> UpdatePathPoint(PathPointDto pathPointDto);

		public Task<PathPointDto> CreatePathPoint(PathPointCreateDto pathPointCreateDto);
	}
}