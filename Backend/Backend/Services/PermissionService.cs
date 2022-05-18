using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Context;
using Backend.Dtos.Permission;
using Backend.Dtos.Role;
using Backend.Models;
using Microsoft.EntityFrameworkCore;

namespace Backend.Services
{
	public class PermissionService : IPermissionService
	{
		private readonly DataContext _dataContext;

		public PermissionService(DataContext dataContext)
		{
			_dataContext = dataContext;
		}

		public async Task<List<PermissionDto>> GetPermissions()
		{
			var results = new List<PermissionDto>();

			await _dataContext.Permissions.ForEachAsync(permission =>
			{
				results.Add(new PermissionDto(
					permission
				));
			});

			return results;
		}

		public async Task<bool> DeletePermission(Guid roleId, Guid pathPointId)
		{
			var permission = await _dataContext.Permissions.FindAsync(roleId, pathPointId);

			if (permission == null)
				return false;

			_dataContext.Permissions.Remove(permission);

			await _dataContext.SaveChangesAsync();

			return true;
		}

		public async Task<bool> UpdatePermission(PermissionDto permissionDto)
		{
			var permission = await _dataContext.Permissions.FindAsync(
				permissionDto.RoleId, permissionDto.PathPointId
			);

			if (permission == null)
				return false;

			permission.Write = permissionDto.Write;
			permission.Read = permissionDto.Read;
			permission.Delete = permissionDto.Delete;
			permission.ModifyRoot = permissionDto.ModifyRoot;

			_dataContext.Permissions.Update(permission);

			await _dataContext.SaveChangesAsync();
			return true;
		}

		public async Task<PermissionDto> CreatePermission(PermissionDto permissionCreateDto)
		{
			var permission = await _dataContext.Permissions.FirstOrDefaultAsync(permission =>
				permission.RoleId == permissionCreateDto.RoleId &&
				permission.PathPointId == permissionCreateDto.PathPointId);

			if (permission != null)
				return null;

			permission = new Permission
			{
				RoleId = permissionCreateDto.RoleId,
				PathPointId = permissionCreateDto.PathPointId,
				Delete = permissionCreateDto.Delete,
				Read = permissionCreateDto.Read,
				Write = permissionCreateDto.Write,
				ModifyRoot = permissionCreateDto.ModifyRoot
			};

			await _dataContext.Permissions.AddAsync(permission);

			await _dataContext.SaveChangesAsync();

			return new PermissionDto(permission);
		}
	}
}