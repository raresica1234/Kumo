import { ValidatorFn } from '@angular/forms';

export default interface FormModalEntry {
  displayName: string;
  name: string;
  type: 'text' | 'checkbox';
  required: boolean;
  validators?: ValidatorFn[];
}
