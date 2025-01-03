import { Component, Input } from '@angular/core';
import { ExplorerFileModel } from '../../../api-models';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { EncoderUtil } from '../../../utils/encoder-util';
import { environment } from '../../../../../environments/environment';
import { SecurePipe } from '../../../security/secure-pipe';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-file-viewer',
  standalone: true,
  imports: [MatCardModule, MatIconModule, SecurePipe, AsyncPipe],
  templateUrl: './file-viewer.component.html',
  styleUrl: './file-viewer.component.scss',
})
export class FileViewerComponent {
  // TODO: automatic gap so elements fit nicely
  // TODO: Cutoff for text that is too long
  @Input() files: ExplorerFileModel[] = [];

  constructor(private router: Router) {}

  navigateTo(file: ExplorerFileModel) {
    if (file.type !== 'DIRECTORY') return;
    if (!file.fullPath) return;
    this.router.navigate(['explore/' + EncoderUtil.encode(file.fullPath)]);
  }

  getImageSourceFor(file: ExplorerFileModel) {
    return environment.basePath + '/api/file?path=' + encodeURIComponent(file.fullPath!);
  }
}
