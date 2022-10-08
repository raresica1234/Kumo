using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Dtos.Explorer;

namespace Backend.Services
{
	public interface IExploreService
	{
		public Task<List<ExploreResultDto>> Explore(string path);

		public Task<List<ExploreResultDto>> GetRootPathPoints();
	}
}