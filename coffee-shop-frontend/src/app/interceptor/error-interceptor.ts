import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error) => {
      if (error.status === 409) {
        alert('Ova stavka ne može biti obrisana jer je povezana sa drugim podacima.');
      } else if (error.status === 403) {
        alert('Nemate dozvolu za ovu akciju.');
      } else if (error.status === 401) {
        alert('Sesija je istekla, prijavite se ponovo.');
      }
      return throwError(() => error);
    })
  );
};
