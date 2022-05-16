using System;

namespace Backend.Dtos.PathPoint
{
	public class PathPointDto
	{
		public Guid Id { get; set; }
		public string Path { get; set; }
		public bool IsRoot { get; set; }

		public PathPointDto(Guid id, string path, bool isRoot)
		{
			Id = id;
			Path = path;
			IsRoot = isRoot;
		}
	}
}