using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Dtos.Role;

namespace Backend.Services
{
	public interface IRoleService
	{
		public Task<List<RoleDto>> GetRoles();

		public Task<bool> DeleteRole(Guid id);

		public Task<bool> UpdateRole(RoleDto roleDto);

		public Task<RoleDto> CreateRole(RoleCreateDto roleCreateDto);
	}
}