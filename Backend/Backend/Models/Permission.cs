namespace Backend.Models
{
	public class Permission
	{
		public string RoleId { get; set; }
		public KumoRole KumoRole { get; set; } = null!;
		
		public string PathPointId { get; set; }
		public PathPoint PathPoint { get; set; } = null!;
		
		public bool Read { get; set; }
		public bool Write { get; set; }
		public bool Delete { get; set; }
		public bool ModifyRoot { get; set; }
	}
}