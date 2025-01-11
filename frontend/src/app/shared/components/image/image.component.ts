import { Component, Input, OnDestroy } from '@angular/core';
import { FileControllerService } from '../../api-models';
import { Subscription } from 'rxjs';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { InViewDirective } from '../../utils/in-view.directive';

@Component({
  selector: 'app-image',
  standalone: true,
  imports: [MatProgressSpinner, InViewDirective],
  templateUrl: './image.component.html',
  styleUrl: './image.component.scss',
})
export class ImageComponent implements OnDestroy {
  @Input({ required: true }) imageUrl!: string;

  isLoaded: boolean = false;

  imageSource: any;

  private subscriptionManager: Subscription = new Subscription();

  constructor(private fileService: FileControllerService) {}

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  private loadImage() {
    if (this.isLoaded) return;
    const sub = this.fileService.getFile(encodeURIComponent(this.imageUrl)).subscribe({
      next: (value) => {
        const reader = new FileReader();
        reader.readAsDataURL(value);
        reader.onloadend = this.onImageLoadEnd.bind(this, reader);
      },
      error: (_) => {
        this.isLoaded = true;
        // Display dummy image
      },
    });
    this.subscriptionManager.add(sub);
  }

  private onImageLoadEnd(reader: FileReader) {
    this.imageSource = reader.result;
    this.isLoaded = true;
  }

  onImageVisibilityChanged(visible: boolean) {
    if (visible) this.loadImage();
  }
}
