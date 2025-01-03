import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../../shared/components/base.component';
import { ActivatedRoute } from '@angular/router';
import { GeneralService } from '../../../../../shared/services/general.service';
import { Feature } from '../../../../../shared/models/features';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { TableComponent } from '../../../../../shared/components/table/table.component';
import ColumnDefinition from '../../../../../shared/models/table/column-definition';
import TableAction from '../../../../../shared/models/table/table-action';
import { Observable, Subject, Subscription } from 'rxjs';
import {
  ExplorationRoleControllerService,
  ExplorationUserRoleControllerService,
  Pageable,
} from '../../../../../shared/api-models';
import { ModalService } from '../../../../../shared/components/modals/modal.service';
import { AlertService } from '../../../../../shared/services/alert.service';
import FormModalData from '../../../../../shared/models/modal/form-modal-data';

@Component({
  selector: 'app-user-exploration-role',
  standalone: true,
  imports: [MatIconModule, MatButtonModule, TableComponent],
  templateUrl: './user-exploration-role.component.html',
})
export class UserExplorationRoleComponent extends BaseComponent implements OnDestroy {
  columns!: ColumnDefinition[];
  actions!: TableAction[];
  refreshTable: Subject<boolean> = new Subject<boolean>();
  updateUserRoleFeature: boolean = false;
  deleteUserRoleFeature: boolean = false;
  private subscriptionManager: Subscription = new Subscription();
  private userExplorationRoleSearchModalData!: FormModalData;
  private userSetExplorationRolesModalData!: FormModalData;

  private userSearchString: string = '';
  private roleSearchString: string = '';

  constructor(
    route: ActivatedRoute,
    private explorationUserRoleController: ExplorationUserRoleControllerService,
    private explorationRoleController: ExplorationRoleControllerService,
    private modalService: ModalService,
    private alertService: AlertService,
    generalService: GeneralService,
  ) {
    super(route);

    this.updateUserRoleFeature = generalService.hasFeature(Feature.SET_USER_EXPLORATION_ROLES);
    this.deleteUserRoleFeature = generalService.hasFeature(Feature.UNASSIGN_USER_EXPLORATION_ROLE);

    this.createFormModalData();
    this.createColumns();
    this.createActions();
  }

  fetchFunction() {
    return (pageable: Pageable): Observable<any> => {
      return this.explorationUserRoleController.getUserExplorationRole(
        pageable,
        this.userSearchString,
        this.roleSearchString,
      );
    };
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  searchUserExplorationRoles() {
    const modalData = {
      ...this.userExplorationRoleSearchModalData,
      object: { username: this.userSearchString, role: this.roleSearchString },
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (obj) => {
        this.userSearchString = obj.username;
        this.roleSearchString = obj.role;
        this.refreshTable.next(true);
      },
    });
  }

  private createColumns() {
    this.columns = [
      {
        fieldName: 'username',
        displayName: 'User',
        sortable: true,
      },
      {
        type: 'array',
        fieldName: 'explorationRoles',
        displayName: 'Exploration Roles',
        sortable: false,
        displayArrayElement: (subElement) => subElement['name'],
        removable: true,
        removeAction: this.unassignUserRole.bind(this),
      },
    ];
  }

  private unassignUserRole(user: any, role: any) {
    this.modalService.openConfirmModal(
      {
        title: 'Delete role from user?',
        text: `Are you sure you want to delete the role ${role.name} from user ${user.username}?`,
      },
      {
        onConfirm: () => {
          const sub = this.explorationUserRoleController.unassignUserExplorationRole(user.uuid, role.uuid).subscribe({
            next: (_) => {
              this.refreshTable.next(true);
              this.alertService.success('Successfully deleted role from user');
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

  private createActions() {
    this.actions = [];
    if (this.updateUserRoleFeature)
      this.actions.push({
        name: 'Edit',
        icon: 'edit',
        color: 'primary',
        action: this.updateUserRoles.bind(this),
      });
  }

  private updateUserRoles(object: any) {
    const modalData: FormModalData = {
      ...this.userSetExplorationRolesModalData,
      title: `Edit ${object.username} exploration roles`,
      object: JSON.parse(JSON.stringify(object)), // Copy object so the table and the modal aren't linked together
      submitFunction: this.explorationUserRoleController.setUserExplorationRoles.bind(
        this.explorationUserRoleController,
      ),
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (_) => {
        this.refreshTable.next(true);
        this.alertService.success('Successfully updated exploration roles for user');
      },
    });
  }

  private createFormModalData() {
    this.userSetExplorationRolesModalData = {
      type: 'update',
      entries: [
        {
          displayName: 'User',
          type: 'text',
          name: 'username',
          disabled: true,
          required: false,
        },
        {
          displayName: 'Roles',
          type: 'multiselect-dropdown',
          name: 'explorationRoles',
          searchField: 'name',
          required: false,
          fetchFunction: (string) => {
            return this.explorationRoleController.getExplorationRoles({ size: 5 }, string);
          },
          getFullObjectPropertyNameForUpdate: () => {
            return 'name';
          },
        },
      ],
    };
    this.userExplorationRoleSearchModalData = {
      type: 'search',
      title: 'Filter User Exploration Roles',
      entries: [
        {
          displayName: 'User',
          type: 'text',
          name: 'username',
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
}
