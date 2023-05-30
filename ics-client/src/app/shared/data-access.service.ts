import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {UrlSubmit} from "../url-submit/url-submit";
import {catchError, Observable, tap, throwError} from "rxjs";
import {Image} from "./image";

@Injectable({
  providedIn: "root"
})
export class DataAccessService {

  private apiEndpoint = "http://localhost:8080/images";

  constructor(private httpClient: HttpClient) {
  }

  categoriseImageUrl(url: string): Observable<Image> {
    const headers = new HttpHeaders({"Content-Type": "application/json"});
    return this.httpClient
      .post<Image>(this.apiEndpoint, url, {headers: headers})
      .pipe(
        tap(data => console.log(JSON.stringify(data))),
        catchError(this.handleError)
      );
  }

  connectionError:string = "Error occurred please try again later.";
  tooManyRequests:string = "Too many requests! Please try again in a minute.";

  private handleError(httpErrorResponse: HttpErrorResponse) {
    let errorMessage = "";
    if (httpErrorResponse.error instanceof ErrorEvent) {
      errorMessage = this.connectionError;
    } else {
      if (httpErrorResponse.status === 429) {
        errorMessage = this.tooManyRequests;
      } else {
        errorMessage = httpErrorResponse.error.error;
      }
    }

    console.error(errorMessage);
    return throwError(() => errorMessage);
  }
}
