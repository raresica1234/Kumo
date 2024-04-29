import { ThemePalette } from '@angular/material/core';

export default interface TableAction {
  name: string;
  icon: string;
  color?: ThemePalette;
  action: (object: any) => void;
}
