import { Component, Inject, OnInit } from '@angular/core';
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
  styleUrl: './form-modal.component.scss',
})
export class FormModalComponent implements OnInit {
  formGroup: FormGroup = new FormGroup<any>({});

  constructor(
    public dialogRef: MatDialogRef<FormModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FormModalData,
  ) {}

  ngOnInit(): void {
    this.data.entries.forEach((entry) => {
      this.formGroup.addControl(entry.name, new FormControl(this.data.object[entry.name], [Validators.required]));
    });
  }

  test() {}
}
