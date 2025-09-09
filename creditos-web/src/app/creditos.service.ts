import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

export interface Credito {
  numeroCredito: string;
  numeroNfse: string;
  dataConstituicao: string;
  valorIssqn: number;
  tipoCredito: string;
  simplesNacional: string;
  aliquota: number;
  valorFaturado: number;
  valorDeducao: number;
  baseCalculo: number;
}

@Injectable({ providedIn: 'root' })
export class CreditosService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  buscarPorNfse(numeroNfse: string): Observable<Credito[]> {
    return this.http.get<Credito[]>(`${this.base}/creditos/${numeroNfse}`);
  }

  buscarPorCredito(numeroCredito: string): Observable<Credito> {
    return this.http.get<Credito>(`${this.base}/creditos/credito/${numeroCredito}`);
  }
}
