export default interface TableAction {
  icon: string;
  color?: string;
  action: (id: string) => void;
}
