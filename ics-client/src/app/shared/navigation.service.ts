import {Injectable} from "@angular/core";

@Injectable({
    providedIn: "root"
})
export class NavigationService {

    private _activeButton: string = '';

    setNavigationButtonMenuWith(activeBtnName: string) {
        this.activeButton = activeBtnName;
    }

    get activeButton(): string {
        return this._activeButton;
    }

    set activeButton(value: string) {
        this._activeButton = value;
    }

}