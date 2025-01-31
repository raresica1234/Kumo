import { Component, OnDestroy, OnInit } from '@angular/core';
import { WebsocketService } from '../../shared/services/websocket.service';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})
export class LayoutComponent implements OnInit, OnDestroy {
  constructor(private websocketService: WebsocketService) {}

  ngOnInit() {
    // this.websocketService.connect();
  }

  ngOnDestroy() {
    // this.websocketService.disconnect();
  }
}
