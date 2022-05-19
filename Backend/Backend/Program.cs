using System;
using System.IO;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;

namespace Backend
{
	public class Program
	{
		public static void Main(string[] args)
		{
			// Console.WriteLine(Path.GetFullPath(Path.Combine(@"D:/Pictures", "..")));
			//
			// var d = new DirectoryInfo(Path.GetFullPath(Path.Combine(@"D:/Pictures", "..")));
			// var e = new DirectoryInfo(Path.GetFullPath(Path.Combine(@"D:/Pictures/apowidjaoiwjdo", "..")));
			//
			// Console.WriteLine(d.Parent);
			// Console.WriteLine(d.Root);
			// Console.WriteLine(e.Parent);
			// Console.WriteLine(e.Root);
			//
			// return;
			CreateHostBuilder(args).Build().Run();
		}

		public static IHostBuilder CreateHostBuilder(string[] args)
		{
			return Host.CreateDefaultBuilder(args)
				.ConfigureWebHostDefaults(webBuilder => { webBuilder.UseStartup<Startup>(); });
		}
	}
}