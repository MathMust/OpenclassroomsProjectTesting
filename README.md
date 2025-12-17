# Yoga App – Full Stack

Ce projet est une application **Full Stack** composée :

* d’un **front-end Angular**
* d’un **back-end Java (Spring Boot / Maven)**
* d’une **base de données MySQL**

Ce README explique comment installer, lancer et tester l’ensemble du projet.

---

## Prérequis

Assurez-vous d’avoir installé :

* Git
* Node.js (version recommandée : 16+)
* npm
* Angular CLI 14.1.0
* Java JDK 11
* Maven
* MySQL

---

## Récupération du projet

```bash
git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing.git
cd P5-Full-Stack-testing
```

---

## Installation de la base de données

1. Lancer MySQL
2. Créer la base de données

```sql
CREATE DATABASE yoga;
```

3. Exécuter le script SQL fourni :

```text
ressources/sql/script.sql
```

Ce script permet de créer le schéma et les données nécessaires.

### Compte administrateur par défaut

* **login** : [yoga@studio.com](mailto:yoga@studio.com)
* **password** : test!1234

---

## Installation et lancement du back-end

1. Se placer dans le dossier back-end

```bash
cd back
```

2. Lancer les tests et démarrer l’application

```bash
mvn clean test
```

Cette commande :

* compile le projet
* exécute les tests
* génère le rapport de couverture **JaCoCo**

### Rapport de couverture back-end

Le rapport JaCoCo est disponible ici :

```text
back/target/site/jacoco/index.html
```

---

## Installation et lancement du front-end

1. Se placer dans le dossier front-end

```bash
cd front
```

2. Installer les dépendances

```bash
npm install
```

3. Lancer l’application Angular

```bash
npm run start
```

L’application est accessible à l’adresse :

```text
http://localhost:4200
```

---

## Tests Front-end

### Tests unitaires

Lancer les tests unitaires :

```bash
npm run test
```

Mode watch :

```bash
npm run test:watch
```

Le rapport est disponible ici :
```text
front/coverage/jest/index.html
```

---

### Tests End-to-End (E2E)

Lancer les tests E2E :

```bash
npm run e2e
```

---

## Génération des rapports de couverture Front-end

⚠️ Les tests E2E doivent être lancés avant la génération du rapport.

```bash
npm run e2e:coverage
```

Le rapport est disponible ici :

```text
front/coverage/lcov-report/index.html
```

---

## Ressources utiles

### Mockoon

Un environnement Mockoon est disponible dans le projet.

### Postman

Une collection Postman est fournie :

```text
ressources/postman/yoga.postman_collection.json
```

Pour l’importer, suivre la documentation officielle :

[https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman)

---
