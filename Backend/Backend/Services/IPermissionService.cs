using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Dtos.PathPoint;
using Backend.Dtos.Role;

namespace Backend.Services
{
	public interface IPermissionService
	{
		public Task<List<PermissionDto>> GetPermissions();

		public Task<bool> DeletePermission(Guid roleId, Guid pathPointId);

		public Task<bool> UpdatePermission(PermissionDto permissionDto);

		public Task<PermissionDto> CreatePermission(PermissionDto permissionCreateDto);
	}
}