import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ClientLocationModel } from '../api-models';
import { catchError, map, Observable, of } from 'rxjs';

interface IpData {
  query: string;
  country: string;
  regionName: string;
}

@Injectable({
  providedIn: 'root',
})
export class IpService {
  // TODO: look into replacing this by using nginx's real ip

  private static API = 'http://ip-api.com/json/';

  constructor(private http: HttpClient) {}

  getClientLocation(): Observable<ClientLocationModel> {
    return this.http.get<IpData>(IpService.API).pipe(
      map((value) => {
        return {
          locationType: 'WEB',
          ipAddress: value.query,
          country: value.country,
        } as ClientLocationModel;
      }),
      catchError(() =>
        of({
          locationType: 'WEB',
          ipAddress: '', // Ip will be figured out on the server
          country: '',
        } as ClientLocationModel),
      ),
    );
  }
}
