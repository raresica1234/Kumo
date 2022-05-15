using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace Backend.Models
{
	public class PathPoint
	{
		[Key]
		public Guid Id { get; set; }
		
		public string Path { get; set; }
		
		public bool IsRoot { get; set; }
		
		public ICollection<KumoRole> Roles { get; set; }
		public List<Permission> Permissions { get; set; }
	}
}