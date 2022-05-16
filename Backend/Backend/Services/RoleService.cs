using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Context;
using Backend.Dtos.Role;
using Backend.Models;
using Microsoft.EntityFrameworkCore;

namespace Backend.Services
{
	public class RoleService : IRoleService
	{
		private readonly DataContext _dataContext;

		public RoleService(DataContext dataContext)
		{
			_dataContext = dataContext;
		}
		
		
		public async Task<List<RoleDto>> GetRoles()
		{
			var results = new List<RoleDto>();
			
			await _dataContext.KumoRoles.ForEachAsync(role =>
			{
				results.Add(new RoleDto(
					role.Id,
					role.Name
				));
			});

			return results;
		}

		public async Task<bool> DeleteRole(Guid id)
		{
			var kumoRole = await _dataContext.KumoRoles.FindAsync(id);

			if (kumoRole == null)
				return false;

			_dataContext.KumoRoles.Remove(kumoRole);

			await _dataContext.SaveChangesAsync();

			return true;
		}

		public async Task<bool> UpdateRole(RoleDto roleDto)
		{
			var kumoRole = await _dataContext.KumoRoles.FindAsync(roleDto.Id);

			if (kumoRole == null)
				return false;

			kumoRole.Name = roleDto.Name;

			_dataContext.KumoRoles.Update(kumoRole);

			await _dataContext.SaveChangesAsync();

			return true;
		}

		public async Task<RoleDto> CreateRole(RoleCreateDto roleCreateDto)
		{
			var kumoRole = await _dataContext.KumoRoles.FirstAsync(kumoRole => kumoRole.Name == roleCreateDto.Name);

			if (kumoRole != null)
				return null;
			
			kumoRole = new KumoRole()
			{
				Id = Guid.NewGuid(),
				Name = roleCreateDto.Name
			};

			await _dataContext.KumoRoles.AddAsync(kumoRole);
			
			await _dataContext.SaveChangesAsync();
			
			return new RoleDto(kumoRole.Id, kumoRole.Name);
		}
	}
}