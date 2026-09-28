# Ticket — plateforme de gestion de tickets

Application full-stack de suivi de tickets (type mini-Jira) : projets, tickets, commentaires, autorisation par rôles.

Backend **Spring Boot 4.1 / Java 21** en architecture hexagonale, frontend **Angular 21**, messagerie **Kafka**, déploiement **Docker Compose** et **Kubernetes**.

> **Contexte** — projet personnel, construit pour mettre en pratique des choix d'architecture que je voulais maîtriser plutôt que seulement connaître de nom : ports & adapters, TDD, événementiel, orchestration de conteneurs. Toutes les décisions techniques qui suivent sont les miennes et sont justifiées plus bas, y compris quand elles sont surdimensionnées pour le besoin réel.

---

## Stack

| Couche | Technologies |
|---|---|
| **Backend** | Java 21, Spring Boot 4.1, Spring Data JPA / Hibernate, Spring Security, JWT (jjwt), Lombok, Micrometer Tracing |
| **Base de données** | PostgreSQL 17 |
| **Messagerie** | Apache Kafka 3.7 (mode KRaft, sans Zookeeper) |
| **Frontend** | Angular 21 (composants standalone), RxJS, nginx |
| **Tests** | JUnit 5, Mockito (backend) · Vitest (frontend) |
| **Infrastructure** | Docker (builds multi-stage), Docker Compose, Kubernetes |
| **CI** | GitHub Actions |

---

## Architecture

Le module Ticket suit une **architecture hexagonale (ports & adapters)**. La logique métier ne dépend d'aucune technologie : elle déclare des interfaces (*ports*) qu'elle possède, et l'infrastructure vient s'y brancher via des *adaptateurs*.

```
                    ┌──────────────────────────────────┐
                    │            DOMAINE               │
   TicketController │                                  │
   ───────────────► │ [TicketUseCase]   ◄── TicketService
       (port in)    │                                  │
                    │       │                  │       │
                    └───────┼──────────────────┼───────┘
                            ▼                  ▼
                 [TicketRepositoryPort]  [TicketEventPublisherPort]
                            ▲                  ▲
                            │                  │            (ports out)
                 ───────────┴──────────────────┴───────────
                            │                  │
                TicketPersistenceAdapter   KafkaTicketEventPublisher
                            │                  │
                            ▼                  ▼
                       PostgreSQL            Kafka
```

Les flèches montantes se lisent « implémente ». Le point clé : les ports appartiennent au domaine, donc **c'est l'infrastructure qui dépend du métier**, et pas l'inverse (inversion de dépendance).

**Bénéfice mesurable :** `TicketService` ne contient aucun import `org.apache.kafka` ni aucun import Spring Data. L'ajout de Kafka n'a demandé qu'un adaptateur supplémentaire derrière le port de publication, sans toucher une ligne de logique métier.

**Coût assumé :** plus de classes et plus d'indirection. Sur un CRUD comme celui-ci, c'est objectivement surdimensionné — le pattern se justifie quand le métier a de la substance ou quand l'infrastructure est amenée à changer.

### Flux événementiel

À la création d'un ticket, un `TicketCreatedEvent` est publié sur Kafka et consommé de façon asynchrone. L'objectif est le découplage : la création d'un ticket n'attend pas les traitements annexes (notification, indexation, audit), et si un consommateur tombe, l'événement reste dans le topic.

Le producteur et le consommateur sont tous deux implémentés comme des adaptateurs hexagonaux.

---

## Fonctionnalités

- **Authentification JWT** — inscription, connexion, mots de passe hachés avec BCrypt, API stateless
- **Autorisation par rôles** — `ADMIN`, `CHEF_PROJET`, `DEVELOPPEUR`, appliquée via `@PreAuthorize`
- **CRUD complet** — projets, tickets, commentaires, utilisateurs
- **Recherche de tickets par mot-clé** — développée en TDD (cycle red-green-refactor)
- **Statuts de ticket** — `OUVERT`, `EN_COURS`, `RESOLU`
- **Frontend Angular** — tableau de bord, liste et formulaire de tickets, intercepteur HTTP pour le JWT, garde de route

---

## API

Toutes les routes sont authentifiées sauf `/api/auth/**`.

| Méthode | Route | Rôle requis |
|---|---|---|
| `POST` | `/api/auth/register` | — |
| `POST` | `/api/auth/login` | — |
| `GET` | `/api/tickets` | authentifié |
| `GET` | `/api/tickets/search?q=` | authentifié |
| `GET` `POST` `PUT` `DELETE` | `/api/tickets`, `/api/tickets/{id}` | authentifié |
| `GET` | `/api/projets`, `/api/projets/{id}` | authentifié |
| `POST` `DELETE` | `/api/projets`, `/api/projets/{id}` | `ADMIN` ou `CHEF_PROJET` |
| `GET` `POST` `DELETE` | `/api/commentaires`, `/api/commentaires/{id}` | authentifié |
| `GET` `POST` `DELETE` | `/api/utilisateurs`, `/api/utilisateurs/{id}` | authentifié |

