import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
} from '@angular/material/dialog';
import FormModalData from '../../../models/modal/form-modal-data';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { NgForOf, NgIf } from '@angular/common';
import { Subscription } from 'rxjs';
import { AlertService } from '../../../services/alert.service';

@Component({
  selector: 'app-form-modal',
  standalone: true,
  imports: [
    MatButtonModule,
    MatDialogClose,
    MatIconModule,
    MatDialogContent,
    MatDialogActions,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    NgForOf,
    NgIf,
  ],
  templateUrl: './form-modal.component.html',
})
export class FormModalComponent implements OnInit, OnDestroy {
  private subscriptionManager: Subscription = new Subscription();

  formGroup: FormGroup = new FormGroup<any>({});

  callFailed: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<FormModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FormModalData,
    private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.data.entries.forEach((entry) => {
      this.formGroup.addControl(
        entry.name,
        new FormControl(this.data.object?.[entry.name] ?? '', entry.required ? [Validators.required] : []),
      );
    });

    this.formGroup.valueChanges.subscribe((_) => (this.callFailed = false));
  }

  createObject() {
    const result = { ...this.data.object };

    this.data.entries.forEach((entry) => {
      result[entry.name] = this.formGroup.get(entry.name)!.value;
    });

    return result;
  }

  submitFunction() {
    const sub = this.data.submitFunction?.(this.createObject()).subscribe({
      next: (result) => {
        this.dialogRef.close(result);
      },
      error: (error) => {
        this.callFailed = true;
        this.alertService.error(error);
      },
    });
    this.subscriptionManager.add(sub);
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }
}
