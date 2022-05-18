using System;
using Backend.Models;

namespace Backend.Dtos.UserRole
{
	public class UserRoleDto
	{
		public string UserId { get; set; }
		public Guid RoleId { get; set; }

		public UserRoleDto()
		{
		}

		public UserRoleDto(string userId, Guid roleId)
		{
			UserId = userId;
			RoleId = roleId;
		}

		public UserRoleDto(Models.UserRole userRole)
		{
			UserId = userRole.UserId;
			RoleId = userRole.RoleId;
		}
	}
}