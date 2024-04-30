import FormModalEntry from './form-modal-entry';
import { Observable } from 'rxjs';

export default interface FormModalData {
  title?: string;
  type: 'add' | 'update' | 'search';
  object?: any;
  entries: FormModalEntry[];
  submitFunction?: (object: any) => Observable<any>;
}
