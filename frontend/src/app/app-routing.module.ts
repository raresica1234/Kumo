import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './shared/guards/auth.guard';
import { NgModule } from '@angular/core';
import { sessionGuard } from './shared/guards/session.guard';

export const routes: Routes = [
  {
    path: 'sessions',
    canActivate: [sessionGuard],
    loadChildren: () => import('./sections/sessions/sessions.module').then((m) => m.SessionsModule),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadChildren: () => import('./sections/layout/layout.module').then((m) => m.LayoutModule),
  },
  {
    path: '**',
    redirectTo: '',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
