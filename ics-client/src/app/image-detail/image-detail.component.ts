import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {DataAccessService} from "../shared/data-access.service";
import {Image} from "../shared/image";
import {NavigationService} from "../shared/navigation.service";
import {ImageHandleService} from "../shared/image-handle.service";
import {Title} from "@angular/platform-browser";

@Component({
    templateUrl: "image-detail.component.html",
    styleUrls: [
        "image-detail.component.css",
        "image-detail-portrait.component.css",
        "image-detail-landscape.component.css"
    ]
})
export class ImageDetailComponent implements OnInit {
    componentTitle: string = "ics | Image Detail";

    image: Image | undefined;

    constructor(private route: ActivatedRoute, private dataAccessService: DataAccessService, private router: Router,
                private navigationService: NavigationService, private  imageHandleService: ImageHandleService,
                private titleService: Title) {
        this.navigationService.setNavigationButtonMenuWith('');
        this.titleService.setTitle(this.componentTitle);
    }


    ngOnInit(): void {

        let id: string = this.route.snapshot.paramMap.get('id') ?? '';

        this.image = this.imageHandleService.getImageFromIndexById(id);

        if (!this.image) {
            this.dataAccessService.getImageById(id)
                .subscribe({
                    next: (data) => {
                        this.imageHandleService.addImageByIdToIndex(data);
                        this.image = data;
                    },
                    error: () => this.router.navigate([''])
                });
        }
    }

    lookUpInGallery(tag: string) {
        const queryParams = { tag: tag};
        this.router.navigate(['/images'], { queryParams });
    }

    sortTagsFor(image: Image) {
        const tagEntries: [string, number][] = Object.entries(image.tags);
        tagEntries.sort((a, b) => b[1] - a[1]);
        return tagEntries;
    }

    protected readonly Object = Object;
}