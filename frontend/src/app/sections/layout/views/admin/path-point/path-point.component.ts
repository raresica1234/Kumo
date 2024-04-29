import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../../shared/components/base.component';
import { ActivatedRoute } from '@angular/router';
import { PathPointControllerService } from '../../../../../shared/api-models';
import { Subject, Subscription } from 'rxjs';
import ColumnDefinition from '../../../../../shared/models/table/column-definition';
import TableAction from '../../../../../shared/models/table/table-action';
import { ModalService } from '../../../../../shared/components/modals/modal.service';
import { AlertService } from '../../../../../shared/services/alert.service';
import FormModalData from '../../../../../shared/models/modal/form-modal-data';

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
    private alertService: AlertService,
  ) {
    super(route);

    this.createColumns();
    this.createActions();

    const obj = { uuid: '6f9b9b8d-bc5f-4374-a88f-a130d6b2f01e', path: '/medida4', root: true };

    const formModalData: FormModalData = {
      object: obj,
      title: 'Edit Path Point',
      type: 'update',
      entries: [
        {
          displayName: 'Path',
          type: 'text',
          name: 'path',
          required: true,
        },
        {
          displayName: 'Is Root',
          type: 'checkbox',
          name: 'root',
          required: false,
        },
      ],
    };
    modalService.openFormModal(formModalData, {
      onConfirm: (object) => console.log(object),
      onClose: () => console.log('closed'),
    });
  }

  private createColumns() {
    this.columns = [
      {
        fieldName: 'path',
        displayName: 'Path',
        sortable: true,
      },
      {
        fieldName: 'root',
        displayName: 'Is Root',
        sortable: true,
      },
    ];
  }

  private createActions() {
    this.actions = [
      {
        name: 'Edit',
        icon: 'edit',
        color: 'primary',
        action: (object) => {
          console.log(object);
          return false;
        },
      },
      {
        name: 'Delete',
        icon: 'delete',
        color: 'warn',
        action: (object) => {
          this.modalService.openConfirmModal(
            { title: 'Delete Path Point', text: 'Are you sure you want to delete this Path Point?' },
            {
              onConfirm: () => {
                const sub = this.pathPointController.deletePathPoint(object.uuid).subscribe((_) => {
                  this.refreshTable.next(true);
                  this.alertService.success('Successfully deleted Path Point');
                });
                this.subscriptionManager.add(sub);
              },
            },
          );
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
