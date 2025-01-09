import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EncoderUtil } from '../../../../../shared/utils/encoder-util';
import { Subscription } from 'rxjs';
import { ImageComponent } from '../../../../../shared/components/image/image.component';

@Component({
  selector: 'app-image-viewer',
  standalone: true,
  imports: [ImageComponent],
  templateUrl: './image-viewer.component.html',
  styleUrl: './image-viewer.component.scss',
})
export class ImageViewerComponent implements OnInit {
  // TODO: figure out how to make this not require a forward slash
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
    return '/' + this.currentPath;
  }
}
