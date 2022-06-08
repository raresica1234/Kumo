namespace Backend.Extensions
{
	public static class DataExtensions
	{
		public static bool? ToBool(this string fromString)
		{
			try
			{
				return bool.Parse(fromString);
			}
			catch
			{
				return null;
			}
		}

		public static int? ToInt(this string fromString)
		{
			try
			{
				return int.Parse(fromString);
			}
			catch
			{
				return null;
			}
		}
	}
}