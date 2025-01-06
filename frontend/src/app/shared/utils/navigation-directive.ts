import { Directive, HostListener, Input } from '@angular/core';
import { Router } from '@angular/router';

@Directive({
  selector: '[navHandler]',
  standalone: true,
})
export class NavigationHandlerDirective {
  @Input() url?: string;

  constructor(private router: Router) {}

  @HostListener('click')
  onClick(): void {
    this.navigate(false);
  }

  @HostListener('auxclick')
  onAuxClick(): void {
    this.navigate(true);
  }

  private navigate(newPage: boolean): void {
    if (!this.url) return;

    if (newPage) {
      const url = this.router.serializeUrl(this.router.createUrlTree([this.url]));

      window.open(url, '_blank');
      return;
    }
    this.router.navigate([this.url], {});
  }
}
