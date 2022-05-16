using System;
using Backend.Models;

namespace Backend.Dtos.Role
{
	public class PermissionDto
	{
		public Guid RoleId { get; set; }
		public Guid PathPointId { get; set; }

		public bool Read { get; set; }
		public bool Write { get; set; }
		public bool Delete { get; set; }
		public bool ModifyRoot { get; set; }

		public PermissionDto(Guid roleId, Guid pathPointId, bool read, bool write, bool delete, bool modifyRoot)
		{
			RoleId = roleId;
			PathPointId = pathPointId;
			Read = read;
			Write = write;
			Delete = delete;
			ModifyRoot = modifyRoot;
		}

		public PermissionDto(Permission permission)
		{
			RoleId = permission.RoleId;
			PathPointId = permission.PathPointId;
			Read = permission.Read;
			Write = permission.Write;
			Delete = permission.Delete;
			ModifyRoot = permission.ModifyRoot;
		}
	}
}