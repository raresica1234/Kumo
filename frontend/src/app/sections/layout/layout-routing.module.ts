import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './views/main/home/home.component';
import { LayoutComponent } from './layout.component';
import { AdminHomeComponent } from './views/admin/admin-home/admin-home.component';
import { PathPointComponent } from './views/admin/path-point/path-point.component';
import { permissionGuard } from '../../shared/guards/permission.guard';
import { Feature } from '../../shared/models/features';
import { ExplorationRoleComponent } from './views/admin/exploration-role/exploration-role.component';
import { PermissionComponent } from './views/admin/permission/permission.component';
import { UserExplorationRoleComponent } from './views/admin/user-exploration-role/user-exploration-role.component';
import { ExploreComponent } from './views/main/explore/explore.component';
import { ImageViewerComponent } from './views/main/image-viewer/image-viewer.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        pathMatch: 'full',
        component: ExploreComponent,
      },
      {
        path: 'explore',
        children: [
          {
            path: '**',
            component: ExploreComponent,
          },
        ],
      },
      {
        path: 'view',
        children: [
          {
            path: '**',
            component: ImageViewerComponent,
          },
        ],
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
      {
        path: 'admin/user_explore_roles',
        component: UserExplorationRoleComponent,
        data: { title: 'User Exploration Roles' },
        canActivate: [permissionGuard([Feature.ADMIN, Feature.GET_USER_EXPLORATION_ROLE])],
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LayoutRoutingModule {}
