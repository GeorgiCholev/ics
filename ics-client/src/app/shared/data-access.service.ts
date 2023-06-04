import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Image} from "./image";
import {UrlSubmit} from "../url-submit/url-submit";
import {ImageHandleService} from "./image-handle.service";

@Injectable({
    providedIn: "root"
})
export class DataAccessService {

    private apiEndpoint = "http://localhost:8080/images";

    constructor(private httpClient: HttpClient, private imageHandleService: ImageHandleService) {
    }

    getImagesPageOf(params: {}, pageNum: number) {
        let httpParams = new HttpParams({fromObject: params});
        httpParams = httpParams.append('num', pageNum);
        httpParams = httpParams.append('size', this.imageHandleService.batchSize);

        return this.httpClient
            .get<Image[]>(this.apiEndpoint, {params: httpParams});
    }

    getImageById(id: string) {
        const headers = new HttpHeaders({"Content-Type": "application/json"});
        return this.httpClient
            .get<Image>(this.apiEndpoint + "/" + id, {headers: headers})
            .pipe(catchError(throwError));
    }

    categoriseImageUrl(url: UrlSubmit): Observable<Image> {
        return this.httpClient
            .post<Image>(this.apiEndpoint, url)
            .pipe(catchError(this.handlePostRequestError));
    }

    connectionError: string = "Error occurred please try again later.";
    tooManyRequests: string = "Too many requests! Please try again in a minute.";

    private handlePostRequestError(httpErrorResponse: HttpErrorResponse) {
        let errorMessage = "";
        if (httpErrorResponse.error instanceof ErrorEvent) {
            errorMessage = this.connectionError;
        } else {
            if (httpErrorResponse.status === 429) {
                errorMessage = this.tooManyRequests;
            } else if (httpErrorResponse.status === 400 || httpErrorResponse.status === 503) {
                errorMessage = httpErrorResponse.error.error;
            }
        }
        return throwError(() => errorMessage);
    }
}
