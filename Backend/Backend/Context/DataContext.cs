using Backend.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace Backend.Context
{
	public class DataContext : IdentityDbContext<User>
	{
		public DataContext(DbContextOptions<DataContext> options) : base(options)
		{
		}
		
		
	}
}