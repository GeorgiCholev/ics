import {Component, OnDestroy, OnInit} from "@angular/core";
import {DataAccessService} from "../shared/data-access.service";
import {NavigationService} from "../shared/navigation.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Image} from "../shared/image";
import {BehaviorSubject} from "rxjs";
import * as lodash from 'lodash';
import {ImageHandleService} from "../shared/image-handle.service";
import {Title} from "@angular/platform-browser";

@Component({
    templateUrl: "image-gallery.component.html",
    styleUrls: ["image-gallery.component.css"]
})
export class ImageGalleryComponent implements OnInit, OnDestroy {
    componentTitle: string = "ics | Gallery";

    images: BehaviorSubject<Image[]> = new BehaviorSubject<Image[]>([]);
    finished = false;
    pageNum = 0;

    currentlyFetching: boolean = false;

    constructor(private dataAccessService: DataAccessService, private navigationService: NavigationService,
                private router: Router, private activatedRoute: ActivatedRoute,
                private imageHandleService: ImageHandleService, private titleService: Title) {
        this.navigationService.setNavigationButtonMenuWith('gallery-page');
        this.titleService.setTitle(this.componentTitle);
    }

    ngOnInit() {
        let queryParams = this.activatedRoute.snapshot.queryParams;
        this.imageHandleService.setCurrentQueryParams(queryParams);
        this.getImages();
    }

    onScroll() {
        let timeout = 0;
        if (this.currentlyFetching) {
            timeout = 2_000;
        }

        setTimeout(() => {
            this.getImages();
        }, timeout);
    }

    getImages() {
        if (this.finished) {
            return;
        }

        this.currentlyFetching = true;

        let currentPage = this.pageNum;
        this.pageNum++;

        let newImages = this.imageHandleService.getImagesPageFromIndex(currentPage);
        if (newImages) {
            this.addImagesToBehaviourSubject(this.images, newImages);
            if (!this.bottomIsReached(newImages)) {
                this.getNextPageInAdvance();
                return;
            }
            this.currentlyFetching = false;
            this.finished = true;
        } else {
            this.dataAccessService
                .getImagesPageOf(this.imageHandleService.galleryQueryParams, currentPage)
                .subscribe({
                    next: (dataArr) => {
                        if (dataArr) {
                            this.addImagesToBehaviourSubject(this.images, dataArr);
                            this.addToIndices(dataArr, currentPage);

                            if (!this.bottomIsReached(dataArr)) {
                                this.getNextPageInAdvance();
                                return;
                            }
                        }
                        this.currentlyFetching = false;
                        this.finished = true;
                    }
                });
        }
    }

    getNextPageInAdvance() {
        let newImages = this.imageHandleService.getImagesPageFromIndex(this.pageNum);

        if (this.finished || newImages) {
            this.currentlyFetching = false;
            return;
        }

        this.dataAccessService
            .getImagesPageOf(this.imageHandleService.galleryQueryParams, this.pageNum)
            .subscribe({
                next: (dataArr) => {
                    if (dataArr) {
                        this.addToIndices(dataArr, this.pageNum);
                        this.currentlyFetching = false;
                    }
                }
            });
    }

    private addToIndices(dataArr: Image[], pageNum: number) {
        dataArr.forEach(data => this.imageHandleService.addImageByIdToIndex(data));
        this.imageHandleService.addImagesPageToIndex(pageNum, dataArr);
    }

    private bottomIsReached(dataArr: Image[]): boolean {
        return dataArr.length < this.imageHandleService.batchSize;
    }


    private addImagesToBehaviourSubject(behaviourSubject: BehaviorSubject<Image[]>, dataArr: Image[]) {
        behaviourSubject.next(lodash.concat(behaviourSubject.getValue(), dataArr));
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

    protected readonly Object = Object;
    
}