import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from "@angular/common/http";
import {RouteReuseStrategy, RouterModule} from "@angular/router";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

import {AppComponent} from './app.component';
import {UrlSubmitComponent} from "./url-submit/url-submit.component";
import {ImageDetailComponent} from "./image-detail/image-detail.component";
import {NgOptimizedImage} from "@angular/common";
import {ImageGalleryComponent} from "./image-gallery/image-gallery.component";
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import {CustomRouteReuseStrategy} from "./shared/custom-route-reuse-strategy";
import {SearchBarComponent} from "./image-gallery/search-bar/search-bar.component";

@NgModule({
    declarations: [
        AppComponent,
        UrlSubmitComponent,
        ImageDetailComponent,
        ImageGalleryComponent,
        SearchBarComponent
    ],
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule.forRoot([
            {path: '', component: UrlSubmitComponent},
            {path: 'images/:id', component: ImageDetailComponent},
            {path: 'images', component: ImageGalleryComponent}
        ], {onSameUrlNavigation: 'reload'}),
        InfiniteScrollModule,
        NgOptimizedImage,
        FormsModule
    ],
    providers: [ { provide: RouteReuseStrategy, useClass: CustomRouteReuseStrategy }],
    bootstrap: [AppComponent]
})
export class AppModule {
}
