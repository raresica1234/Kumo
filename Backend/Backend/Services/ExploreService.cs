using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Backend.Context;
using Backend.Dtos.Explorer;
using Backend.Dtos.Role;
using Backend.Models;

namespace Backend.Services
{
	public class ExploreService : IExploreService
	{
		private readonly DataContext _dataContext;
		private readonly IUserService _userService;

		public ExploreService(DataContext dataContext, IUserService userService)
		{
			_dataContext = dataContext;
			_userService = userService;
		}

		public async Task<List<ExploreResultDto>> Explore(string path)
		{
			var isAdministrator = await _userService.IsAdministrator();

			if (!isAdministrator)
			{
				var relevantPermissions = await GetRelevantPathPoints(path);

				if (!Directory.Exists(path))
					return null;

				var effectivePermission = GetEffectivePermission(relevantPermissions, path);

				if (!effectivePermission.Read)
					return null;

				var folders = Directory.GetDirectories(path);

				var results = new List<ExploreResultDto>();

				foreach (var folder in folders)
				{
					var currentFolderPerms = relevantPermissions.Where(permission =>
						Path.GetFullPath(permission.PathPoint.Path) == Path.GetFullPath(folder));


					var exploreResultDto = new ExploreResultDto(Path.GetFileName(folder), Path.GetFullPath(folder),
						FileSystemEntryType.Directory, effectivePermission.Write, effectivePermission.Delete,
						effectivePermission.ModifyRoot);

					var folderPerms = currentFolderPerms as Permission[] ?? currentFolderPerms.ToArray();
					if (folderPerms.Length > 0)
					{
						var read = folderPerms.Aggregate(false, (prev, next) => prev || next.Read);
						if (!read)
							continue;

						var write = folderPerms.Aggregate(false, (prev, next) => prev || next.Write);
						var delete = folderPerms.Aggregate(false, (prev, next) => prev || next.Delete);
						var modifyRoot = folderPerms.Aggregate(false, (prev, next) => prev || next.ModifyRoot);

						exploreResultDto.CanWrite = write;
						exploreResultDto.CanDelete = delete;
						exploreResultDto.CanModifyRoot = modifyRoot;
					}

					results.Add(exploreResultDto);
				}

				foreach (var file in Directory.GetFiles(path))
				{
					results.Add(new ExploreResultDto(Path.GetFileName(file), Path.GetFullPath(file),
						FileSystemEntryType.File, effectivePermission.Write, effectivePermission.Delete,
						effectivePermission.ModifyRoot));
				}

				return results;
			}
			else
			{
				List<ExploreResultDto> results = new List<ExploreResultDto>();

				string[] directories = Directory.GetDirectories(path);

				foreach (var directory in directories)
				{
					results.Add(new ExploreResultDto(
						Path.GetFileName(directory),
						Path.GetFullPath(directory),
						FileSystemEntryType.Directory,
						true,
						true,
						true
					));
				}

				string[] files = Directory.GetFiles(path);

				foreach (var file in files)
				{
					results.Add(new ExploreResultDto(
						Path.GetFileName(file),
						Path.GetFullPath(file),
						FileSystemEntryType.File,
						true,
						true,
						true
					));
				}

				return results;
			}
		}

		public async Task<List<ExploreResultDto>> GetRootPathPoints()
		{
			var pathPoints = await GetAccessibleRootPathPoints();

			List<ExploreResultDto> results = new List<ExploreResultDto>();

			foreach (var pathPoint in pathPoints)
			{
				results.Add(new ExploreResultDto(
					pathPoint.Path,
					Path.GetFullPath(pathPoint.Path),
					FileSystemEntryType.Directory,
					true,
					true,
					true
				));
			}

			return results;
		}

		private async Task<List<PathPoint>> GetAccessibleRootPathPoints()
		{
			var isAdministrator = await _userService.IsAdministrator();

			if (!isAdministrator)
			{
				var user = await _dataContext.Users.Include(user => user.Roles)
					.ThenInclude(role => role.Permissions)
					.ThenInclude(permission => permission.PathPoint)
					.FirstOrDefaultAsync(user => user.Id == _userService.GetCurrentUserId());

				if (user == null)
					return null;

				var pathPoints = new List<PathPoint>();

				foreach (var role in user.Roles)
				{
					role.Permissions.ForEach(permission =>
					{
						if (permission.Read && permission.PathPoint.IsRoot)
							pathPoints.Add(permission.PathPoint);
					});
				}


				return pathPoints.Distinct().ToList();
			}


			return await _dataContext.PathPoints.Where(pathPoint => pathPoint.IsRoot).ToListAsync();
		}

		private async Task<List<Permission>> GetRelevantPathPoints(string path)
		{
			var user = await _dataContext.Users.Include(user => user.Roles)
				.ThenInclude(role => role.Permissions)
				.ThenInclude(permission => permission.PathPoint)
				.FirstOrDefaultAsync(user => user.Id == _userService.GetCurrentUserId());

			var nonTrashPath = Path.GetFullPath(path);

			return (from role in user.Roles
				from permission in role.Permissions
				where Path.GetFullPath(permission.PathPoint.Path).StartsWith(nonTrashPath) ||
				      nonTrashPath.StartsWith(Path.GetFullPath(permission.PathPoint.Path))
				select permission).ToList();
		}

		private Permission GetEffectivePermission(List<Permission> relevantPermissions, string path)
		{
			var currentDirectory = Path.GetFullPath(path);
			do
			{
				var permissions = relevantPermissions
					.Where(permission =>
						Path.GetFullPath(permission.PathPoint.Path) == Path.GetFullPath(currentDirectory))
					.ToList();

				if (permissions.Count > 0)
					return new Permission
					{
						Read = permissions.Aggregate(false, (b, permission) => b || permission.Read),
						Write = permissions.Aggregate(false, (b, permission) => b || permission.Write),
						Delete = permissions.Aggregate(false, (b, permission) => b || permission.Delete),
						ModifyRoot = permissions.Aggregate(false, (b, permission) => b || permission.ModifyRoot)
					};
			} while ((currentDirectory = new DirectoryInfo(currentDirectory).Parent?.FullName) != null);

			return new Permission
			{
				Read = false,
				Write = false,
				Delete = false,
				ModifyRoot = false
			};
		}
	}
}