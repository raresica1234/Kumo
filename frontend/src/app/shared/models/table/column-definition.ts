export default interface ColumnDefinition {
  type?: 'simple' | 'array';
  displayName: string;
  fieldName: string;
  sortable: boolean;
  displayArrayElement?: (subElement: any) => string;
  removable?: boolean;
  removeAction?: (element: any, subElement: any) => void;
}
