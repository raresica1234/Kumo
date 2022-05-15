using System.Collections.Generic;
using Microsoft.AspNetCore.Identity;

namespace Backend.Models
{
	public class User : IdentityUser
	{
		public ICollection<KumoRole> Roles { get; set; }
		public List<UserRole> UserRoles { get; set; }
	}
}