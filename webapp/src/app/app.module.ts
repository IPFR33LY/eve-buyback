import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {DataTableModule} from "angular2-datatable";
import { AppComponent } from './app.component';
import { DataFilterPipe }   from './data-filter.pipe';
import {NumberGrouping} from './numberGrouping.pipe';

@NgModule({
  declarations: [
    AppComponent, DataFilterPipe, NumberGrouping
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    DataTableModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
