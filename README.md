# 💰 MoneyWise

🔗 Repository : https://github.com/KevinAkkouche/moneywise

Application web de gestion financière, déployée via une infrastructure DevOps complète (Ansible, Docker, GitHub Actions).

---

## 📋 Description du projet

MoneyWise est une application full-stack composée de :
- Un **backend** Java 17 / Spring Boot, buildé avec Maven
- Un **frontend** Angular, servi via un reverse proxy Nginx
- Une **base de données** MariaDB avec volume persistant

L'ensemble est conteneurisé avec Docker et orchestré via Docker Compose. Le déploiement est automatisé grâce à Ansible et une pipeline CI/CD GitHub Actions.

---

## 🏗️ Architecture

```
Internet / GitHub
       │
   Routeur → Switch
       │
 VMware Workstation (Hyperviseur Type 2)
       │
  ┌────┴────────────────────────┐
  │                             │
Ubuntu Server — Contrôleur      Ubuntu Server — Serveur
192.168.188.134                 192.168.188.135
  • OpenSSH                       • OpenSSH
  • Ansible                       • Docker & Docker Compose
  • Clé privée Contrôleur         • Runner GitHub Actions
  │                               • Clé publique Contrôleur
  │                               • Clé privée Serveur
  │                                       │
  └──────── SSH / Ansible ────────────────┘
                                          │
                              ┌───────────▼────────┐
                              │   MoneyWise App     │
                              │   ─────────────     │
                              │   Backend :8080     │
                              │   Frontend :80      │
                              │   DB MariaDB        │
                              └─────────────────────┘
```

**Réseau :** VMnet8 en NAT — sous-réseau `192.168.188.0/24`

### Structure du projet

```
moneywise/
├── backend/                        # Application Spring Boot
│   ├── src/
│   ├── target/
│   ├── Dockerfile                  # Information Conteneurisation backend
│   └── pom.xml
├── frontend/                       # Application Angular
│   ├── src/
│   ├── Dockerfile                  # Information Conteneurisation frontend
│   ├── nginx.conf                  # Configuration du reverse proxy
│   ├── angular.json
│   ├── package.json
│   ├── package-lock.json
│   ├── tsconfig.app.json
│   └── tsconfig.json
├── db/                             # Script d'initialisation de la BDD
│   └── init.sql
├── .github/
│   └── workflows/
│       └── ci-cd.yml               # Pipeline CI/CD GitHub Actions
├── docker-compose.yml              # Orchestration des conteneurs
├── .env                            # Variables d'environnement (base de données)
└── .gitignore                      # Dossiers à ignorer dans l'architecture

ubuntu~                             # Serveur Contrôleur
└── ansible-moneywise/
    ├── inventory/
    │   └── hosts.ini               # Inventaire Ansible
    └── playbooks/
        ├── install_docker.yml      # Playbook d'installation Docker & Docker Compose
        └── deploy_moneywise.yml    # Playbook de déploiement et conteneurisation projet

ubuntu~                             # Serveur Server
└── actions-runner/                 # Runner GitHub
```

---

## ⚙️ Commandes principales

### Ansible

```bash
# Tester la connectivité avec le serveur
ansible -i inventory/hosts.ini moneywise -m ping

# Installer Docker sur le serveur
ansible-playbook -i inventory/hosts.ini playbooks/install_docker.yml --ask-become-pass

# Déployer l'application
ansible-playbook -i inventory/hosts.ini playbooks/deploy_moneywise.yml --ask-become-pass

# Vérifier l'état des conteneurs
ansible -i inventory/hosts.ini moneywise -b -m shell -a "cd /opt/moneywise && docker compose ps" --ask-become-pass
```

### Docker Compose

```bash
# Lancer tous les conteneurs (build inclus)
docker compose up -d --build

# Arrêter les conteneurs
docker compose down

# Voir l'état des conteneurs
docker compose ps

# Voir les logs
docker compose logs -f
```

### Git

```bash
# Envoyer les modifications sur le dépôt
git add .
git commit -m "message"
git push -u origin main

# Créer une branche de test
git checkout -b ma-branche
```

---

## 🚀 Procédure de déploiement

### Prérequis

- VMware Workstation avec deux VM Ubuntu Server 24.04 sur VMnet8 (NAT)
- Ansible installé sur la machine **Contrôleur**
- Accès SSH sans mot de passe depuis le Contrôleur vers le Serveur
- Compte GitHub avec accès au repository

### 1. Configurer le SSH (sur le Contrôleur)

```bash
ssh-keygen -t ed25519 -C "ansible-controller"
ssh-copy-id ubuntu@192.168.188.135
```

### 2. Installer Ansible (sur le Contrôleur)

```bash
sudo apt update && sudo apt install ansible -y
```

### 3. Installer Docker sur le Serveur via Ansible

```bash
cd ~/ansible-moneywise
ansible-playbook -i inventory/hosts.ini playbooks/install_docker.yml --ask-become-pass
```

### 4. Configurer le Runner GitHub Actions (sur le Serveur)

Depuis GitHub → Settings → Actions → Runners → New self-hosted runner → Linux, exécuter les commandes fournies, puis :

```bash
sudo ./svc.sh install
sudo ./svc.sh start
```

### 5. Ajouter la clé SSH du Serveur sur GitHub

```bash
ssh-keygen -t ed25519 -C "moneywise-server"
cat ~/.ssh/id_ed25519.pub
```

Copier la clé publique dans GitHub → Settings → Deploy keys → Add deploy key.

### 6. Déployer l'application via Ansible

```bash
ansible-playbook -i inventory/hosts.ini playbooks/deploy_moneywise.yml --ask-become-pass
```

### 7. Vérifier le déploiement

L'application est accessible à l'adresse :

```
http://192.168.188.135/
```

---

## 🔄 Pipeline CI/CD

La pipeline GitHub Actions (`ci-cd.yml`) se déclenche automatiquement lors de :
- Un **push** sur la branche `main`
- Une **pull request** vers `main`
- Un déclenchement **manuel** (workflow_dispatch)

Elle effectue dans l'ordre :
1. Build & tests du backend (Maven via Docker)
2. Build du frontend (Node/Angular via Docker)
3. Build des images Docker Compose
4. Redéploiement de l'application (`docker compose down` + `docker compose up -d`)
5. Vérification des conteneurs en cours d'exécution

---

## 🛠️ Stack technique

| Composant | Technologie |
|---|---|
| Backend | Java 17, Spring Boot, Maven |
| Frontend | Angular, Nginx |
| Base de données | MariaDB 11 |
| Conteneurisation | Docker, Docker Compose |
| Automatisation | Ansible |
| CI/CD | GitHub Actions (self-hosted runner) |
| Virtualisation | VMware Workstation |
| OS | Ubuntu Server 24.04.4 LTS |
