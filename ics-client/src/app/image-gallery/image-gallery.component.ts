import {Component, OnDestroy, OnInit} from "@angular/core";
import {DataAccessService} from "../shared/data-access.service";
import {NavigationService} from "../shared/navigation.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Image} from "../shared/image";
import {BehaviorSubject} from "rxjs";
import * as lodash from 'lodash';
import {ImageHandleService} from "../shared/image-handle.service";

@Component({
    templateUrl: "image-gallery.component.html",
    styleUrls: ["image-gallery.component.css"]
})
export class ImageGalleryComponent implements OnInit, OnDestroy {

    images: BehaviorSubject<Image[]> = new BehaviorSubject<Image[]>([]);
    finished = false;
    pageNum = 0;

    triggeredOnScroll: boolean = false;

    constructor(private dataAccessService: DataAccessService, private navigationService: NavigationService,
                private router: Router, private activatedRoute: ActivatedRoute,
                private imageHandleService: ImageHandleService) {
        this.navigationService.setNavigationButtonMenuWith('gallery-page');
    }

    ngOnInit(): void {
        this.activatedRoute.queryParams.subscribe(params => {
            this.imageHandleService.setCurrentQueryParams(params);
            this.getImages();
        });
    }

    onScroll() {
        if (this.triggeredOnScroll) {
            return;
        }
        this.triggeredOnScroll = true;
        setTimeout(() => this.triggeredOnScroll = false, 5_000);
        this.getImages();

    }

    onImgClick(imageId: string) {
        this.router.navigate(['/images', imageId])
            .catch(err => console.log(err));
    }

    getTopThreeTags(image: Image) {
        const tagEntries: [string, number][] = Object.entries(image.tags);
        tagEntries.sort((a, b) => b[1] - a[1]);
        return tagEntries.slice(0, 3);
    }


    ngOnDestroy(): void {
        this.images.unsubscribe();
    }

    private getImages() {

        if (this.finished) {
            this.pageNum = 0;
            return;
        }

        let newImages = this.imageHandleService.getImagesPageFromIndex(this.pageNum);
        if (newImages) {
            this.determineIfScrollingEnds(newImages);
            this.addImagesToBehaviourSubject(this.images, newImages);
            this.pageNum++;
        } else {

            this.dataAccessService
                .getImagesPageOf(this.imageHandleService.galleryQueryParams, this.pageNum)
                .subscribe({
                    next: (dataArr) => {
                        if (dataArr) {
                            this.addToIndices(dataArr, this.pageNum);
                            this.determineIfScrollingEnds(dataArr);
                            this.addImagesToBehaviourSubject(this.images, dataArr);
                            this.pageNum++;
                        } else {
                            this.finished = true;
                        }
                    }
                });
        }
    }

    private addToIndices(dataArr: Image[], pageNum: number) {
        dataArr.forEach(data => this.imageHandleService.addImageByIdToIndex(data));
        this.imageHandleService.addImagesPageToIndex(pageNum, dataArr);
    }

    private determineIfScrollingEnds(dataArr: Image[]) {
        if (dataArr.length < this.imageHandleService.batchSize) {
            this.finished = true;
        }
    }

    private addImagesToBehaviourSubject(behaviourSubject: BehaviorSubject<Image[]>, dataArr: Image[]) {
        behaviourSubject.next(lodash.concat(behaviourSubject.getValue(), dataArr));
    }

    protected readonly Object = Object;
}