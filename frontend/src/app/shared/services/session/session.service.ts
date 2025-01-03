import { Injectable } from '@angular/core';
import { TokenDataResponse, UserModel } from '../../api-models';
import { Feature } from '../../models/features';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private static ACCESS_TOKEN_STORAGE_LOCATION = 'accessToken';
  private static REFRESH_TOKEN_STORAGE_LOCATION = 'refreshToken';
  private static TWO_FA_REQUIRED_STORAGE_LOCATION = 'twoFARequired';
  private static USER_DATA_STORAGE_LOCATION = 'user';
  private static FEATURES_STORAGE_LOCATION = 'features';
  public accessToken;
  public refreshToken;
  public registerInvite: string | null = null;

  constructor() {
    this.accessToken = localStorage.getItem(SessionService.ACCESS_TOKEN_STORAGE_LOCATION);
    this.refreshToken = localStorage.getItem(SessionService.REFRESH_TOKEN_STORAGE_LOCATION);
  }

  saveTokenData(tokenData: TokenDataResponse) {
    this.accessToken = tokenData.jwtToken;
    this.refreshToken = tokenData.refreshToken;

    localStorage.setItem(SessionService.ACCESS_TOKEN_STORAGE_LOCATION, tokenData.jwtToken);
    localStorage.setItem(SessionService.REFRESH_TOKEN_STORAGE_LOCATION, tokenData.refreshToken);
  }

  removeSessionFromLocalStorage() {
    this.removeUserDataFromLocalStorage();
    this.removeTokensFromLocalStorage();
    this.removeFeaturesFromLocalStorage();
  }

  removeTokensFromLocalStorage() {
    localStorage.removeItem(SessionService.ACCESS_TOKEN_STORAGE_LOCATION);
    localStorage.removeItem(SessionService.REFRESH_TOKEN_STORAGE_LOCATION);
  }

  removeUserDataFromLocalStorage() {
    localStorage.removeItem(SessionService.USER_DATA_STORAGE_LOCATION);
  }

  removeFeaturesFromLocalStorage() {
    localStorage.removeItem(SessionService.FEATURES_STORAGE_LOCATION);
  }

  setTwoFARequired(value: boolean) {
    if (!value) localStorage.removeItem(SessionService.TWO_FA_REQUIRED_STORAGE_LOCATION);
    else localStorage.setItem(SessionService.TWO_FA_REQUIRED_STORAGE_LOCATION, 'true');
  }

  getFeatures(): Feature[] | null {
    const value = localStorage.getItem(SessionService.FEATURES_STORAGE_LOCATION);
    if (value) return JSON.parse(value);
    return null;
  }

  setFeatures(value: Feature[]) {
    localStorage.setItem(SessionService.FEATURES_STORAGE_LOCATION, JSON.stringify(value));
  }

  getUserData(): UserModel | null {
    const value = localStorage.getItem(SessionService.USER_DATA_STORAGE_LOCATION);
    if (value) return JSON.parse(value);
    return null;
  }

  isTwoFARequired() {
    return !!localStorage.getItem(SessionService.TWO_FA_REQUIRED_STORAGE_LOCATION);
  }

  setUserData(userData: UserModel) {
    localStorage.setItem(SessionService.USER_DATA_STORAGE_LOCATION, JSON.stringify(userData));
  }

  setRegisterInvite(token: string) {
    this.registerInvite = token;
  }
}
