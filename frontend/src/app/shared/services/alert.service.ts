import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  constructor(private toaster: ToastrService) {
    toaster.toastrConfig.maxOpened = 5;
    toaster.toastrConfig.newestOnTop = false;
    toaster.toastrConfig.positionClass = 'toast-bottom-right';
    toaster.toastrConfig.timeOut = 0;
    toaster.toastrConfig.preventDuplicates = true;
    toaster.toastrConfig.tapToDismiss = false;
  }

  public success(message: string) {}
  public warning(message: string) {}

  public info(message: string) {}

  public error(message: string) {
    this.toaster.error(message);
  }
}
