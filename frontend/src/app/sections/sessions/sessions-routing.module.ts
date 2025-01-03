import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignInComponent } from './views/sign-in/sign-in.component';
import { SignUpComponent } from './views/sign-up/sign-up.component';

const routes: Routes = [
  {
    path: 'signin',
    component: SignInComponent,
    data: { title: 'Sign in' },
  },
  {
    path: 'signup',
    component: SignUpComponent,
    data: { title: 'Sign up' },
  },
  {
    path: '**',
    redirectTo: 'signin',
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SessionsRoutingModule {}
