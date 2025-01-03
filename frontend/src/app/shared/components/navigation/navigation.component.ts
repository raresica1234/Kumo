import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../services/session/auth.service';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { filter, Subscription } from 'rxjs';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { GeneralService } from '../../services/general.service';
import { Feature } from '../../models/features';
import { NgIf } from '@angular/common';
import { MediaMatcher } from '@angular/cdk/layout';

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
    NgIf,
  ],
})
export class NavigationComponent implements OnDestroy, OnInit {
  mobileQuery: MediaQueryList;
  tabletQuery: MediaQueryList;
  private readonly mobileQueryListener: () => void;

  subscriptionManager: Subscription = new Subscription();

  username: string;

  sidenav: boolean = true;
  isAdmin: boolean = false;
  getPathPoint: boolean = false;
  getExplorationRole: boolean = false;
  getPermission: boolean = false;
  getUserRoles: boolean = false;
  adminPanel: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private generalService: GeneralService,
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
  ) {
    this.username = authService.getCurrentUser()!.username;

    this.mobileQuery = media.matchMedia('(max-width:674px)');
    this.tabletQuery = media.matchMedia('(min-width:675px) and (max-width:1079px)');
    this.mobileQueryListener = () => changeDetectorRef.detectChanges();

    this.mobileQuery.addEventListener('change', this.mobileQueryListener);

    if (this.mobileQuery.matches || this.tabletQuery.matches) this.sidenav = false;

    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => {
          return event instanceof NavigationEnd;
        }),
      )
      .subscribe((event: NavigationEnd) => {
        this.adminPanel = event.url.startsWith('/admin');
      });
  }

  ngOnInit(): void {
    this.isAdmin = this.generalService.hasFeature(Feature.ADMIN);
    this.getPathPoint = this.generalService.hasFeature(Feature.GET_PATH_POINT);
    this.getExplorationRole = this.generalService.hasFeature(Feature.GET_EXPLORATION_ROLE);
    this.getPermission = this.generalService.hasFeature(Feature.GET_EXPLORATION_PERMISSION);
    this.getUserRoles = this.generalService.hasFeature(Feature.GET_USER_EXPLORATION_ROLE);
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeEventListener('change', this.mobileQueryListener);
    this.subscriptionManager.unsubscribe();
  }

  logout() {
    this.authService.signOut();
  }

  toggleSidenav() {
    this.sidenav = !this.sidenav;
  }

  navigateTo(location: string) {
    this.router.navigate([location]);
  }
}
