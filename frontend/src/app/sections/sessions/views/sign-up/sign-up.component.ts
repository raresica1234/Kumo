import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomValidators } from '../../../../shared/utils/custom-validators';
import { AuthenticationControllerService, RegisterRequest } from '../../../../shared/api-models';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AlertService } from '../../../../shared/services/alert.service';
import { SessionService } from '../../../../shared/services/session/session.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss',
})
export class SignUpComponent implements OnInit, OnDestroy {
  signUpForm!: FormGroup;
  inviteRequired: boolean = false;
  subscriptionManager: Subscription = new Subscription();

  inviteValid: boolean = false;
  inviteMissing: boolean = true;

  invite?: string;

  loading = true;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authenticationController: AuthenticationControllerService,
    private alertService: AlertService,
    private sessionService: SessionService,
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
    this.fetchAndValidateInvite();
    this.initForm();
  }

  fetchAndValidateInvite(): void {
    this.route.queryParams.subscribe((params) => {
      this.invite = params['invite'];
      this.inviteMissing = this.invite === undefined;

      if (this.invite === undefined) {
        this.inviteValid = false;
        this.loading = false;
      } else {
        this.sessionService.setRegisterInvite(this.invite);
        const sub = this.authenticationController.validRegisterInvite().subscribe({
          next: () => (this.inviteValid = true),
          error: () => (this.inviteValid = false),
          complete: () => (this.loading = false),
        });

        this.subscriptionManager.add(sub);
      }
    });
  }

  singUp() {
    const sub = this.authenticationController.register(this.convertFormToRequest(), this.invite).subscribe({
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
