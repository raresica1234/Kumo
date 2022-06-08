using System.Net;

namespace Backend.Models
{
	public class ResponseModel
	{
		public HttpStatusCode StatusCode { get; set; }

		public object Payload { get; set; }

		public ResponseModel(HttpStatusCode statusCode, object payload)
		{
			StatusCode = statusCode;
			Payload = payload;
		}

		public ResponseModel(int statusCode, object payload)
		{
			StatusCode = (HttpStatusCode)statusCode;
			Payload = payload;
		}
	}
}