using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Dtos.Authentication;
using Backend.Dtos.UserRole;

namespace Backend.Services
{
	public interface IUserService
	{
		public Task RegisterAsync(RegisterUserDto registerUserDto);

		public Task<string?> LoginUser(LoginUserDto loginUserDto);
		
		public Task<bool> IsAdministrator();

		public Task<List<UserRoleDto>> GetUserRoles();
		public Task<bool> DeleteUserRole(string userId, Guid roleId);
		public Task<UserRoleDto> CreateUserRole(UserRoleDto userRoleDto);
	}
}