import { Injectable } from '@angular/core';
import { TokenDataResponse } from '../../api-models';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private static ACCESS_TOKEN_STORAGE_LOCATION = 'accessToken';
  private static REFRESH_TOKEN_STORAGE_LOCATION = 'refreshToken';
  private static TWO_FA_REQUIRED_STORAGE_LOCATION = 'twoFARequired';
  private static USER_DATA_STORAGE_LOCATION = 'user';
  public accessToken;
  public refreshToken;

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

  removeLocalStorageItems() {
    localStorage.removeItem(SessionService.ACCESS_TOKEN_STORAGE_LOCATION);
    localStorage.removeItem(SessionService.REFRESH_TOKEN_STORAGE_LOCATION);
  }

  setTwoFARequired(value: boolean) {
    if (!value) localStorage.removeItem(SessionService.TWO_FA_REQUIRED_STORAGE_LOCATION);
    else localStorage.setItem(SessionService.TWO_FA_REQUIRED_STORAGE_LOCATION, 'true');
  }

  getUserData() {
    return localStorage.getItem(SessionService.USER_DATA_STORAGE_LOCATION);
  }

  isTwoFARequired() {
    return !!localStorage.getItem(SessionService.TWO_FA_REQUIRED_STORAGE_LOCATION);
  }
}
