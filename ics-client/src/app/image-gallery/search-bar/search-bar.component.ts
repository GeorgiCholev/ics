import {Component} from "@angular/core";
import {Router} from "@angular/router";

@Component({
    selector: "search-bar",
    templateUrl: "search-bar.component.html",
    styleUrls: ["search-bar.component.css"]
})
export class SearchBarComponent {

    constructor(private router: Router) {
    }

    selectedTags : Set<string> = new Set();

    private _inputTag : string = '';


    get inputTag(): string {
        return this._inputTag;
    }

    set inputTag(value: string) {
        this._inputTag = value;
    }

    addTagFilter() {
        if (!this.selectedTags.has(this.inputTag)) {
            this.selectedTags.add(this.inputTag);
        }
        this.inputTag = "";
    }

    removeTag(tag: string) {
        this.selectedTags.delete(tag);
    }

    applyTagsFilter() {
        const queryParams = {
            tag: Array.from(this.selectedTags)
        };
        this.router.navigate(['/images'], { queryParams });
    }

    clearFilters() {
        this.router.navigate(['/images']);
    }
}