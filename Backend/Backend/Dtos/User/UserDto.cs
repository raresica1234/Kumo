using System.Configuration;

namespace Backend.Dtos.User
{
	public class UserDto
	{
		public string Id { get; set; }
		public string Username { get; set; }

		public UserDto()
		{
		}

		public UserDto(string userId, string username)
		{
			Id = userId;
			Username = username;
		}

		public UserDto(Models.User user)
		{
			Id = user.Id;
			Username = user.Email;
		}
	}
}