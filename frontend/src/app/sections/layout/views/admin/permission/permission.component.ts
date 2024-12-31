import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { TableComponent } from '../../../../../shared/components/table/table.component';
import { BaseComponent } from '../../../../../shared/components/base.component';
import { Observable, Subject, Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import {
  ExplorationRoleControllerService,
  Pageable,
  PathPointControllerService,
  PermissionControllerService,
} from '../../../../../shared/api-models';
import { ModalService } from '../../../../../shared/components/modals/modal.service';
import { AlertService } from '../../../../../shared/services/alert.service';
import { GeneralService } from '../../../../../shared/services/general.service';
import { Feature } from '../../../../../shared/models/features';
import ColumnDefinition from '../../../../../shared/models/table/column-definition';
import TableAction from '../../../../../shared/models/table/table-action';
import FormModalData from '../../../../../shared/models/modal/form-modal-data';

@Component({
  selector: 'app-permission',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, NgIf, TableComponent],
  templateUrl: './permission.component.html',
})
export class PermissionComponent extends BaseComponent implements OnDestroy {
  columns!: ColumnDefinition[];
  actions!: TableAction[];
  refreshTable: Subject<boolean> = new Subject<boolean>();

  private subscriptionManager: Subscription = new Subscription();

  addPermissionFeature: boolean = false;
  updatePermissionFeature: boolean = false;
  deletePermissionFeature: boolean = false;

  private permissionFormModalData!: FormModalData;
  private permissionSearchModalData!: FormModalData;

  private pathSearchString: string = '';
  private roleSearchString: string = '';

  constructor(
    route: ActivatedRoute,
    private permissionController: PermissionControllerService,
    private pathPointController: PathPointControllerService,
    private explorationRoleController: ExplorationRoleControllerService,
    private modalService: ModalService,
    private alertService: AlertService,
    generalService: GeneralService,
  ) {
    super(route);

    this.addPermissionFeature = generalService.hasFeature(Feature.CREATE_EXPLORATION_PERMISSION);
    this.updatePermissionFeature = generalService.hasFeature(Feature.UPDATE_EXPLORATION_PERMISSION);
    this.deletePermissionFeature = generalService.hasFeature(Feature.DELETE_EXPLORATION_PERMISSION);

    this.createFormModalData();
    this.createColumns();
    this.createActions();
  }

  fetchFunction() {
    return (pageable: Pageable): Observable<any> => {
      return this.permissionController.getPermissions(pageable, this.pathSearchString, this.roleSearchString);
    };
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  addPermission() {
    const modalData: FormModalData = {
      ...this.permissionFormModalData,
      title: 'Add Permission',
      type: 'add',
      object: undefined,
      submitFunction: this.permissionController.createPermission.bind(this.permissionController),
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (_) => {
        this.refreshTable.next(true);
        this.alertService.success('Successfully created Permission');
      },
    });
  }

  private createFormModalData() {
    this.permissionFormModalData = {
      type: 'update',
      entries: [
        {
          displayName: 'Path Point',
          type: 'dropdown',
          name: 'pathPointId',
          searchField: 'path',
          required: true,
          displayFunction: (object) => {
            return `${object.root ? 'Root: ' : ''}${object.path}`;
          },
          fetchFunction: (string) => {
            return this.pathPointController.getPathPoints({ size: 5 }, string);
          },
          getFullObjectPropertyNameForUpdate: () => {
            return 'pathPoint';
          },
        },
        {
          displayName: 'Exploration Role',
          type: 'dropdown',
          name: 'explorationRoleId',
          searchField: 'name',
          required: true,
          fetchFunction: (string) => {
            return this.explorationRoleController.getExplorationRoles({ size: 5 }, string);
          },
          getFullObjectPropertyNameForUpdate: () => {
            return 'explorationRole';
          },
        },
        {
          displayName: 'Read',
          type: 'checkbox',
          name: 'read',
          required: false,
        },
        {
          displayName: 'Write',
          type: 'checkbox',
          name: 'write',
          required: false,
        },
        {
          displayName: 'Delete',
          type: 'checkbox',
          name: 'delete',
          required: false,
        },
        {
          displayName: 'Modify Root',
          type: 'checkbox',
          name: 'modifyRoot',
          required: false,
        },
      ],
    };

    this.permissionSearchModalData = {
      type: 'search',
      title: 'Filter Permissions',
      entries: [
        {
          displayName: 'Path',
          type: 'text',
          name: 'path',
          required: false,
        },
        {
          displayName: 'Role name',
          type: 'text',
          name: 'role',
          required: false,
        },
      ],
    };
  }

  searchPermission() {
    const modalData = {
      ...this.permissionSearchModalData,
      object: { path: this.pathSearchString, role: this.roleSearchString },
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (obj) => {
        this.pathSearchString = obj.path;
        this.roleSearchString = obj.role;
        this.refreshTable.next(true);
      },
    });
  }

  private createColumns() {
    this.columns = [
      {
        fieldName: 'pathPoint.path',
        displayName: 'Path',
        sortable: true,
      },
      {
        fieldName: 'pathPoint.root',
        displayName: 'Is Root',
        sortable: true,
      },
      {
        fieldName: 'explorationRole.name',
        displayName: 'Role Name',
        sortable: true,
      },
      {
        fieldName: 'read',
        displayName: 'Read',
        sortable: true,
      },
      {
        fieldName: 'write',
        displayName: 'Write',
        sortable: true,
      },
      {
        fieldName: 'delete',
        displayName: 'Delete',
        sortable: true,
      },
      {
        fieldName: 'modifyRoot',
        displayName: 'Modify Root',
        sortable: true,
      },
    ];
  }

  private createActions() {
    this.actions = [];
    if (this.updatePermissionFeature)
      this.actions.push({
        name: 'Edit',
        icon: 'edit',
        color: 'primary',
        action: this.updatePermission.bind(this),
      });

    if (this.deletePermissionFeature)
      this.actions.push({
        name: 'Delete',
        icon: 'delete',
        color: 'warn',
        action: this.deletePermission.bind(this),
      });
  }

  private deletePermission(object: any) {
    this.modalService.openConfirmModal(
      { title: 'Delete Permission', text: 'Are you sure you want to delete this Permission?' },
      {
        onConfirm: () => {
          const sub = this.permissionController.deletePermission(object.uuid).subscribe({
            next: (_) => {
              this.refreshTable.next(true);
              this.alertService.success('Successfully deleted Permission');
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

  private convertPermissionToModalObject(object: any) {
    return { ...object, pathPointId: object.pathPoint.uuid, explorationRoleId: object.explorationRole.uuid };
  }

  private updatePermission(object: any) {
    const modalData: FormModalData = {
      ...this.permissionFormModalData,
      title: 'Update Permission',
      type: 'update',
      object: this.convertPermissionToModalObject(object),
      submitFunction: this.permissionController.updatePermission.bind(this.permissionController),
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (_) => {
        this.refreshTable.next(true);
        this.alertService.success('Successfully updated Permission');
      },
    });
  }
}
