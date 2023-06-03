import {Component} from '@angular/core';
import {NavigationService} from "./shared/navigation.service";

@Component({
    selector: 'ics-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'ics';

    constructor(public navigationService: NavigationService) {
    }
}
