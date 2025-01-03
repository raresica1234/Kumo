import { Component } from '@angular/core';
import { Toast } from 'ngx-toastr';

@Component({
  selector: 'app-error-alert',
  templateUrl: './error-alert.component.html',
  styleUrl: './error-alert.component.scss',
  preserveWhitespaces: false,
})
export class ErrorAlertComponent extends Toast {
  //
  // action(event: Event) {
  //   event.stopPropagation();
  //   this.undoString = 'undid';
  //   this.toastPackage.triggerAction();
  //   return false;
  // }
}
