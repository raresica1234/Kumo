import { HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { BehaviorSubject, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { AlertService } from '../alert.service';
import { SessionService } from '../session/session.service';
import { AuthenticationControllerService } from '../../api-models';
import { AuthService } from '../session/auth.service';

const reservedErrors = [
  'Authorization2', // invalid token
];

let isRefreshing = false;
let refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

export const httpErrorInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  const toaster = inject(AlertService);
  const sessionService = inject(SessionService);
  const authController = inject(AuthenticationControllerService);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((httpErrorResponse: HttpErrorResponse) => {
      if (isAuthorizationIssue(httpErrorResponse)) {
        return handleAuthorizationIssue(req, next, sessionService, authController, authService);
      }

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

function isAuthorizationIssue(httpErrorResponse: HttpErrorResponse): boolean {
  return httpErrorResponse.error.errorCode !== undefined && reservedErrors.includes(httpErrorResponse.error.errorCode);
}

function handleAuthorizationIssue(
  request: HttpRequest<any>,
  next: HttpHandlerFn,
  sessionService: SessionService,
  authController: AuthenticationControllerService,
  authService: AuthService,
) {
  if (!isRefreshing) {
    isRefreshing = true;
    refreshTokenSubject.next(null);

    const token = sessionService.refreshToken;

    if (token) {
      return authController.refreshToken(`Bearer ${token}`).pipe(
        switchMap((response) => {
          isRefreshing = false;

          sessionService.saveTokenData(response);
          refreshTokenSubject.next(response.jwtToken);

          return next(addTokenHeader(request, response.jwtToken));
        }),
        catchError((err) => {
          isRefreshing = false;

          // TODO: Investigate issue where sign out is called even though it should just refresh
          authService.signOut();

          return throwError(() => err);
        }),
      );
    }
  }

  return refreshTokenSubject.pipe(
    filter((token) => token !== null),
    take(1),
    switchMap((token) => next(addTokenHeader(request, token))),
  );
}

function addTokenHeader(request: HttpRequest<any>, token: string) {
  return request.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });
}
