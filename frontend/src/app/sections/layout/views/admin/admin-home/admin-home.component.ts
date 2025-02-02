import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../../shared/components/base.component';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { AdminControllerService, ThumbnailStatusResponse } from '../../../../../shared/api-models';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrl: './admin-home.component.scss',
})
export class AdminHomeComponent extends BaseComponent implements OnInit, OnDestroy {
  private subscriptionManager: Subscription = new Subscription();

  thumbnailStatusData?: ThumbnailStatusResponse;

  public constructor(
    route: ActivatedRoute,
    private adminService: AdminControllerService,
  ) {
    super(route);
  }

  override ngOnInit() {
    super.ngOnInit();
    this.fetchThumbnailData();
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  private fetchThumbnailData() {
    const sub = this.adminService.getThumbnailStatus().subscribe((result) => {
      this.thumbnailStatusData = result;
    });
    this.subscriptionManager.add(sub);
  }
}
