import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  template: '',
})
export abstract class BaseComponent implements OnInit {
  title: string | undefined;
  protected constructor(protected route: ActivatedRoute) {}

  ngOnInit() {
    this.title = this.route.snapshot.data['title'];
  }
}
