import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CreditosService, Credito } from './creditos.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
  <div class="container">
    <div class="card">
      <h1>Consulta de Créditos</h1>
      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <div class="field">
          <label class="label">Número da NFS-e</label>
          <input class="input" formControlName="numeroNfse" placeholder="ex: 7891011">
          <div class="helper">Preencha <b>apenas</b> um campo por vez.</div>

          <label class="label">Número do Crédito</label>
          <input class="input" formControlName="numeroCredito" placeholder="ex: 123456">
        </div>
        <div class="actions">
          <button class="btn btn-primary" type="submit">Buscar</button>
          <button class="btn" type="button" (click)="clear()">Limpar</button>
        </div>
      </form>

      <div *ngIf="error()" class="helper" style="color:#fca5a5">{{error()}}</div>

      <div class="table-wrap" *ngIf="resultList().length">
        <table>
          <thead>
            <tr>
              <th>Nº Crédito</th>
              <th>Nº NFS-e</th>
              <th>Data</th>
              <th>ISSQN</th>
              <th>Tipo</th>
              <th>Simples</th>
              <th>Alíquota</th>
              <th>Faturado</th>
              <th>Dedução</th>
              <th>Base Cálculo</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let c of resultList()">
              <td>{{c.numeroCredito}}</td>
              <td>{{c.numeroNfse}}</td>
              <td>{{c.dataConstituicao}}</td>
              <td>{{c.valorIssqn | number:'1.2-2'}}</td>
              <td>{{c.tipoCredito}}</td>
              <td><span class="badge" [class.ok]="c.simplesNacional==='Sim'" [class.no]="c.simplesNacional!=='Sim'">{{c.simplesNacional}}</span></td>
              <td>{{c.aliquota}}</td>
              <td>{{c.valorFaturado | number:'1.2-2'}}</td>
              <td>{{c.valorDeducao | number:'1.2-2'}}</td>
              <td>{{c.baseCalculo | number:'1.2-2'}}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div *ngIf="!resultList().length && submitted()" class="helper">Nenhum resultado encontrado.</div>

      <footer>Frontend Angular • Consome <code>/api/creditos</code></footer>
    </div>
  </div>
  `
})
export class AppComponent {
  private fb = inject(FormBuilder);

  form = this.fb.group({ numeroNfse: [''], numeroCredito: [''] });
  resultList = signal<Credito[]>([]);
  error = signal<string>('');
  submitted = signal<boolean>(false);

  constructor(private api: CreditosService) {}

  onSubmit() {
    this.error.set('');
    this.submitted.set(true);
    const nfse = (this.form.value.numeroNfse || '').trim();
    const cred = (this.form.value.numeroCredito || '').trim();

    if (nfse && cred) {
      this.error.set('Preencha apenas um dos campos.');
      this.resultList.set([]);
      return;
    }
    if (!nfse && !cred) {
      this.error.set('Informe o número da NFS-e ou do Crédito.');
      this.resultList.set([]);
      return;
    }

    if (nfse) {
      this.api.buscarPorNfse(nfse).subscribe({
        next: res => this.resultList.set(res),
        error: () => { this.error.set('Erro ao consultar por NFS-e.'); this.resultList.set([]); }
      });
    } else {
      this.api.buscarPorCredito(cred!).subscribe({
        next: res => this.resultList.set(res ? [res] : []),
        error: () => { this.error.set('Crédito não encontrado.'); this.resultList.set([]); }
      });
    }
  }

  clear() {
    this.form.reset();
    this.resultList.set([]);
    this.submitted.set(false);
    this.error.set('');
  }
}
