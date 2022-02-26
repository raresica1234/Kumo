﻿using System.ComponentModel.DataAnnotations;

namespace Backend.Dtos.Authentication
{
	public class RegisterUserDto
	{
		[EmailAddress] public string Email { get; set; } = null;
		public string Password { get; set; } = null;
	}
}