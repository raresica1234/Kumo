import {NgModule} from '@angular/core';
import {AppComponent} from "./app.component";
import {BrowserModule} from "@angular/platform-browser";
import {ApiModule, BASE_PATH} from "./shared/api-models";
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {environment} from "../environments/environment";
import {AppRoutingModule} from "./app-routing.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {httpErrorInterceptor} from "./shared/services/errors/http-error.interceptor";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ApiModule,
    BrowserAnimationsModule
  ],
  providers: [
    [
      {provide: BASE_PATH, useValue: environment.basePath},
      provideHttpClient(withInterceptors([httpErrorInterceptor]))
    ]
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
