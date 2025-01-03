import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  isDarkTheme: boolean = true;

  get darkTheme() {
    return this.isDarkTheme;
  }
}
