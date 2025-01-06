import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './views/main/home/home.component';
import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { NavigationComponent } from '../../shared/components/navigation/navigation.component';
import { AdminHomeComponent } from './views/admin/admin-home/admin-home.component';
import { PathPointComponent } from './views/admin/path-point/path-point.component';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { TableComponent } from '../../shared/components/table/table.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [LayoutComponent, HomeComponent, AdminHomeComponent, PathPointComponent],
  imports: [
    CommonModule,
    LayoutRoutingModule,
    NavigationComponent,
    MatTableModule,
    MatSortModule,
    TableComponent,
    MatButtonModule,
    MatIconModule,
  ],
  bootstrap: [LayoutComponent],
})
export class LayoutModule {}
