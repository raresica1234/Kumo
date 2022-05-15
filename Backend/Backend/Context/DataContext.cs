using Backend.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace Backend.Context
{
	public class DataContext : IdentityDbContext<User>
	{
		public DbSet<PathPoint> PathPoints { get; set; }
		public DbSet<Permission> Permissions { get; set; }
		public DbSet<KumoRole> KumoRoles { get; set; }
		public DbSet<UserRole> KumoUserRoles { get; set; }

		public DataContext(DbContextOptions<DataContext> options) : base(options)
		{
		}

		protected override void OnModelCreating(ModelBuilder builder)
		{
			builder.Entity<KumoRole>()
				.HasMany(role => role.Users)
				.WithMany(user => user.Roles)
				.UsingEntity<UserRole>(
					userRole => userRole
						.HasOne(userRole => userRole.User)
						.WithMany(user => user.UserRoles)
						.HasForeignKey(userRole => userRole.UserId),
					
					userRole => userRole
						.HasOne(userRole => userRole.KumoRole)
						.WithMany(role => role.UserRoles)
						.HasForeignKey(userRole => userRole.RoleId),

					userRole =>
					{
						userRole.HasKey(t => new {t.RoleId, t.UserId});
					}
				);

			builder.Entity<PathPoint>()
				.HasMany(pathPoint => pathPoint.Roles)
				.WithMany(role => role.PathPoints)
				.UsingEntity<Permission>(
					permission => permission
						.HasOne(permission => permission.KumoRole)
						.WithMany(role => role.Permissions)
						.HasForeignKey(permission => permission.RoleId),

					permission => permission
						.HasOne(permission => permission.PathPoint)
						.WithMany(pathPoint => pathPoint.Permissions)
						.HasForeignKey(permission => permission.PathPointId),

					permission =>
					{
						permission.HasKey(t => new {t.PathPointId, t.RoleId});
					}
				);
				
			base.OnModelCreating(builder);
		}
	}
}