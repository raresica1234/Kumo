import {HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {catchError, throwError} from "rxjs";

export const httpErrorInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  return next(req).pipe(
    catchError((httpErrorResponse: HttpErrorResponse) => {
      console.log(httpErrorResponse.error)
      return throwError(() => httpErrorResponse);
    })
  )
};
