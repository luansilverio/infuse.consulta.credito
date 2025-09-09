# Full Stack Créditos (Java 8 + Angular) — com testes (API + Web)

## Subir tudo
```bash
docker compose -f docker-compose-all.yml up --build
```
Acesse:
- Web: http://localhost:8081
- API: http://localhost:8080

## Testes API (Java 8)
```bash
cd creditos-api
mvn clean test
```

## Testes Web (Angular)
```bash
cd creditos-web
npm install
npm run test           # ou: npm run test:ci
```

## Endpoints
- `GET /api/creditos/{numeroNfse}`
- `GET /api/creditos/credito/{numeroCredito}`
