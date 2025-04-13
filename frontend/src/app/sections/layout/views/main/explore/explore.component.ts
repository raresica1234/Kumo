import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ExplorerControllerService, ExplorerFileModel } from '../../../../../shared/api-models';
import { Subscription } from 'rxjs';
import { FileViewerComponent } from '../../../../../shared/components/explore/file-viewer/file-viewer.component';
import { EncoderUtil } from '../../../../../shared/utils/encoder-util';
import FileViewerSettings from '../../../../../shared/models/explore/file-viewer-settings';
import { FileViewerSettingsComponent } from '../../../../../shared/components/explore/file-viewer-settings/file-viewer-settings.component';

@Component({
  selector: 'app-explore',
  standalone: true,
  imports: [FileViewerComponent, FileViewerSettingsComponent],
  templateUrl: './explore.component.html',
  styleUrl: './explore.component.scss',
})
export class ExploreComponent implements OnInit, OnDestroy {
  private currentPath = '';

  files: ExplorerFileModel[] = [];
  fileViewerSettings!: FileViewerSettings;

  private subscriptionManager = new Subscription();

  notFound = false;

  constructor(
    private route: ActivatedRoute,
    private explorerController: ExplorerControllerService,
  ) {}

  ngOnInit() {
    const sub = this.route.url.subscribe((segments) => {
      this.currentPath = EncoderUtil.decode(segments.map((segment) => segment.path).join('/'));

      const encodedPath = encodeURIComponent(this.currentPath);

      const sub = this.explorerController.explore(encodedPath).subscribe({
        next: (data) => {
          this.files = data;
        },
        error: (error) => {
          this.notFound = true;
        },
      });
      this.subscriptionManager.add(sub);
    });
    this.subscriptionManager.add(sub);
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  onSettingsChanged(settings: FileViewerSettings) {
    this.fileViewerSettings = settings;
  }
}