Les entités JPA ne sont jamais exposées : toutes les réponses passent par des DTO.

---

## Démarrage

### Avec Docker Compose (recommandé)

```bash
docker compose up --build
```

- Frontend : http://localhost:4200
- Backend : http://localhost:8080
- PostgreSQL : `localhost:5432` · Kafka : `localhost:9092`

Le frontend sert l'application derrière nginx, qui fait office de **reverse proxy** vers le backend sur `/api/`. Les appels HTTP du frontend utilisent donc des chemins relatifs, ce qui rend l'application indépendante de l'endroit où elle est déployée.

### En développement

**Backend** — nécessite PostgreSQL et Kafka démarrés :

```bash
cd ticket
mvn spring-boot:run
```

**Frontend** :

```bash
cd ticket-frontend
npm install
npm start
```

`proxy.conf.json` reproduit le comportement du reverse proxy nginx en développement, pour que les mêmes chemins relatifs fonctionnent avec `ng serve`.

---

## Tests

```bash
cd ticket && mvn test               # backend — JUnit 5 + Mockito
cd ticket-frontend && npx ng test   # frontend — Vitest
```

Les tests du domaine ne chargent aucun contexte Spring : les ports étant des interfaces, ils se mockent directement avec Mockito.

La CI GitHub Actions (`.github/workflows/ci.yml`) exécute à chaque push et pull request sur `main` : les tests backend contre un service PostgreSQL réel, le build du jar, puis les tests et le build du frontend.

---

## Kubernetes

Manifestes pour un cluster local, **déployés et validés sur `kind`** :

```bash
kubectl apply -f k8s/
```

Quatre composants — PostgreSQL, Kafka, backend, frontend — chacun avec son `Deployment` et son `Service`. La configuration est externalisée dans un `ConfigMap` pour les valeurs non sensibles et un `Secret` pour les identifiants de base de données.

> À noter : un `Secret` Kubernetes est encodé en base64, pas chiffré. En production, un gestionnaire de secrets dédié serait nécessaire.

---

## Déploiement GCP

Le dossier `k8s-gcp/` et le script `deploy-gcp.sh` contiennent un pipeline de déploiement complet vers Google Cloud : Artifact Registry pour les images, GKE Autopilot pour l'orchestration, manifestes templatisés générés par `envsubst`.

> **Ce pipeline a été conçu et validé sur le papier, mais n'a jamais été exécuté**, afin d'éviter les coûts d'infrastructure cloud. Je connais la mécanique, je n'ai pas d'expérience d'exploitation réelle sur GCP.

---

## Limites connues

Points identifiés, **en cours d'implémentation** :

1. **L'isolation hexagonale est partielle.** `TicketService` accède encore à `UtilisateurRepository` et `ProjetRepository` directement, sans passer par un port. Le domaine dépend donc toujours de Spring Data pour ces deux agrégats.
2. **La gestion d'erreurs centralisée ne couvre qu'un cas.** `GlobalExceptionHandler` traite `InvalidCredentialsException` ; les autres erreurs remontent en `RuntimeException` et produisent un `500` là où un `404` ou un `400` serait attendu.
3. **La validation des entrées n'est pas branchée.** `spring-boot-starter-validation` est présent dans le `pom.xml` mais aucune contrainte (`@Valid`, `@NotBlank`) n'est encore appliquée sur les DTO.

Ensuite, par ordre de priorité : pagination sur les listes, codes HTTP plus rigoureux (`201 Created` + `Location`, `204 No Content`), tests d'intégration avec Testcontainers, et refresh tokens — aujourd'hui un access token JWT ne peut pas être révoqué avant son expiration.

---

## Structure du dépôt

```
ticket/              backend Spring Boot
  └─ src/main/java/com/app/ticket/
       ├─ domain/event/     événements métier
       ├─ port/in/          ports pilotants (use cases)
       ├─ port/out/         ports pilotés (persistance, messagerie)
       ├─ adapter/          adaptateurs (JPA, Kafka)
       ├─ service/          logique métier
       ├─ controller/       exposition REST
       ├─ dto/ entity/      contrat d'API / modèle persistant
       └─ config/           sécurité, JWT, Kafka
ticket-frontend/     frontend Angular + nginx
k8s/                 manifestes Kubernetes (cluster local kind)
k8s-gcp/             manifestes templatisés pour GKE (non déployés)
deploy-gcp.sh        script de déploiement GCP (non exécuté)
docker-compose.yml   orchestration locale complète
```
