namespace Backend.Dtos.Role
{
	public class RoleCreateDto
	{
		public string Name { get; set; }

		public RoleCreateDto(string name)
		{
			Name = name;
		}
	}
}