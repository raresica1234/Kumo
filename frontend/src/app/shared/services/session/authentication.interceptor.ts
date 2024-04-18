import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { SessionService } from './session.service';
import { environment } from '../../../../environments/environment';

const noAuthEndpoints = ['authenticate/login', 'authenticate/register', 'authenticate/requireRegisterInvite'];
const registerInviteEndpoints = ['authenticate/validRegisterInvite'];
export const authenticationInterceptor: HttpInterceptorFn = (req, next) => {
  const sessionService = inject(SessionService);

  let token = sessionService.accessToken;
  if (registerInviteNeeded(req)) token = sessionService.registerInvite;

  if (req.url.startsWith(environment.basePath) && authenticationNeeded(req))
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });

  return next(req);
};

const authenticationNeeded = (req: any): boolean => {
  for (let endpoint of noAuthEndpoints) if (req.url.endsWith(endpoint)) return false;
  return true;
};

const registerInviteNeeded = (req: any): boolean => {
  for (let endpoint of registerInviteEndpoints) if (req.url.endsWith(endpoint)) return true;
  return false;
};
