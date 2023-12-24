import { HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { AlertService } from '../alert.service';

const reservedErrors = [
  'Authorization2', // invalid token
  'Authorization3', // invalid refresh token
];

export const httpErrorInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  const toaster = inject(AlertService);

  return next(req).pipe(
    catchError((httpErrorResponse: HttpErrorResponse) => {
      let message = '';
      if (httpErrorResponse.error.message !== undefined) {
        message = httpErrorResponse.error.message;
      } else {
        toaster.error('Server unavailable');
      }

      return throwError(() => message);
    }),
  );
};
