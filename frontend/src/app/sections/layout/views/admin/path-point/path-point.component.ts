import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../../shared/components/base.component';
import { ActivatedRoute } from '@angular/router';
import { PathPointControllerService } from '../../../../../shared/api-models';
import { Subscription } from 'rxjs';
import ColumnDefinition from '../../../../../shared/models/table/column-definition';
import TableAction from '../../../../../shared/models/table/table-action';

@Component({
  selector: 'app-path-point',
  templateUrl: './path-point.component.html',
  styleUrl: './path-point.component.scss',
})
export class PathPointComponent extends BaseComponent implements OnInit, OnDestroy {
  columns: ColumnDefinition[];
  private subscriptionManager: Subscription = new Subscription();
  actions: TableAction[];

  public constructor(
    route: ActivatedRoute,
    private pathPointController: PathPointControllerService,
  ) {
    super(route);

    this.columns = [
      {
        name: 'path',
        displayName: 'Path',
        sortable: true,
      },
      {
        name: 'root',
        displayName: 'Is Root',
        sortable: true,
      },
    ];

    this.actions = [
      {
        icon: 'edit',
        color: 'primary',
        action: (id) => {
          console.log(id);
          return false;
        },
      },
      {
        icon: 'delete',
        color: 'warn',
        action: (id) => {
          console.log(id);
          return false;
        },
      },
    ];
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  override ngOnInit() {
    super.ngOnInit();
  }

  fetchFunction() {
    return this.pathPointController.getPathPoint.bind(this.pathPointController);
  }
}
