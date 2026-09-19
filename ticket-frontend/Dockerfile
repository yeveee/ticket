# Étape 1 : build — compile l'app Angular en fichiers statiques
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npx ng build

# Étape 2 : runtime — sert les fichiers statiques avec nginx
FROM nginx:alpine
COPY --from=build /app/dist/ticket-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
