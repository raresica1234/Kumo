import { Component } from '@angular/core';
import { AuthService } from '../../../../shared/services/session/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  username: string;

  constructor(private authService: AuthService) {
    this.username = authService.getCurrentUser()!.username;
  }
}
