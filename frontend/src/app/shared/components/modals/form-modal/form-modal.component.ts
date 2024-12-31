import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import FormModalData from '../../../models/modal/form-modal-data';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { debounceTime, Subscription } from 'rxjs';
import { AlertService } from '../../../services/alert.service';
import { MatAutocompleteModule, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import FormModalEntry from '../../../models/modal/form-modal-entry';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-form-modal',
  standalone: true,
  imports: [
    MatButtonModule,
    MatDialogClose,
    MatIconModule,
    MatDialogActions,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MatChipsModule,
  ],
  templateUrl: './form-modal.component.html',
})
export class FormModalComponent implements OnInit, OnDestroy {
  // TODO: Fix the edge case when selecting a dropdown option switches to
  // display mode but the input box is still highlighted, meaning the user can
  // search in it. If the user types anything in the box, the first input is
  // ignored as the mode switches from display to search

  // TODO: typing gibberish doesn't just display nothing, shit gets reset

  // TODO: left to do for multiselect-dropdown:
  //      - autocomplete active, when clicking remove on chip, it doesn't update

  private subscriptionManager: Subscription = new Subscription();

  formGroup: FormGroup = new FormGroup<any>({});

  values: Map<string, any[]> = new Map();

  multiSelectValues: Map<string, any[]> = new Map();
  multiSelectRemainingValues: Map<string, any[]> = new Map();

  callFailed: boolean = false;

  currentSelected: Map<string, any> = new Map();

  filters: Map<string, string | null> = new Map();

  ignoreFirstValueChange: Map<string, boolean> = new Map();

  constructor(
    public dialogRef: MatDialogRef<FormModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FormModalData,
    private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.data.entries.forEach((entry) => {
      const formControl = new FormControl(
        { value: this.data.object?.[entry.name] ?? '', disabled: !!entry.disabled },
        entry.required ? [Validators.required] : [],
      );

      if (entry.type === 'multiselect-dropdown') {
        this.multiSelectValues.set(entry.name, this.data.object?.[entry.name] ?? []);
      }

      this.formGroup.addControl(entry.name, formControl);
      if (entry.type === 'dropdown' || entry.type === 'multiselect-dropdown') {
        formControl.valueChanges.pipe(debounceTime(200)).subscribe((value) => {
          if (this.ignoreFirstValueChange.get(entry.name)) {
            this.ignoreFirstValueChange.set(entry.name, false);
            return;
          }
          if (this.filters.get(entry.name) === null) {
            this.filters.set(entry.name, '');
          } else {
            this.filters.set(entry.name, value);
          }
          this.fetchNecessaryObjectsForDropdown(entry);
        });
        this.fetchNecessaryObjectsForDropdown(entry);
      }
    });

    this.formGroup.valueChanges.subscribe((_) => (this.callFailed = false));

    this.createCurrentSelectedObjectsForDropdowns();
  }

  displayDropdownField(entry: FormModalEntry) {
    return (id: any): string => {
      const currentSelected = this.currentSelected.get(entry.name);
      if (currentSelected === undefined) return '';

      if (currentSelected.uuid === id) {
        return this.displayField(entry, currentSelected);
      }

      const result = this.values.get(entry.name)?.find((element) => element.uuid === id);
      if (result === undefined) return id;

      return this.displayField(entry, result);
    };
  }

  displayField(entry: FormModalEntry, value: any) {
    if (value === undefined) return;
    if (entry.displayFunction !== undefined) return entry.displayFunction(value);
    else if (entry.searchField !== undefined) return value[entry.searchField];
    else return value;
  }

  createObject() {
    const result = { ...this.data.object };

    this.data.entries.forEach((entry) => {
      if (entry.type === 'dropdown') {
        result[entry.name] = this.currentSelected.get(entry.name)['uuid'];
      } else if (entry.type === 'multiselect-dropdown') {
        const entryValues = this.multiSelectValues.get(entry.name)?.map((object) => object['uuid']);
        result[entry.name] = entryValues || [];
      } else {
        result[entry.name] = this.formGroup.get(entry.name)!.value;
      }
    });

    return result;
  }

