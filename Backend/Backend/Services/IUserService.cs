using System.Threading.Tasks;
using Backend.Dtos.Authentication;

namespace Backend.Services
{
	public interface IUserService
	{
		Task RegisterAsync(RegisterUserDto registerUserDto);
	}
}