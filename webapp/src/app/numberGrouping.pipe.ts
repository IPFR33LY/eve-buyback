import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'numberGroups'})
export class NumberGrouping implements PipeTransform {
  transform(value:number):string {
    let result = value + "";
    let length = result.length;
    let i = length;
    i -= 3;
    while (0 < i) {
      result = result.slice(0, i) + "," + result.slice(i, length);
      length++;
      i -= 3;
    }
    return result;
  }
}
