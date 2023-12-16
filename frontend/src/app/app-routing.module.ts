import {RouterModule, Routes} from '@angular/router';
import {authGuard} from "./shared/guards/auth.guard";
import {NgModule} from "@angular/core";

export const routes: Routes = [
  {
    path: "",
    canActivate: [authGuard],
    loadChildren: () => import("./sections/sessions/sessions.module").then(m => m.SessionsModule)
  },
  {
    path: "**",
    redirectTo: ""
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
