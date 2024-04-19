import { Component } from '@angular/core';
import { ThemeService } from './shared/services/theme.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'kumo';
  isDarkEnabled: boolean;

  constructor(themeService: ThemeService) {
    // TODO: when refreshing page, it takes a bit for the dark theme to apply, try to see if it can be fixed
    this.isDarkEnabled = themeService.darkTheme;
    if (this.isDarkEnabled) {
      document.getElementsByTagName('body')[0].className += ' dark';
    }
  }
}
