import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EncoderUtil } from '../../../../../shared/utils/encoder-util';
import { Subscription } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { SecurePipe } from '../../../../../shared/security/secure-pipe';
import { environment } from '../../../../../../environments/environment';

@Component({
  selector: 'app-image-viewer',
  standalone: true,
  imports: [AsyncPipe, SecurePipe],
  templateUrl: './image-viewer.component.html',
  styleUrl: './image-viewer.component.scss',
})
export class ImageViewerComponent implements OnInit {
  currentPath = '';

  private subscriptionManager = new Subscription();

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    const sub = this.route.url.subscribe((segments) => {
      this.currentPath = EncoderUtil.decode(segments.map((segment) => segment.path).join('/'));
    });
    this.subscriptionManager.add(sub);
  }

  getImageSource() {
    return environment.basePath + '/api/file?path=' + encodeURIComponent('/' + this.currentPath);
  }
}
