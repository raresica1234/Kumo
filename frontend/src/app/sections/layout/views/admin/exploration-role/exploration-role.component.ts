import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { TableComponent } from '../../../../../shared/components/table/table.component';
import { BaseComponent } from '../../../../../shared/components/base.component';
import ColumnDefinition from '../../../../../shared/models/table/column-definition';
import TableAction from '../../../../../shared/models/table/table-action';
import { Observable, Subject, Subscription } from 'rxjs';
import FormModalData from '../../../../../shared/models/modal/form-modal-data';
import { ActivatedRoute } from '@angular/router';
import { ExplorationRoleControllerService, Pageable } from '../../../../../shared/api-models';
import { ModalService } from '../../../../../shared/components/modals/modal.service';
import { AlertService } from '../../../../../shared/services/alert.service';
import { GeneralService } from '../../../../../shared/services/general.service';
import { Feature } from '../../../../../shared/models/features';

@Component({
  selector: 'app-exploration-role',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, NgIf, TableComponent],
  templateUrl: './exploration-role.component.html',
})
export class ExplorationRoleComponent extends BaseComponent implements OnInit, OnDestroy {
  columns!: ColumnDefinition[];
  actions!: TableAction[];
  refreshTable: Subject<boolean> = new Subject<boolean>();
  private subscriptionManager: Subscription = new Subscription();

  private explorationRoleFormModalData!: FormModalData;
  private explorationRoleSearchModalData!: FormModalData;

  addExplorationRoleFeature: boolean = false;
  updateExplorationRoleFeature: boolean = false;
  deleteExplorationRoleFeature: boolean = false;

  private searchString: string = '';

  constructor(
    route: ActivatedRoute,
    private explorationRoleController: ExplorationRoleControllerService,
    private modalService: ModalService,
    private alertService: AlertService,
    generalService: GeneralService,
  ) {
    super(route);

    this.addExplorationRoleFeature = generalService.hasFeature(Feature.CREATE_EXPLORATION_ROLE);
    this.updateExplorationRoleFeature = generalService.hasFeature(Feature.UPDATE_EXPLORATION_ROLE);
    this.deleteExplorationRoleFeature = generalService.hasFeature(Feature.DELETE_EXPLORATION_ROLE);

    this.createFormModalData();
    this.createColumns();
    this.createActions();
  }

  private createFormModalData() {
    this.explorationRoleFormModalData = {
      type: 'update',
      entries: [
        {
          displayName: 'Name',
          type: 'text',
          name: 'name',
          required: true,
        },
      ],
    };

    this.explorationRoleSearchModalData = {
      type: 'search',
      title: 'Filter Exploration Roles',
      entries: [
        {
          displayName: 'Name',
          type: 'text',
          name: 'name',
          required: false,
        },
      ],
    };
  }

  private createColumns() {
    this.columns = [
      {
        fieldName: 'name',
        displayName: 'Name',
        sortable: true,
      },
    ];
  }

  private createActions() {
    this.actions = [];
    if (this.updateExplorationRoleFeature)
      this.actions.push({
        name: 'Edit',
        icon: 'edit',
        color: 'primary',
        action: this.updateExplorationRole.bind(this),
      });

    if (this.deleteExplorationRoleFeature)
      this.actions.push({
        name: 'Delete',
        icon: 'delete',
        color: 'warn',
        action: this.deleteExplorationRole.bind(this),
      });
  }

  fetchFunction() {
    return (pageable: Pageable): Observable<any> => {
      return this.explorationRoleController.getExplorationRoles(pageable, this.searchString);
    };
  }

  addExplorationRole() {
    const modalData: FormModalData = {
      ...this.explorationRoleFormModalData,
      title: 'Add Exploration Role',
      object: undefined,
      type: 'add',
      submitFunction: this.explorationRoleController.createExplorationRole.bind(this.explorationRoleController),
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (object) => {
        this.refreshTable.next(true);
        this.alertService.success('Successfully added Exploration Role');
      },
    });
  }

  private updateExplorationRole(object: any) {
    const modalData: FormModalData = {
      ...this.explorationRoleFormModalData,
      title: 'Edit Exploration Role',
      object: object,
      type: 'update',
      submitFunction: this.explorationRoleController.updateExplorationRole.bind(this.explorationRoleController),
    };

    this.modalService.openFormModal(modalData, {
      onConfirm: (object) => {
        this.refreshTable.next(true);
        this.alertService.success('Successfully updated Exploration Role');
      },
    });
  }

  private deleteExplorationRole(object: any) {
    this.modalService.openConfirmModal(
      { title: 'Delete Exploration Role', text: 'Are you sure you want to delete this Exploration Role?' },
      {
        onConfirm: () => {
          const sub = this.explorationRoleController.deleteExplorationRole(object.uuid).subscribe({
            next: (_) => {
              this.refreshTable.next(true);
              this.alertService.success('Successfully deleted Exploration Role');
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

  searchExplorationRole() {
    const modalData = { ...this.explorationRoleSearchModalData, object: { name: this.searchString } };

    this.modalService.openFormModal(modalData, {
      onConfirm: (obj) => {
        this.searchString = obj.name;
        this.refreshTable.next(true);
      },
    });
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }
}
