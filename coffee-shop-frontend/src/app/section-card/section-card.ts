import { Component, input } from '@angular/core';

@Component({
  selector: 'app-section-card',
  imports: [],
  templateUrl: './section-card.html',
  styleUrl: './section-card.css',
})
export class SectionCard {
  title = input.required<string>();
}
