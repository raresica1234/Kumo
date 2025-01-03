import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmModalComponent } from './confirm-modal/confirm-modal.component';
import ConfirmModalCallbacks from '../../models/modal/confirm-modal-callbacks';
import FormModalData from '../../models/modal/form-modal-data';
import FormModalCallbacks from '../../models/modal/form-modal-callbacks';
import { FormModalComponent } from './form-modal/form-modal.component';
import { ConfirmModalData } from '../../models/modal';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  constructor(private dialog: MatDialog) {}

  public openConfirmModal(data: ConfirmModalData, callbacks: ConfirmModalCallbacks): void {
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      width: '300px',
      data: data,
    });

    dialogRef.afterClosed().subscribe((data) => {
      if (data === 'CONFIRM') callbacks.onConfirm();
      else callbacks.onClose?.();
    });
  }

  public openFormModal(data: FormModalData, callbacks: FormModalCallbacks): void {
    const dialogRef = this.dialog.open(FormModalComponent, {
      width: '350px',
      data: data,
    });

    dialogRef.afterClosed().subscribe((data) => {
      if (data !== undefined) callbacks.onConfirm(data);
      else callbacks.onClose?.();
    });
  }
}
