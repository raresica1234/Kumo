import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FileViewerSettingsComponent } from './file-viewer-settings.component';

describe('FileViewerSettingsComponent', () => {
  let component: FileViewerSettingsComponent;
  let fixture: ComponentFixture<FileViewerSettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FileViewerSettingsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FileViewerSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
