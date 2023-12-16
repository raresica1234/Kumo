import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SessionsComponent} from "./sessions.component";
import {SignInComponent} from "./views/sign-in/sign-in.component";
import {SignUpComponent} from "./views/sign-up/sign-up.component";
import {SessionsRoutingModule} from "./sessions-routing.module";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";



@NgModule({
  declarations: [
    SessionsComponent,
    SignInComponent,
    SignUpComponent
  ],
  imports: [
    CommonModule,
    SessionsRoutingModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ]
})
export class SessionsModule { }
