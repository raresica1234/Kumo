import FormModalEntry from './form-modal-entry';

export default interface FormModalData {
  title: string;
  type: 'add' | 'update';
  object?: any;
  entries: FormModalEntry[];
}
