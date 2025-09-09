# Build Angular app
FROM node:18-alpine AS build
WORKDIR /app
ENV NODE_OPTIONS=--max_old_space_size=4096
COPY package.json package-lock.json* ./
RUN npm install --no-audit --no-fund --legacy-peer-deps
RUN npx -y @angular/cli@16.2.0 version
COPY . .
RUN npx ng version
RUN npx ng build --configuration production

# Serve with Nginx and proxy /api -> api:8080
FROM nginx:alpine
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/creditos-web/ /usr/share/nginx/html
EXPOSE 80
CMD ["nginx","-g","daemon off;"]
