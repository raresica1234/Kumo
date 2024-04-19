import { Injectable } from '@angular/core';
import { UserControllerService } from '../api-models';
import { Feature } from '../models/features';
import { SessionService } from './session/session.service';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GeneralService {
  private featuresList: Feature[] | null = null;

  private isOwner = false;

  constructor(
    private userController: UserControllerService,
    private sessionService: SessionService,
  ) {
    this.featuresList = sessionService.getFeatures();
    this.verifyOwner();
  }

  public init() {
    return this.userController.getFeatures().pipe(
      tap((features) => {
        this.featuresList = features.map((feature) => Feature[feature as keyof typeof Feature]);
        this.sessionService.setFeatures(this.featuresList);
        this.verifyOwner();
      }),
    );
  }

  private verifyOwner() {
    this.isOwner = !!this.featuresList?.includes(Feature.OWNER);
  }

  public hasFeature(feature: Feature) {
    return this.isOwner || !!this.featuresList?.includes(feature);
  }

  removeFeatures() {
    this.featuresList = null;
  }
}
