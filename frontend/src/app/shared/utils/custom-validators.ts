import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class CustomValidators {
  private static PASSWORD_REGEX = /(?=.*[A-Z])(?=.*[a-z]).*/;

  public static password(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      return !this.PASSWORD_REGEX.test(value) ? { password: true } : null;
    };
  }
}
