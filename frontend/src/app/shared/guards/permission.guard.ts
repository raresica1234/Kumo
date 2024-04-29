import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { GeneralService } from '../services/general.service';
import { Feature } from '../models/features';

export function permissionGuard(permissions: Feature[]): CanActivateFn {
  return (route, state) => {
    const router = inject(Router);
    const generalService = inject(GeneralService);

    if (!generalService.hasFeatures(permissions)) {
      router.navigate(['']);
      return false;
    }
    return true;
  };
}
