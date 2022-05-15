using Backend.Models;

namespace Backend.Dtos.Explorer
{
	public class ExploreResultDto
	{
		public string Name { get; set; }
		public FileSystemEntryType FileSystemEntryType { get; set; }
		
		public bool CanWrite { get; set; }
		public bool CanDelete { get; set; }

		public ExploreResultDto(string name, FileSystemEntryType type, bool canWrite, bool canDelete)
		{
			Name = name;
			FileSystemEntryType = type;
			CanWrite = canWrite;
			CanDelete = canDelete;
		}
	}
}