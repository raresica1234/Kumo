import { Injectable } from '@angular/core';
import SignInModel from "../models/sessions/sign-in-model";
import {IpService} from "./ip.service";
import {AuthenticationControllerService, LoginRequest} from "../api-models";
import {Subscription} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private subscriptionManager: Subscription = new Subscription();

  constructor(private ipService: IpService,
              private authenticationController: AuthenticationControllerService) { }

  isAuthenticated(): boolean {
    const user = localStorage.getItem("userData");
    if (user) {
      return true;
    } else {
      this.removeSessionDataFromLocalStorage();
      return false;
    }
  }

  private removeSessionDataFromLocalStorage() {
    localStorage.removeItem("userData");
  }

  public signIn(model: SignInModel): Promise<string> {
    return new Promise<string>(async (resolve, reject) => {
      localStorage.clear();
      const loginRequest = {
        clientLocation: await this.ipService.getClientLocation(),
        ...model
      } as LoginRequest;
      const sub = this.authenticationController.login(loginRequest)
        .subscribe({
          next: tokenData => {
            console.log(tokenData)
            resolve("OK");
          },
          error: error => {
            console.log(error)
            reject(error);
          }
        });

      this.subscriptionManager.add(sub);
    })

  }

}
