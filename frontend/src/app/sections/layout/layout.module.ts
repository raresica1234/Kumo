import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './views/home/home.component';
import { LayoutRoutingModule } from './layout-routing.module';

@NgModule({
  declarations: [HomeComponent],
  imports: [CommonModule, LayoutRoutingModule],
})
export class LayoutModule {}
