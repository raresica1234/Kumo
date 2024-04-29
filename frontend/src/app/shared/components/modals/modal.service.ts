import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmModalComponent } from './confirm-modal/confirm-modal.component';
import ConfirmModalCallbacks from '../../models/confirm-modal-callbacks';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  constructor(private dialog: MatDialog) {}

  public openConfirmModal(title: string, text?: string, callbacks?: ConfirmModalCallbacks): void {
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      width: '300px',
      data: {
        text: text,
        title,
      },
    });

    dialogRef.afterClosed().subscribe((data) => {
      if (data === 'CONFIRM') callbacks?.onConfirm();
      else callbacks?.onClose?.();
    });
  }
}
