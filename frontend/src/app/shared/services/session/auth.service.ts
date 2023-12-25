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
import { catchError, concatMap, map, Observable, Subscription, tap, throwError } from 'rxjs';
import { SessionService } from './session.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private user: UserModel | undefined;

  constructor(
    private ipService: IpService,
    private authenticationController: AuthenticationControllerService,
    private sessionService: SessionService,
    private userController: UserControllerService,
  ) {}

  isAuthenticated(): boolean {
    this.user = this.sessionService.getUserData();
    if (this.user) {
      return true;
    } else {
      this.removeSessionDataFromLocalStorage();
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
      catchError((error) => {
        console.log(error);
        return throwError(() => error);
      }),
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
      catchError((error) => {
        console.log(error);
        return throwError(() => error);
      }),
    );
  }

  is2FARequired(): Observable<boolean> {
    return this.authenticationController.require2FA().pipe(map((val) => val.required!));
  }

  private removeSessionDataFromLocalStorage() {
    localStorage.removeItem('userData');
  }

  fetchCurrentUser() {
    return this.userController.getUser().pipe(
      tap((userData) => {
        this.sessionService.setUserData(userData);
        this.user = userData;
      }),
    );
  }

  getCurrentUser() {
    return this.user;
  }
}
