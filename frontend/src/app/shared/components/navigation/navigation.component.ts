import { Component, OnDestroy } from '@angular/core';
import { AuthService } from '../../services/session/auth.service';
import { Router, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Subscription } from 'rxjs';
import { Location } from '@angular/common';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.scss',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatSidenavModule,
    MatListModule,
    RouterOutlet,
  ],
})
export class NavigationComponent implements OnDestroy {
  subscriptionManager: Subscription = new Subscription();

  username: string;
  protected readonly localStorage = localStorage;

  sidenav: boolean = true;

  constructor(
    private authService: AuthService,
    public router: Router,
  ) {
    this.username = authService.getCurrentUser()!.username;
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  logout() {
    const sub = this.authService.signOut().subscribe(() => {
      window.location.reload();
    });

    this.subscriptionManager.add(sub);
  }

  toggleSidenav() {
    this.sidenav = !this.sidenav;
  }
}
