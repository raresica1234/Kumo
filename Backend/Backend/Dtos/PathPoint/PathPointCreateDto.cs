namespace Backend.Dtos.PathPoint
{
	public class PathPointCreateDto
	{
		public string Path { get; set; }
		public bool IsRoot { get; set; }

		public PathPointCreateDto(string path, bool isRoot)
		{
			Path = path;
			IsRoot = isRoot;
		}
	}
}