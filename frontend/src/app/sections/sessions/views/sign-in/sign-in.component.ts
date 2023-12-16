import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import SignInModel from "../../../../shared/models/sessions/sign-in-model";
import {AuthService} from "../../../../shared/services/auth.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.scss'
})
export class SignInComponent implements OnInit, OnDestroy {
  signInForm!: FormGroup

  subscriptionManager: Subscription = new Subscription()

  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  singIn() {
    const model: SignInModel = {
      username: this.signInForm.get("email")?.value,
      password: this.signInForm.get("password")?.value,
    }
    this.authService.signIn(model)
      .then(this.handleSuccess.bind(this))
      .catch(this.handleError.bind(this));
  }

  private handleSuccess() {

  }

  private handleError(error: string) {
    console.log(error)
  }

  private initForm() {
    this.signInForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    });
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }
}
