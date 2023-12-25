import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { SessionService } from './session.service';
import { environment } from '../../../../environments/environment';

const noAuthEndpoints = ['authenticate/login', 'authenticate/register', 'authenticate/requireRegisterInvite'];

export const authenticationInterceptor: HttpInterceptorFn = (req, next) => {
  const sessionService = inject(SessionService);

  if (req.url.startsWith(environment.basePath) && authenticationNeeded(req))
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${sessionService.accessToken}`,
      },
    });

  return next(req);
};

const authenticationNeeded = (req: any): boolean => {
  for (let endpoint of noAuthEndpoints) if (req.url.endsWith(endpoint)) return false;
  return true;
};
