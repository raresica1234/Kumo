import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserModule } from '@angular/platform-browser';
import { ApiModule, BASE_PATH } from './shared/api-models';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { environment } from '../environments/environment';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { httpErrorInterceptor } from './shared/services/errors/http-error.interceptor';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { ToastrModule } from 'ngx-toastr';
import { ErrorAlertComponent } from './shared/components/alerts/error-alert/error-alert.component';
import { authenticationInterceptor } from './shared/services/session/authentication.interceptor';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { UrlHandlingStrategy } from '@angular/router';
import { IMAGE_LOADER, ImageLoaderConfig } from '@angular/common';

@NgModule({
  declarations: [AppComponent, ErrorAlertComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ApiModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
  ],
  providers: [
    [
      { provide: BASE_PATH, useValue: environment.basePath },
      provideHttpClient(withInterceptors([authenticationInterceptor, httpErrorInterceptor])),
      {
        provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
        useValue: { appearance: 'outline' },
      },

      {
        provide: IMAGE_LOADER,
        useValue: (config: ImageLoaderConfig) => {
          return environment.basePath + `/api/file?path=${encodeURIComponent(config.src)}`;
        },
      },
    ],
  ],
  bootstrap: [AppComponent],
  exports: [],
})
export class AppModule {}
