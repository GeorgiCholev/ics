import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DataAccessService} from "../shared/data-access.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NavigationService} from "../shared/navigation.service";
import {ImageHandleService} from "../shared/image-handle.service";

@Component({
    templateUrl: 'url-submit.component.html',
    styleUrls: ['url-submit.component.css']
})
export class UrlSubmitComponent implements OnInit {

    urlPattern: string =
        "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

    urlForm!: FormGroup;

    errorMessage: string = "";
    hasError: boolean = false;
    isBeingProcessed: boolean = false;

    constructor(private formBuilder: FormBuilder, private dataAccessService: DataAccessService, private router: Router,
                private activatedRoute: ActivatedRoute, private navigationService: NavigationService,
                private imageHandleService: ImageHandleService) {
        this.navigationService.setNavigationButtonMenuWith('submit-page');
    }


    ngOnInit(): void {
        this.urlForm = this.formBuilder.group({
            url: ["", [Validators.required, Validators.pattern(this.urlPattern)]]
        });
    }

    onSubmit() {
        this.isBeingProcessed = true;
        let url = this.urlForm.value;

        this.dataAccessService
            .categoriseImageUrl(url)
            .subscribe({
                next: data => {
                    this.imageHandleService.addImageByIdToIndex(data);
                    this.isBeingProcessed = false;
                    this.router.navigate(['/images', data.id])
                        .catch(err => console.log(err));
                },
                error: err => this.supplyErrorToTemplate(err)
            });

    }

    private supplyErrorToTemplate(err: string) {
        this.isBeingProcessed = false;
        this.errorMessage = err;
        this.hasError = true;

        setTimeout(() => {
            this.hasError = false;
        }, 5_000);
    }

}