  submitFunction() {
    if (this.data.submitFunction) {
      const sub = this.data.submitFunction(this.createObject()).subscribe({
        next: (result) => {
          this.dialogRef.close(result);
        },
        error: (error) => {
          this.callFailed = true;
          this.alertService.error(error);
        },
      });
      this.subscriptionManager.add(sub);
    } else {
      this.dialogRef.close(this.createObject());
    }
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  resetFilter(entry: FormModalEntry) {
    this.filters.set(entry.name, null);
    this.switchToDisplayMode(entry);
  }

  switchToSearchMode(entry: FormModalEntry) {
    const control = this.getControlForName(entry.name);
    if (control === null) return;
    if (entry.searchField !== undefined) {
      const value = this.currentSelected.get(entry.name);
      if (value === undefined) return;

      control.setValue(value[entry.searchField]);
      this.ignoreFirstValueChange.set(entry.name, true);
    }
  }

  switchToDisplayMode(entry: FormModalEntry) {
    const control = this.getControlForName(entry.name);
    if (control === null) return;
    if (entry.searchField !== undefined)
      control.setValue(this.displayField(entry, this.currentSelected.get(entry.name)));
  }

  optionSelected(entry: FormModalEntry, event: MatAutocompleteSelectedEvent) {
    const value = event.option.value;
    const currentValue = this.values.get(entry.name)?.find((obj) => obj['uuid'] == value);
    if (entry.type === 'multiselect-dropdown') {
      const currentValues = this.multiSelectValues.get(entry.name) ?? [];
      currentValues.push(currentValue);
      this.multiSelectValues.set(entry.name, currentValues);
    } else {
      this.currentSelected.set(entry.name, currentValue);
    }
  }

  private fetchNecessaryObjectsForDropdown(entry: FormModalEntry) {
    if (entry.type === 'dropdown') {
      entry.fetchFunction?.(this.filters.get(entry.name) ?? '').subscribe((values) => {
        this.values.set(entry.name, values.content);
      });
    } else if (entry.type === 'multiselect-dropdown') {
      entry.fetchFunction?.(this.filters.get(entry.name) ?? '').subscribe((values) => {
        this.values.set(entry.name, values.content);
        this.updateRemainingMultiSelectValues(entry);
      });
    }
  }

  private updateRemainingMultiSelectValues(entry: FormModalEntry) {
    const remainingValues: any[] = this.values.get(entry.name) || [];

    for (const currentValue of this.multiSelectValues.get(entry.name) || []) {
      const index = remainingValues.findIndex((val) => val['uuid'] === currentValue['uuid']);
      if (index < 0) continue;
      remainingValues.splice(index, 1);
    }

    this.multiSelectRemainingValues.set(entry.name, remainingValues);
  }

  private createCurrentSelectedObjectsForDropdowns() {
    if (this.data.object === undefined) return;

    this.data.entries.forEach((entry) => {
      if (entry.type === 'dropdown' || entry.type === 'multiselect-dropdown') {
        const fullPropertyName = entry.getFullObjectPropertyNameForUpdate!();
        const currentObject = this.data.object[fullPropertyName];

        this.currentSelected.set(entry.name, currentObject);
        // Remove the full object as it pollutes the response of the modal
        delete this.data.object[fullPropertyName];
      }
    });
  }

  private getControlForName(name: string): AbstractControl | null {
    return this.formGroup.get(name);
  }

  removeFromMultiSelectDropdown(entry: FormModalEntry, value: any) {
    const currentValues = this.multiSelectValues.get(entry.name) ?? [];
    const index = currentValues?.indexOf(value);
    if (index < 0) return;
    currentValues.splice(index, 1);
    this.multiSelectValues.set(entry.name, currentValues);
    this.updateRemainingMultiSelectValues(entry);
  }
}
