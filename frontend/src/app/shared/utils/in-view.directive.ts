import { AfterViewInit, Directive, ElementRef, EventEmitter, OnDestroy, Output } from '@angular/core';

@Directive({
  selector: '[appInView]',
  standalone: true,
})
export class InViewDirective implements AfterViewInit, OnDestroy {
  // TODO: Figure out some buffered preloading, if necessary
  @Output() visibleChanged = new EventEmitter<boolean>();
  private observer!: IntersectionObserver;
  constructor(private element: ElementRef) {}

  ngAfterViewInit(): void {
    this.observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) this.emitVisible();
      },
      {
        root: document.querySelector('.nav-container'),
        threshold: 0.1,
      },
    );
    this.observer.observe(this.element.nativeElement);
  }

  private emitVisible() {
    this.visibleChanged.emit(true);
    this.observer.disconnect();
  }

  ngOnDestroy(): void {
    this.observer.disconnect();
  }
}
