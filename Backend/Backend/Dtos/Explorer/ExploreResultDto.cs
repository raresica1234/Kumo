using Backend.Models;

namespace Backend.Dtos.Explorer
{
	public class ExploreResultDto
	{
		public string Name { get; set; }
		public string AbsolutePath { get; set; }
		public FileSystemEntryType FileSystemEntryType { get; set; }
		
		public bool CanWrite { get; set; }
		public bool CanDelete { get; set; }

		public bool CanModifyRoot { get; set; }
		
		public ExploreResultDto(string name, string absolutePath, FileSystemEntryType type, bool canWrite, bool canDelete, bool canModifyRoot)
		{
			Name = name;
			AbsolutePath = absolutePath;
			FileSystemEntryType = type;
			CanWrite = canWrite;
			CanDelete = canDelete;
			CanModifyRoot = canModifyRoot;
		}

		public ExploreResultDto(string name, string absolutePath, FileSystemEntryType fileSystemEntryType)
		{
			Name = name;
			AbsolutePath = absolutePath;
			FileSystemEntryType = fileSystemEntryType;
		}
	}
}