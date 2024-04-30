import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../../shared/components/base.component';
import { ActivatedRoute } from '@angular/router';
import { Pageable, PathPointControllerService } from '../../../../../shared/api-models';
import { Observable, Subject, Subscription, tap } from 'rxjs';
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
  columns!: ColumnDefinition[];
  actions!: TableAction[];
  refreshTable: Subject<boolean> = new Subject<boolean>();
  private subscriptionManager: Subscription = new Subscription();

  private pathPointFormModalData!: FormModalData;
  private pathPointSearchModalData!: FormModalData;

  addPathPointPermission: boolean = false;
  updatePathPointPermission: boolean = false;
  deletePathPointPermission: boolean = false;

  private searchString: string = '';

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

    this.createFormModalData();
    this.createColumns();
    this.createActions();
  }

  private createFormModalData() {
    this.pathPointFormModalData = {
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

    this.pathPointSearchModalData = {
      type: 'search',
      title: 'Filter Path Points',
      entries: [
        {
          displayName: 'Path',
          type: 'text',
          name: 'path',
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
        action: this.updatePathPoint.bind(this),
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
    return (pageable: Pageable): Observable<any> => {
      return this.pathPointController.getPathPoints(pageable, this.searchString);
    };
  }

  addPathPoint() {
    const modalData: FormModalData = {
      ...this.pathPointFormModalData,
      title: 'Add Path Point',
      object: undefined,
      type: 'add',
      submitFunction: this.pathPointController.createPathPoint.bind(this.pathPointController),
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (object) => {
        this.refreshTable.next(true);
        this.alertService.success('Successfully added Path Point');
      },
    });
  }

  private updatePathPoint(object: any) {
    const modalData: FormModalData = {
      ...this.pathPointFormModalData,
      title: 'Edit Path Point',
      object: object,
      type: 'update',
      submitFunction: this.pathPointController.updatePathPoint.bind(this.pathPointController),
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (object) => {
        this.refreshTable.next(true);
        this.alertService.success('Successfully updated Path Point');
      },
    });
  }

  private deletePathPoint(object: any) {
    this.modalService.openConfirmModal(
      { title: 'Delete Path Point', text: 'Are you sure you want to delete this Path Point?' },
      {
        onConfirm: () => {
          const sub = this.pathPointController.deletePathPoint(object.uuid).subscribe({
            next: (_) => {
              this.refreshTable.next(true);
              this.alertService.success('Successfully deleted Path Point');
            },
            error: (error) => {
              this.alertService.error(error);
            },
          });
          this.subscriptionManager.add(sub);
        },
      },
    );
  }

  searchPathPoint() {
    const modalData = { ...this.pathPointSearchModalData, object: { path: this.searchString } };

    this.modalService.openFormModal(modalData, {
      onConfirm: (obj) => {
        this.searchString = obj.path;
        this.refreshTable.next(true);
      },
    });
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }
}
