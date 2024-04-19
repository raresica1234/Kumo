import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PathPointComponent } from './path-point.component';

describe('PathPointComponent', () => {
  let component: PathPointComponent;
  let fixture: ComponentFixture<PathPointComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PathPointComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PathPointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
