#!/bin/bash
set -e

# === Configuration à adapter avant de lancer ===
export PROJECT_ID="ton-project-id-ici"
export REGION="europe-west1"
export REPO_NAME="ticket-app"
CLUSTER_NAME="ticket-cluster"

echo "1) Authentification (ouvre le navigateur, connecte-toi avec ton compte Google)"
gcloud auth login

echo "2) Sélection du projet GCP"
gcloud config set project "$PROJECT_ID"

echo "3) Activation des APIs nécessaires (GKE + Artifact Registry)"
gcloud services enable container.googleapis.com artifactregistry.googleapis.com

echo "4) Création du dépôt Artifact Registry (stockage des images Docker)"
gcloud artifacts repositories create "$REPO_NAME" \
  --repository-format=docker \
  --location="$REGION" \
  --description="Images Docker du projet Ticket" || echo "(dépôt déjà existant, on continue)"

echo "5) Authentifier Docker auprès d'Artifact Registry"
gcloud auth configure-docker "${REGION}-docker.pkg.dev"

echo "6) Build + push de l'image backend"
docker build -t "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/backend:latest" ./ticket
docker push "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/backend:latest"

echo "7) Build + push de l'image frontend"
docker build -t "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/frontend:latest" ./ticket-frontend
docker push "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/frontend:latest"

echo "8) Création du cluster GKE Autopilot (Google gère les nœuds automatiquement)"
gcloud container clusters create-auto "$CLUSTER_NAME" --region="$REGION"

echo "9) Récupération des identifiants kubectl pour ce cluster"
gcloud container clusters get-credentials "$CLUSTER_NAME" --region="$REGION"

echo "10) Génération des manifestes finaux à partir des templates (remplace \${PROJECT_ID}, etc.)"
mkdir -p k8s-gcp/generated
envsubst < k8s-gcp/backend.yaml.template > k8s-gcp/generated/backend.yaml
envsubst < k8s-gcp/frontend.yaml.template > k8s-gcp/generated/frontend.yaml
cp k8s-gcp/postgres.yaml k8s-gcp/generated/postgres.yaml
cp k8s-gcp/kafka.yaml k8s-gcp/generated/kafka.yaml

echo "11) Application des manifestes sur le cluster"
kubectl apply -f k8s-gcp/generated/

echo "12) Attente de l'IP externe du frontend (peut prendre 1-2 minutes)"
kubectl get service frontend --watch
