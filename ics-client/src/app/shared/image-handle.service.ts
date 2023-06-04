import {Injectable} from "@angular/core";
import {Image} from "./image";
import * as lodash from "lodash";

@Injectable({
    providedIn: "root"
})
export class ImageHandleService {

    imagesById: { [key: string]: Image } = {};
    imagesByPage: Image[][] = [];
    batchSize = 12;
    galleryQueryParams: {} = {};

    setCurrentQueryParams(params: {}) {
        if (!lodash.isEqual(this.galleryQueryParams, params)) {
            this.galleryQueryParams = params;
            this.resetPageIndices();
        }
    }

    resetPageIndices() {
        this.imagesByPage = [];
    }
    getImagesPageFromIndex(pageNum: number) {
        return this.imagesByPage[pageNum];
    }
    addImagesPageToIndex(pageNum: number, dataArr: Image[]) {
        this.imagesByPage[pageNum] = lodash.take(dataArr, this.batchSize);
    }

    addImageByIdToIndex(image: Image) {
        this.imagesById[image.id] = image;
    }

    getImageFromIndexById(id: string) {
        return this.imagesById[id];
    }
}