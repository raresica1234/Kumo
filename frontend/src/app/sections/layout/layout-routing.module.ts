import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './views/home/home.component';
import { LayoutComponent } from './layout.component';
import { AdminHomeComponent } from './views/admin/admin-home/admin-home.component';
import { PathPointComponent } from './views/admin/path-point/path-point.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        component: HomeComponent,
        data: { title: 'Home' },
      },
      {
        path: 'admin',
        component: AdminHomeComponent,
        data: { title: 'Admin Panel' },
      },
      {
        path: 'admin/path_point',
        component: PathPointComponent,
        data: { title: 'Path Points' },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LayoutRoutingModule {}
