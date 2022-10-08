using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace Backend.Models
{
	public class KumoRole
	{
		[Key] public Guid Id { get; set; }

		public string Name { get; set; }
		
		public ICollection<PathPoint> PathPoints { get; set; }
		public List<Permission> Permissions { get; set; }
		public ICollection<User> Users { get; set; }
		public List<UserRole> UserRoles { get; set; }
	}
}