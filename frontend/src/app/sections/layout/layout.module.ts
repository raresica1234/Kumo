import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './views/home/home.component';
import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { NavigationComponent } from '../../shared/components/navigation/navigation.component';
import { AppModule } from '../../app.module';

@NgModule({
  declarations: [LayoutComponent, HomeComponent],
  imports: [CommonModule, LayoutRoutingModule, NavigationComponent],
  bootstrap: [LayoutComponent],
})
export class LayoutModule {}
