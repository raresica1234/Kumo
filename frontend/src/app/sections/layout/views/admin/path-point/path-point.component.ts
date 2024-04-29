import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../../shared/components/base.component';
import { ActivatedRoute } from '@angular/router';
import { PathPointControllerService } from '../../../../../shared/api-models';
import { Subject, Subscription } from 'rxjs';
import ColumnDefinition from '../../../../../shared/models/table/column-definition';
import TableAction from '../../../../../shared/models/table/table-action';
import { ModalService } from '../../../../../shared/components/modals/modal.service';

@Component({
  selector: 'app-path-point',
  templateUrl: './path-point.component.html',
  styleUrl: './path-point.component.scss',
})
export class PathPointComponent extends BaseComponent implements OnInit, OnDestroy {
  columns!: ColumnDefinition[];
  actions!: TableAction[];
  refreshTable: Subject<boolean> = new Subject<boolean>();
  private subscriptionManager!: Subscription;

  public constructor(
    route: ActivatedRoute,
    private pathPointController: PathPointControllerService,
    private modalService: ModalService,
  ) {
    super(route);

    this.createColumns();
    this.createActions();
  }

  private createColumns() {
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
  }

  private createActions() {
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
          this.modalService.openConfirmModal('Delete Path Point', 'Are you sure you want to delete this Path Point?', {
            onConfirm: () => {
              const sub = this.pathPointController.deletePathPoint(id).subscribe((_) => {
                this.refreshTable.next(true);
              });
              this.subscriptionManager.add(sub);
            },
          });
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
