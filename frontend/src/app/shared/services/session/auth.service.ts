import { Injectable } from '@angular/core';
import SignInModel from '../../models/sessions/sign-in-model';
import { IpService } from '../ip.service';
import {
  AccountCodeRequest,
  AuthenticationControllerService,
  LoginRequest,
  TokenDataResponse,
  UserControllerService,
  UserModel,
} from '../../api-models';
import { concatMap, map, Observable, switchMap, tap } from 'rxjs';
import { SessionService } from './session.service';
import { GeneralService } from '../general.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private user: UserModel | null;

  constructor(
    private ipService: IpService,
    private authenticationController: AuthenticationControllerService,
    private sessionService: SessionService,
    private userController: UserControllerService,
    private generalService: GeneralService,
  ) {
    this.user = null;
  }

  isAuthenticated(): boolean {
    this.user = this.sessionService.getUserData();
    if (this.user) {
      return true;
    } else {
      this.sessionService.removeUserDataFromLocalStorage();
      return false;
    }
  }

  public signIn(model: SignInModel): Observable<TokenDataResponse> {
    localStorage.clear();

    return this.ipService.getClientLocation().pipe(
      map((clientLocation) => {
        return {
          clientLocation,
          ...model,
        } as LoginRequest;
      }),
      concatMap((loginRequest) => this.authenticationController.login(loginRequest)),
    );
  }

  public validateTwoFA(code: string): Observable<TokenDataResponse> {
    return this.ipService.getClientLocation().pipe(
      map((clientLocation) => {
        return {
          clientLocation,
          code,
        } as AccountCodeRequest;
      }),
      concatMap((accountCodeRequest) => this.authenticationController.validateCode(accountCodeRequest)),
    );
  }

  is2FARequired(): Observable<boolean> {
    return this.authenticationController.require2FA().pipe(map((val) => val.value!));
  }

  fetchCurrentUser() {
    return this.generalService.init().pipe(
      switchMap((features) => {
        return this.userController.getUser().pipe(
          tap((userData) => {
            this.sessionService.setUserData(userData);
            this.user = userData;
            this.generalService.init();
          }),
        );
      }),
    );
  }

  getCurrentUser() {
    return this.user;
  }

  signOut() {
    this.authenticationController.logout().subscribe({
      next: this.finishSignOut.bind(this),
      error: this.finishSignOut.bind(this),
    });
  }

  private finishSignOut() {
    console.log('finished sign out');
    this.sessionService.removeSessionFromLocalStorage();
    this.generalService.removeFeatures();
    window.location.reload();
  }
}
