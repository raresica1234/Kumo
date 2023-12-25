import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomValidators } from '../../../../shared/utils/custom-validators';
import { AuthenticationControllerService, RegisterRequest } from '../../../../shared/api-models';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AlertService } from '../../../../shared/services/alert.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss',
})
export class SignUpComponent implements OnInit, OnDestroy {
  // TODO: Register by invite code (check if it's enabled and if invite code is null display message)
  signUpForm!: FormGroup;
  inviteRequired: boolean = false;
  subscriptionManager: Subscription = new Subscription();

  constructor(
    private router: Router,
    private authenticationController: AuthenticationControllerService,
    private alertService: AlertService,
  ) {}

  public get email() {
    return this.signUpForm.get('email');
  }

  public get username() {
    return this.signUpForm.get('username');
  }

  public get password() {
    return this.signUpForm.get('password');
  }

  public get confirmPassword() {
    return this.signUpForm.get('confirmPassword');
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  ngOnInit(): void {
    this.checkIfRegisterIsByInvite();
    this.initForm();
  }

  singUp() {
    const sub = this.authenticationController.register(this.convertFormToRequest()).subscribe({
      next: () => {
        this.alertService.success('Account successfully created');
        this.router.navigate(['/']);
      },
      error: this.handleError.bind(this),
    });

    this.subscriptionManager.add(sub);
  }

  private convertFormToRequest(): RegisterRequest {
    return {
      email: this.email?.value,
      username: this.username?.value,
      password: this.password?.value,
    } as RegisterRequest;
  }

  private initForm() {
    this.signUpForm = new FormGroup(
      {
        username: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(25)]),
        email: new FormControl('', [Validators.required, Validators.email]),
        password: new FormControl('', [
          Validators.required,
          CustomValidators.password(),
          Validators.minLength(6),
          Validators.maxLength(35),
        ]),
        confirmPassword: new FormControl('', [Validators.required]),
      },
      CustomValidators.confirmPassword(),
    );
  }

  private checkIfRegisterIsByInvite() {
    this.authenticationController.registerByInvites().subscribe((response) => {
      this.inviteRequired = response.value!;
    });
  }

  private handleError(error: string) {
    if (error.toLowerCase().includes('username')) this.username?.setErrors({ validation: error });
    else if (error.toLowerCase().includes('email')) this.email?.setErrors({ validation: error });
  }
}
