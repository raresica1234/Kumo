import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import FileViewerSettings from '../../../models/explore/file-viewer-settings';
import { FileViewType } from '../../../models/explore/file-view-type';
import { MatButton, MatFabButton, MatIconButton, MatMiniFabButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu';

@Component({
  selector: 'app-file-viewer-settings',
  standalone: true,
  imports: [MatIconButton, MatIcon, MatButton, MatFabButton, MatMenu, MatMenuItem, MatMenuTrigger, MatMiniFabButton],
  templateUrl: './file-viewer-settings.component.html',
  styleUrl: './file-viewer-settings.component.scss',
})
export class FileViewerSettingsComponent implements OnInit {
  @Output('settingsChanged') settingsChanged = new EventEmitter<FileViewerSettings>();

  fileViewerSettings?: FileViewerSettings = undefined;

  constructor() {
    const localStorageSettings = localStorage.getItem('fileViewerSettings');
    if (localStorageSettings == null) {
      this.fileViewerSettings = this.getDefaultFileViewerSettings();
    } else {
      try {
        this.fileViewerSettings = JSON.parse(localStorageSettings);
      } catch (e) {
        // could not parse json
        this.fileViewerSettings = this.getDefaultFileViewerSettings();
      }
    }
  }

  ngOnInit(): void {
    this.publishSettings();
  }

  private getDefaultFileViewerSettings(): FileViewerSettings {
    return {
      viewType: FileViewType.LIST,
    };
  }

  private publishSettings() {
    localStorage.setItem('fileViewerSettings', JSON.stringify(this.fileViewerSettings));
    this.settingsChanged.emit(this.fileViewerSettings);
  }

  protected readonly FileViewType = FileViewType;

  changeView(viewType: FileViewType) {
    this.fileViewerSettings = { ...this.fileViewerSettings, viewType };
    this.publishSettings();
  }
}
