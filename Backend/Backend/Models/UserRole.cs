using System;

namespace Backend.Models
{
	public class UserRole
	{
		public string UserId { get; set; }
		public User User { get; set; } = null!;
		
		public Guid RoleId { get; set; }
		public KumoRole KumoRole { get; set; } = null!;
	}
}