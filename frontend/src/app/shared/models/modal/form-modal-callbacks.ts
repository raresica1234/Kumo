export default interface FormModalCallbacks {
  onConfirm: (object: any) => void;
  onClose?: () => void;
}
