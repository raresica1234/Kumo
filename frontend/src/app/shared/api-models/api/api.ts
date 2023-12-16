export * from './authenticationController.service';
import { AuthenticationControllerService } from './authenticationController.service';
export * from './explorerController.service';
import { ExplorerControllerService } from './explorerController.service';
export const APIS = [AuthenticationControllerService, ExplorerControllerService];
