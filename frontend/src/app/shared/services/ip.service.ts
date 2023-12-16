import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ClientLocationModel} from "../api-models";
import {firstValueFrom} from "rxjs";

interface IpData {
  query: string;
  country: string;
  regionName: string;
}

@Injectable({
  providedIn: 'root'
})
export class IpService {
  private static API = "http://ip-api.com/json/";


  constructor(private http: HttpClient) {
  }

  async getClientLocation(): Promise<ClientLocationModel> {
    try {
      const ipData = await firstValueFrom(this.http.get<IpData>(IpService.API));
      return {
        locationType: "WEB",
        ipAddress: ipData.query,
        country: ipData.country
      } as ClientLocationModel;
    } catch {
      return {
        locationType: "WEB",
        ipAddress: "", // Ip will be figured out on the server
        country: ""
      } as ClientLocationModel;
    }
  }
}
