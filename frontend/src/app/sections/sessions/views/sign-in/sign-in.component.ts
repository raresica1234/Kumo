import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import SignInModel from '../../../../shared/models/sessions/sign-in-model';
import { AuthService } from '../../../../shared/services/session/auth.service';
import { Subscription } from 'rxjs';
import { TokenDataResponse } from '../../../../shared/api-models';
import { SessionService } from '../../../../shared/services/session/session.service';
import { AlertService } from '../../../../shared/services/alert.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.scss',
})
export class SignInComponent implements OnInit, OnDestroy {
  signInForm!: FormGroup;
  twoFAForm!: FormGroup;
  // TODO: Figure out what to do when a user's code has expired, they'll just be stuck in this screen
  // maybe on 2fa flow, if the user refreshes, just remove everything and let them start over
  // TODO: Add 2fa sending by mail or some other way
  twoFAFlow: boolean = false;

  // TODO: Decide if when not loading the loading bar should disappear, if so, fix the spacing
  loading: boolean = false;

  subscriptionManager: Subscription = new Subscription();

  constructor(
    private authService: AuthService,
    private sessionService: SessionService,
    private alertService: AlertService,
    private router: Router,
  ) {}

  public get username() {
    return this.signInForm.get('username');
  }

  public get password() {
    return this.signInForm.get('password');
  }

  public get code() {
    return this.twoFAForm.get('code');
  }

  ngOnInit(): void {
    this.initForm();
    this.twoFAFlow = this.sessionService.isTwoFARequired();
  }

  signIn() {
    this.loading = true;
    const model: SignInModel = {
      username: this.username?.value,
      password: this.password?.value,
    };
    const sub = this.authService.signIn(model).subscribe({
      next: this.handleSuccess.bind(this),
      error: this.handleError.bind(this),
    });
    this.subscriptionManager.add(sub);
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  validateTwoFA() {
    this.loading = true;
    const sub = this.authService.validateTwoFA(this.code?.value).subscribe({
      next: (tokenData) => {
        this.sessionService.saveTokenData(tokenData);
        this.finishSignIn();
      },
      error: (error) => {
        this.code?.setErrors({ validation: error });
        this.loading = false;
      },
    });

    this.subscriptionManager.add(sub);
  }

  private handleSuccess(tokenData: TokenDataResponse) {
    this.sessionService.saveTokenData(tokenData);
    const sub = this.authService.is2FARequired().subscribe((value) => {
      this.sessionService.setTwoFARequired(value);
      this.twoFAFlow = value;
      if (!value) this.finishSignIn();
      else this.loading = false;
    });

    this.subscriptionManager.add(sub);
  }

  private finishSignIn() {
    const sub = this.authService.fetchCurrentUser().subscribe(() => {
      this.router.navigate(['/']);
    });

    this.subscriptionManager.add(sub);
  }

  private handleError(error: string) {
    this.loading = false;
    if (error.toLowerCase().includes('username')) {
      this.username?.setErrors({ validation: error });
    } else if (error.toLowerCase().includes('password')) {
      this.password?.setErrors({ validation: error });
    } else this.alertService.error(error);
  }

  private initForm() {
    this.signInForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });

    this.twoFAForm = new FormGroup({
      code: new FormControl('', [Validators.required]),
    });
  }
}
