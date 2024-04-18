import { Component } from '@angular/core';
import { AuthService } from '../../../../shared/services/session/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  username: string;
  protected readonly localStorage = localStorage;

  constructor(
    private authService: AuthService,
    public router: Router,
  ) {
    this.username = authService.getCurrentUser()!.username;
  }
}
