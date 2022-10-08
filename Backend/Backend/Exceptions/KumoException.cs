using System;
using System.Linq;
using Microsoft.AspNetCore.Mvc.ModelBinding;

namespace Backend.Exceptions
{
	public class KumoException : Exception
	{
		public KumoException(string message) : base(message)
		{
		}

		public static void ValidateModel(ModelStateDictionary modelState)
		{
			if (modelState.IsValid)
				return;

			var errorMessages = modelState.Values
				.SelectMany(entry => entry.Errors)
				.Select(error => error.ErrorMessage);

			throw new KumoException(string.Join("\n", errorMessages));
		}
	}
}