import { AfterViewInit, Component, ElementRef, HostListener, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FileControllerService } from '../../api-models';
import { Subscription } from 'rxjs';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-image',
  standalone: true,
  imports: [MatProgressSpinner],
  templateUrl: './image.component.html',
  styleUrl: './image.component.scss',
})
export class ImageComponent implements OnDestroy, AfterViewInit {
  // TODO: center loading vertically as well
  // TODO: fix style for mobile and tablet
  @Input({ required: true }) imageUrl!: string;

  @ViewChild('image', { static: false }) private image!: ElementRef<HTMLDivElement>;

  isLoaded: boolean = false;

  imageSource: any;

  private subscriptionManager: Subscription = new Subscription();

  constructor(private fileService: FileControllerService) {}

  ngAfterViewInit(): void {
    this.isScrolledIntoView();
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribe();
  }

  // TODO: fix this not working
  @HostListener('document:mousewheel', [])
  isScrolledIntoView() {
    if (this.image) {
      const rect = this.image.nativeElement.getBoundingClientRect();
      if (rect.top >= 0 && rect.bottom <= window.innerHeight) this.loadImage();
    }
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
}
