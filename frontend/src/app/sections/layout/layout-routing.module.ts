import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './views/home/home.component';
import { LayoutComponent } from './layout.component';
import { AdminHomeComponent } from './views/admin/admin-home/admin-home.component';
import { PathPointComponent } from './views/admin/path-point/path-point.component';
import { permissionGuard } from '../../shared/guards/permission.guard';
import { Feature } from '../../shared/models/features';
import { ExplorationRoleComponent } from './views/admin/exploration-role/exploration-role.component';
import { PermissionComponent } from './views/admin/permission/permission.component';

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
      {
        path: 'admin/exploration_role',
        component: ExplorationRoleComponent,
        data: { title: 'Exploration Roles' },
        canActivate: [permissionGuard([Feature.ADMIN, Feature.GET_EXPLORATION_ROLE])],
      },
      {
        path: 'admin/permission',
        component: PermissionComponent,
        data: { title: 'Permissions' },
        canActivate: [permissionGuard([Feature.ADMIN, Feature.GET_EXPLORATION_PERMISSION])],
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LayoutRoutingModule {}
