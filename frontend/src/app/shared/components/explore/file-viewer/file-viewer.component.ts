import { Component, Input } from '@angular/core';
import { ExplorerFileModel } from '../../../api-models';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { EncoderUtil } from '../../../utils/encoder-util';
import { NavigationHandlerDirective } from '../../../utils/navigation-directive';
import { ImageComponent } from '../../image/image.component';

@Component({
  selector: 'app-file-viewer',
  standalone: true,
  imports: [MatCardModule, MatIconModule, NavigationHandlerDirective, ImageComponent],
  templateUrl: './file-viewer.component.html',
  styleUrl: './file-viewer.component.scss',
})
export class FileViewerComponent {
  // TODO: figure out a way to make the grid usable on mobile (the file name space is very small)
  // TODO: if auth expires when an image is fetched, refresh token isn't called

  @Input() files: ExplorerFileModel[] = [];

  navigateTo(file: ExplorerFileModel) {
    if (file.type !== 'DIRECTORY') return;
    if (!file.fullPath) return;
    return 'explore/' + EncoderUtil.encode(file.fullPath);
  }

  viewImage(file: ExplorerFileModel) {
    if (!file.fullPath) return;
    return 'view/' + EncoderUtil.encode(file.fullPath);
  }
}
