import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class CustomValidators {
  private static PASSWORD_REGEX = /(?=.*[A-Z])(?=.*[a-z]).*/;

  public static password(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      return !this.PASSWORD_REGEX.test(value) ? { password: true } : null;
    };
  }

  public static confirmPassword(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value.password;
      const confirmPasswordValue = control.value.confirmPassword;

      const confirmPasswordControl = control.get('confirmPassword');
      if (confirmPasswordControl != null) {
        if (confirmPasswordValue.length !== 0 && value !== confirmPasswordValue)
          confirmPasswordControl.setErrors({ confirmPassword: true, ...confirmPasswordControl.errors });
        else {
          // Javascript hacks time baby
          let errors = confirmPasswordControl.errors;
          if (errors && errors['confirmPassword']) delete errors['confirmPassword'];

          confirmPasswordControl.setErrors(errors);
        }
      }
      return null;
    };
  }
}
