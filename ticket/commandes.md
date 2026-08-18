PostgreSQL

# Démarrer Postgres (brew services étant cassé , je passe par pg_ctl directement)
/opt/homebrew/opt/postgresql@17/bin/pg_ctl -D /opt/homebrew/var/postgresql@17 -l /opt/homebrew/var/postgresql@17.log start

# Arrêter Postgres
/opt/homebrew/opt/postgresql@17/bin/pg_ctl -D /opt/homebrew/var/postgresql@17 stop

# Lister toutes les bases
psql -U yevheniibondarenko -d postgres -l

# Se connecter à une base en mode interactif
psql -U yevheniibondarenko -d ticket_app

# Créer une base (à lancer depuis le shell, pas depuis psql interactif)
psql -U yevheniibondarenko -d postgres -c "CREATE DATABASE ticket_app;"

# Voir la structure d'une table (colonnes, types, contraintes)
psql -U yevheniibondarenko -d ticket_app -c "\d utilisateur"
<
# Supprimer une table et ses contraintes liées (utile si le schéma généré par Hibernate est corrompu)
psql -U yevheniibondarenko -d ticket_app -c "DROP TABLE utilisateur CASCADE;"

# Vérifier si un port est déjà utilisé par un ancien process qui traîne
lsof -i :8080