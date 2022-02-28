using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.AspNetCore.Mvc.ModelBinding;

namespace Backend.Utils
{
	public static class ModelErrorParser
	{
		public static string GetErrors(ModelStateDictionary dictionary)
		{
			if (dictionary.IsValid) return "";
			var errors = (from state in dictionary from error in state.Value.Errors select error.ErrorMessage).ToList();

			return string.Join("\n", errors.ToList());
		}
	}
}