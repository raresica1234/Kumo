using System;
using System.IO;
using System.Net;
using System.Text.Json;
using System.Threading.Tasks;
using Backend.Extensions;
using Backend.Models;
using Microsoft.AspNetCore.Http;

namespace Backend.Middlewares
{
	public class MiddlewareHandler
	{
		private readonly RequestDelegate nextRequest;

		public MiddlewareHandler(RequestDelegate nextRequest)
		{
			this.nextRequest = nextRequest;
		}

		public async Task InvokeAsync(HttpContext context)
		{
			var response = context.Response;
			response.ContentType = "application/json";

			var bodyBeforeNextRequest = response.Body;
			using var memoryStream = new MemoryStream();

			response.Body = memoryStream;
			var exception = await ExecuteNextRequest(context);
			response.Body = bodyBeforeNextRequest;

			UpdateStatusCodeForException(response, exception);

			var responseMessage = exception?.Message ??
			                      await ObtainResultToWrap(memoryStream);

			await WriteResult(response, responseMessage);
		}

		private async Task<object> ObtainResultToWrap(MemoryStream fromBody)
		{
			var parsedObject = await ToObjectAsync(fromBody);
			if (parsedObject != null)
				return parsedObject;

			var parsedString = await ToStringAsync(fromBody);

			return parsedString.ToInt() ??
			       parsedString.ToBool() as object ??
			       parsedString;
		}

		private async Task<object> ToObjectAsync(MemoryStream stream)
		{
			try
			{
				stream.Seek(0, SeekOrigin.Begin);
				return await JsonSerializer.DeserializeAsync<object>(stream);
			}
			catch
			{
				return null;
			}
		}
		
		private async Task<string> ToStringAsync(MemoryStream stream)
		{
			try
			{
				stream.Seek(0, SeekOrigin.Begin);
				return await new StreamReader(stream).ReadToEndAsync();
			}
			catch
			{
				return null;
			}
		}
		
		private async Task<Exception> ExecuteNextRequest(HttpContext context)
		{
			try
			{
				await nextRequest(context);
			}
			catch (Exception exception)
			{
				return exception;
			}

			return null;
		}

		private void UpdateStatusCodeForException(
			HttpResponse response,
			Exception exception)
		{
			if (exception != null && response.StatusCode == (int) HttpStatusCode.OK)
				response.StatusCode = (int) HttpStatusCode.BadRequest;
		}

		private Task WriteResult(HttpResponse response, object message)
		{
			var result = new ResponseModel(response.StatusCode, message);

			return response.WriteAsync(JsonSerializer.Serialize(result, new JsonSerializerOptions
			{
				PropertyNamingPolicy = JsonNamingPolicy.CamelCase
			}));
		}
	}
}