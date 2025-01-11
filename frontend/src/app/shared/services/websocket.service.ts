import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Client, Frame } from '@stomp/stompjs';
import { SessionService } from './session/session.service';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  private stompClient!: Client;

  private disconnected = false;

  constructor(private sessionService: SessionService) {}

  connect() {
    if (!this.sessionService.getUserData()?.uuid || !this.sessionService.accessToken) {
      console.log('Session user data or access token not present');
      return;
    }
    this.stompClient = new Client({
      brokerURL: this.getWebsocketUrl(),

      connectHeaders: {
        login: this.sessionService.getUserData()?.uuid!,
        passcode: this.sessionService.accessToken!,
      },
      debug: (msg: string) => console.log(msg),
      onDisconnect: this.handleDisconnect.bind(this),
      logRawCommunication: true,
    });

    this.stompClient.activate();
  }

  disconnect() {
    this.disconnected = true;
    if (this.stompClient) this.stompClient.deactivate();
  }

  private handleDisconnect(frame: Frame) {
    if (this.disconnected) {
      console.log('Graceful disconnect');
    }
    console.log('Web socket disconnected', frame);
  }

  private getWebsocketUrl(): string {
    const appUrl = environment.basePath;
    const isSecure = appUrl.startsWith('https');

    const protocolLength = 'http://'.length;

    const baseUrl = appUrl.substring(protocolLength + +isSecure);

    return 'ws' + (isSecure ? 's' : '') + '://' + baseUrl + '/api/websocket';
  }
}
