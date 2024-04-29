import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './views/home/home.component';
import { LayoutComponent } from './layout.component';
import { AdminHomeComponent } from './views/admin/admin-home/admin-home.component';
import { PathPointComponent } from './views/admin/path-point/path-point.component';
import { permissionGuard } from '../../shared/guards/permission.guard';
import { Feature } from '../../shared/models/features';

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
        canActivate: [permissionGuard([Feature.ADMIN])],
      },
      {
        path: 'admin/path_point',
        component: PathPointComponent,
        data: { title: 'Path Points' },
        canActivate: [permissionGuard([Feature.ADMIN, Feature.GET_PATH_POINT])],
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LayoutRoutingModule {}
