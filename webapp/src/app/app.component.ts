import {Component, ChangeDetectorRef} from '@angular/core';
import {Http} from "@angular/http";
import {environment} from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  public data;
  public filterQuery = "";
  public sortBy = "buy";
  public sortOrder = "desc";
  public buyFactor = 0.97;

  constructor(private http: Http) {
  }

  ngOnInit(): void {
    this.http.get(environment.url + "/pending")
      .subscribe((data)=> {
        setTimeout(()=> {
          this.data = data.json();
          // this.calcTotalBuyback(data.json());
        }, 1000);
      });
  }

  public floor(num: number) {
    return Math.floor(num);
  }

  public getPriceBorderStyling(item:any) {
    let price = item['price'];
    if (price == 0) {
      return "green";
    }
    let buybackPrice = item['buy'] * this.buyFactor;
    let percentDelta = ((price / buybackPrice) - 1) * 100;
    if (percentDelta > 5 || percentDelta < -5) {
      return "red"
    } else {
      return ""
    }
  }

  public getTotalBuyback() {
    let total = 0;
    for(let i = 0; i < this.data.length; i++) {
      let item = this.data[i];
      total += item.buy;
    }
    return this.floor(total * this.buyFactor);
  }
}
