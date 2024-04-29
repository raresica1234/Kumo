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
import { GeneralService } from '../../../../../shared/services/general.service';
import { Feature } from '../../../../../shared/models/features';

@Component({
  selector: 'app-path-point',
  templateUrl: './path-point.component.html',
})
export class PathPointComponent extends BaseComponent implements OnInit, OnDestroy {
  // TODO: Some errors can be caught in the modal so the user doesn't have to rewrite everything: path point already exists, invalid path
  // TODO: Check if path is valid (exists) on backend, also show in modal

  columns!: ColumnDefinition[];
  actions!: TableAction[];
  refreshTable: Subject<boolean> = new Subject<boolean>();
  private subscriptionManager!: Subscription;

  private pathPointFormModalData!: FormModalData;

  addPathPointPermission: boolean = false;
  updatePathPointPermission: boolean = false;
  deletePathPointPermission: boolean = false;

  public constructor(
    route: ActivatedRoute,
    private pathPointController: PathPointControllerService,
    private modalService: ModalService,
    private alertService: AlertService,
    generalService: GeneralService,
  ) {
    super(route);

    this.addPathPointPermission = generalService.hasFeature(Feature.CREATE_PATH_POINT);
    this.updatePathPointPermission = generalService.hasFeature(Feature.UPDATE_PATH_POINT);
    this.deletePathPointPermission = generalService.hasFeature(Feature.DELETE_PATH_POINT);

    this.createForModalData();
    this.createColumns();
    this.createActions();
  }

  private createForModalData() {
    this.pathPointFormModalData = {
      object: {},
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
    this.actions = [];
    if (this.updatePathPointPermission)
      this.actions.push({
        name: 'Edit',
        icon: 'edit',
        color: 'primary',
        action: this.editPathPoint.bind(this),
      });

    if (this.deletePathPointPermission)
      this.actions.push({
        name: 'Delete',
        icon: 'delete',
        color: 'warn',
        action: this.deletePathPoint.bind(this),
      });
  }

  override ngOnInit() {
    super.ngOnInit();
  }

  fetchFunction() {
    return this.pathPointController.getPathPoint.bind(this.pathPointController);
  }

  addPathPoint() {
    this.pathPointFormModalData = {
      ...this.pathPointFormModalData,
      title: 'Add Path Point',
      object: undefined,
      type: 'add',
    };

    this.modalService.openFormModal(this.pathPointFormModalData, {
      onConfirm: (object) => {
        const sub = this.pathPointController.createPathPoint(object).subscribe((_) => {
          this.refreshTable.next(true);
          this.alertService.success('Successfully added Path Point');
        });
        this.subscriptionManager.add(sub);
      },
    });
  }

  private editPathPoint(object: any) {
    this.pathPointFormModalData = {
      ...this.pathPointFormModalData,
      title: 'Edit Path Point',
      object: object,
      type: 'update',
    };

    this.modalService.openFormModal(this.pathPointFormModalData, {
      onConfirm: (object) => {
        const sub = this.pathPointController.updatePathPoint(object).subscribe((_) => {
          this.refreshTable.next(true);
          this.alertService.success('Successfully updated Path Point');
        });
        this.subscriptionManager.add(sub);
      },
    });
  }

  private deletePathPoint(object: any) {
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
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }
}
