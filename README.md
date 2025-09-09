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
# ou sem Java local:
docker run --rm -v "$PWD":/app -w /app maven:3.9-eclipse-temurin-8 mvn -q -e -B clean test
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
