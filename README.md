## Lien repo du projet

Lien : https://github.com/EnzoT454/Devoir3-2255  


## Lien JavaDoc du projet

```bash
 -[JavaDoc](/docs/javaDoc/index.html)
```

## Lien démonstration

Lien: https://drive.google.com/file/d/1NlWyTHpnAUDsi9Z7IEA1Gj6UgxJBWbhf/view?usp=drive_link

## CoursAdvisor (Brève description)
 
***CoursAdvisor*** est une plateforme web destinée aux étudiants du DIRO (Université de Montréal).
Elle centralise les données provenant de Planifium, des résultats académiques fournis par les enseignants ou les auxiliaires et des avis étudiants collectés via Discord, afin d’aider les étudiants à choisir leurs cours de manière éclairée.
L’application permet de rechercher, comparer et consulter des cours tout en personnalisant les recommandations selon le profil de l’étudiant.

## Fonctionnalités par rôle

### Étudiant
- Rechercher des cours :
  - Par sigle partiel (ex. : "IFT" retourne tous les cours IFT*)
  - Par mots-clés dans le titre ou la description
- Voir les cours offerts dans un programme
- Voir les cours offerts pour un trimestre donné (format : H25, A24, E24)
- Limiter la recherche aux cours d’un programme donné
- Voir l’horaire d’un cours pour un trimestre donné
  - Distinction des sections et des types d’activité
- Vérifier son éligibilité à un cours :
  - L’étudiant fournit la liste des cours complétés et son cycle
  - Le système vérifie les prérequis et le cycle approprié
- Voir les résultats académiques d’un cours
  - Message d’erreur convivial si le cours n’existe pas
- Voir les avis étudiants agrégés pour un cours
- Comparer deux cours :
  - Charge de travail (avis étudiants)
  - Difficulté estimée (résultats académiques)
  - Autres critères issus du catalogue
- Créer un ensemble de cours (maximum 6)
- Voir l’horaire résultant pour un trimestre donné

### Bot Discord
- Soumettre un avis étudiant pour un cours donné


## Instructions pour consulter le rapport

1. Activez l'environnement virtuel avec 

```bash
pipenv shell
```
2. Installez les dépendances listées dans `requirements.txt` (à exécuter dans le répertoire du projet) :

```bash
pip install -r requirements.txt
```

> Avant toute utilisation, assurez-vous que l’environnement virtuel est activé (`pipenv shell`).

Pour lancer un serveur de développement local et visualiser le rapport en temps réel, utilisez :

```bash
mkdocs serve
```

Le site sera accessible à l'adresse [http://127.0.0.1:8000](http://127.0.0.1:8000)

## Exécution des tests unitaires

Pour lancer les tests JUnit du projet, suivez les étapes ci-dessous.  
Assurez-vous d’être dans le bon répertoire avant d’exécuter les commandes Maven.

### 1. Accéder au projet backend

```bash
cd ./rest-api
```

### 2. Compiler le projet

Cette commande nettoie les anciens fichiers générés puis recompile tout le code Java.

```bash
mvn clean compile
```

### 3. Exécuter les tests

Lance l’ensemble des tests unitaires situés dans src/test/java/.

```bash
mvn test
```

### Résumé des tests

Les tests unitaires ont été réalisés à l’aide de JUnit et couvrent les principales fonctionnalités métier de l’application. Chaque service a été testé de manière isolée.

- Recherche et consultation de cours – CourseServiceTest – 5 tests – Hamza

- Consultation des résultats académiques – ResultatAcademiqueServiceTest – 6 tests – Hamza

- Comparaison de cours (2 à 4 cours) – ComparaisonServiceTest – 5 tests – Farah

- Comparaison de deux ensembles de cours – ComparaisonServiceTest – 5 tests – Farah

- Soumission et consultation des avis étudiants – AvisServiceTest – 10 tests – Nouh

- Gestion des utilisateurs (création, mise à jour, suppression) – UserServiceTest – 5 tests – Samah

- Vérification de l’éligibilité à un cours – CourseEligibilityServiceTest – 6 tests – Samah

Au total, 42 tests unitaires valident les cas de succès, les cas d’échec et la gestion des entrées invalides, assurant ainsi une couverture complète des fonctionnalités critiques du système.

## Vérification de l'implémentation des cas d'utilisation CUs

Le projet CoursAdvisor peut être exécuté de deux manières différentes, selon les besoins :
un **mode serveur (API REST)** et un **mode console (test interactif)**.
Ces deux modes sont **totalement indépendants**.

### 1. Mode Console — Tests interactifs rapides (MainTestCourse.java)

Ce mode n’utilise ni Javalin, ni les routes, ni les contrôleurs.  
Il permet une vérification rapide du fonctionnement de l’implémentation des cas d’utilisation suivants :  
- recherche de cours,  
- comparaison de cours,  
- affichage des détails d’un cours.  

Tout se fait entièrement **offline**, directement dans le terminal.

Pour exécuter :

```bash
cd ./rest-api
```

```bash
mvn clean compile
```

```bash
mvn exec:java -Dexec.mainClass="com.diro.ift2255.MainTestCourse"
```

Ensuite, choisissez une option dans le menu affiché dans le terminal.

### 2. Mode Serveur — API REST (Main.java)

Ce mode lance le serveur web Javalin et rend accessibles toutes les routes définies dans `Routes.java`.

- Utilisé pour l’API REST
- Nécessite Postman, un navigateur, ou un frontend
- Démarre un serveur HTTP accessible sur `http://localhost:7070`
- Active les contrôleurs (`CourseController`, `UserController`, etc.)

Pour exécuter :

```bash
cd ./rest-api
```

```bash
mvn clean compile
```

```bash
mvn exec:java -Dexec.mainClass="com.diro.ift2255.Main"
```

## Compilation et installation du projet

Depuis la racine du projet, exécutez la commande suivante :

```bash
mvn clean install
```

Cette commande nettoie les artefacts précédents et compile le projet en générant un fichier JAR exécutable.

Le fichier JAR est généré dans le répertoire : `target/rest-api-1.0-SNAPSHOT.jar`

## Lancement du bot Discord (Bonus)

Le dossier `discord-bot/` contient un bot Discord permettant de collecter automatiquement les avis des étudiants publiés dans un serveur Discord.  
Ces avis sont ensuite sauvegardés localement et pourront être consommés par l’application CoursAdvisor.

### Étapes pour lancer le bot

1. Accéder au dossier du bot :

```bash
cd ./discord-bot
```

2. Créer un environnement virtuel Python :

```bash
python3 -m venv venv
```

ou

```bash
python -m venv venv
```


3. Activer l’environnement virtuel :

```bash
source venv/bin/activate
```

4. Installer les dépendances nécessaires :

```bash
pip install discord.py python-dotenv
```

5. Lancer le bot :

```bash
python3 botDiscord.py
```

ou

```bash
python botDiscord.py
```

6. Ouvrir le serveur Discord à l’adresse suivante :
  https://discord.gg/3BhMhhFn

7. Aller dans le salon #general et poster un avis

8. Si l’avis est valide : le bot le reconnaît, il l’analyse, puis l’ajoute automatiquement dans :

```bash
./discord-bot/data/avisDiscord.json
```

## Prototype interactif (Bonus)

Voici le lien permettant de visualiser le prototype interactif initial [Prototype](https://www.figma.com/make/oLDVLNKRifwxeUm5kLpRos/CourAdvisor--Copy-?node-id=0-1&p=f&t=6YJ55hxH3yMLNSCu-0&fullscreen=1).

Le prototype de CoursAdvisor comporte quatre pages principales. D’abord, la page d’accueil permet de se connecter ou de créer un compte étudiant à l’aide d’un courriel UdeM . Ensuite, la page du catalogue affiche la liste des cours avec leurs notes, difficultés et descriptions; l’utilisateur peut consulter les détails ou ajouter des cours à la comparaison. La page de comparaison permet d’analyser les cours à la fois selon leur charge de travail, difficulté et crédits .Enfin la page du profil permet d’ajuster les préférences pour personnaliser les recommandations.    

Note : Pour vous connecter, vous pouvez utiliser une adresse courriel au format suivant : aaaaa@aa.aa, ainsi que n’importe quel mot de passe.


## Structure du projet

Le projet est structuré selon une architecture MVC avec une couche Service : les Controllers gèrent les requêtes HTTP via Javalin, les Services contiennent la logique métier , les Models représentent les entités du domaine (Cours, Avis, User, Comparaison), et les Enums assurent la cohérence des types.Des classes Util aident à la validation et aux réponses HTTP, tandis que les tests JUnit valident les fonctionnalités métier. Les données sont chargées depuis des fichiers JSON un discord bot permet de récupérer des avis externes . 

```text
Devoir2-2255/ 
|
├─ docs/                                   → Documentation (MkDocs)
│  ├─ besoins/
│  │  ├─ diagrammes/                       → Diagrammes de flux + cas d'utilisation
│  │  ├─ cas-utilisation.md                → Cas d’utilisation et scénarios
│  │  ├─ exigences.md                      → Exigences fonctionnelles et non fonctionnelles
│  │  ├─ flux-principaux.md                → Diagrammes des flux d’informations
│  │  ├─ index.md                          → Analyse des besoins - Présentation générale
│  │  ├─ glossaire.md                      → Définition des termes clés
│  │  ├─ risques.md                        → Analyse des risques
│  │
│  ├─ conception/
│  │  ├─ DiagrammesDeSequence/             → Diagrammes de séquence
│  │  ├─ architecture.md                   → Architecture logicielle globale(Diagrammes)
│  │  ├─ C4_niveau1.png                    → Diagramme C4 (Contexte)
│  │  ├─ C4_niveau2.png                    → Diagramme C4 (Conteneurs)
│  │  ├─ C4_niveau3.png                    → Diagramme C4 (Composants)
│  │  └─ DiagrammeDeClasse.png             → Diagramme c4 (Code)
│  │
│  ├─ css/
│  │  └─ no-sidebar.css                    → Style personnalisé
│  │
|  ├─javaDoc/                              → Documentation JavaDoc
|  |  └─ index.html                        → Page d'accueil de la documentation
|  |  └─ (autres fichiers HTML générés automatiquement)
|  |
│  ├─ tests/
│  │  └─ test.md                           → Oracle de tests
│  │
│  └─ index.md                             → Page d’accueil de la documentation
│
├─ rest-api/                               → API REST (Java + Javalin)
│  ├── data/
│  │   ├── Avis.json                       → Données de test pour les avis
│  │   └── Courses.json                    → Données de test pour les cours
|  |   ├── historique_cours_prog_117510.csv → Données de test pour les historiques de cours
│  │   └── Users.json                      → Données de test pour les utilisateurs
│  │ 
│  ├── discord-bot/   
│  │   ├── botDiscord.py                   → Script Python pour le bot Discord
│  │   └── discordRequirements.txt         → bot Discord requirements
│  │
│  ├── src/
│  │   ├── main/
│  │   │   ├── java/com/diro/ift2255/
│  │   │   │
│  │   │   ├── config/
│  │   │   │   └── Routes.java             → Définition des routes HTTP
│  │   │   │
│  │   │   ├── controller/
│  │   │   │   ├── AvisController.java     → Endpoints avis
│  │   │   │   ├── ComparisonController.java → Endpoints comparaison de cours
|  │   │   │   ├──ResultatAcademiqueController.java → Endpoints résultats académiques
│  │   │   │   ├── CourseController.java   → Endpoints cours 
│  │   │   │   └── UserController.java     → Endpoints utilisateurs
│  │   │   │
│  │   │   ├── enums/                      → Énumérations utilisées 
│  │   │   │   ├── Charge.java 
│  │   │   │   ├── Cycle.java
│  │   │   │   ├── NiveauDifficulte.java
│  │   │   │   └── Trimestre.java
│  │   │   │
│  │   │   ├── model/                      
|  |   |   |   ├── AcademicResult.java             → AcademicResult
│  │   │   │   ├── Avis.java                       → Avis d'un cours
│  │   │   │   ├── ComparisonDesCours.java         → Comparaison entre cours
│  │   │   │   ├── ComparisonEnsembleDesCours.java → Comparaison entre plusieurs cours
│  │   │   │   ├── Course.java                     → Données cours
|  |   |   |   ├── EnsembleDesCours.java           → Ensemble de cours
|  |   |   │   ├── EnsembleDesCoursResult.java     → Ensemble des result            
|  |   |   |   ├── Program.java                    → Données programme
|  │   │   │   ├── ProgramCoursesResponse.java     → Réponse cours
|  │   │   │   ├── StudentProfile.java             → Données profil étudiant
│  │   │   │   └── User.java                       → Données utilisateur
│  │   │   │
│  │   │   ├── repository/
│  │   │   │   ├── AvisRepository.java              → Gestion des avis
│  │   │   │   ├── CourseRepository.java            → Gestion des cours
|  │   │   │   ├── FakeAvisRepository.java          → fake repo pour tests
│  │   │   │   ├── FakeCourseRepository.java        → fake repo pour tests
|  |   |   |   |── FakeResultatAcademiqueRepository.java          → fake repo pour tests
│  │   │   │   ├── ResultatAcademiqueRepository.java→ Gestion des résultats académiques
│  │   │   │   └── UserRepository.java              → Gestion des utilisateurs
│  │   │   │
│  │   │   ├── service/
│  │   │   │   ├── AvisService.java                 → Logique métier (avis)
│  │   │   │   ├── ComparisonService.java           → Logique métier (comparaison)
│  │   │   │   ├── CourseService.java               → Logique métier (cours)
│  │   │   │   ├── CourseEligibilityService.java    → Logique métier (éligibilité)
│  │   │   │   ├── FakeAvisService.java             → fake service pour tests
│  │   │   │   ├── ProgramService.java            
│  │   │   │   ├── EligibilityService.java          → Logique métier (éligibilité)
│  │   │   │   ├── FakeCourseService.java           → fake service pour tests
│  │   │   │   ├── ResultatAcademiqueService.java   → Logique métier (résultats académiques)
│  │   │   │   └── UserService.java                 → Logique métier (users)
│  │   │   │
│  │   │   ├── util/                                → Utilitaires généraux
│  │   │   │   ├── FakeHttpClientAPI.java
│  │   │   │   ├── HttpClientAPI.java
│  │   │   │   ├── HttpClientApiResponse.java
│  │   │   │   ├── HttpStatus.java
│  │   │   │   ├── ResponseUtil.java
│  │   │   │   └── ValidationUtil.java
│  │   │   │
│  │   │   ├── vue/                                 →  Interface vue côté serveur
│  │   │   │   ├── CourseVue.java                   → Point d’entrée du serveur Javalin
│  │   │   │
│  │   │   ├── Main.java                            → Point d’entrée du serveur Javalin
│  │   │   └── MainTestCourse.java                  → Script de test main pour les cas d'utilisation
│  │   │
│  │   └── test/                                    → Tests unitaires
│  │       └── java/com/diro/ift2255/service/
│  │             ├── UserServiceTest.java
│  │             ├── CourseServiceTest.java
│  │             ├── AvisServiceTest.java
|  |             ├── CourseEligibilityServiceTest.java
|  |             ├──ResulatatAcademiqueServiceTest.java
│  │             └── ComparisonServiceTest.java
│  │
│  └── pom.xml                             → Configuration Maven
│
├─ equipe2_feedback.pdf                    → Feedback devoir1
├─ mkdocs.yml                              → Configuration du site
├─ requirements.txt                        → Dépendances Python
├─ Pipfile                                 → Environnement virtuel pipenv
└─ README.md                               → Documentation du projet
```

## Prérequis

Assurez-vous d’avoir les outils suivants installés :

- Python **3.11** ou plus récent
- `pip` (gestionnaire de paquets Python)
- `pipenv` ou équivalent (gestion d’environnement virtuel) 
  - Évite de polluer votre système et les conflits de version.
  - Installez-le avec `pip install pipenv`.
- Type terminal : bash.

---
### Prérequis système

- **Java Development Kit (JDK)** : version **17 ou supérieure**
- **Apache Maven** : version **3.8 ou supérieure**
- **Système d’exploitation** : Windows, macOS ou Linux
- **Connexion Internet** : requise lors du premier lancement pour le téléchargement automatique des dépendances Maven

---

## Dépendances externes (Maven)

Les dépendances du projet backend sont définies dans le fichier `pom.xml`.  
Aucune installation manuelle n’est nécessaire : Maven télécharge automatiquement toutes les bibliothèques requises lors de la compilation.

Bibliothèques utilisées :

- **Javalin (v6.7.0)**  
  Framework léger utilisé pour la création de l’API REST.

- **Jackson Databind (v2.17.2)**  
  Utilisé pour la sérialisation et la désérialisation des données au format JSON (cours, avis, etc.).

- **SLF4J Simple (v2.0.16)**  
  Gestion des journaux (logs) de l’application.

- **JUnit 5 (v5.13.4)**  
  Framework de tests unitaires permettant de valider les fonctionnalités de l’application.

Le projet est configuré pour être compilé avec **Java 17**, conformément aux propriétés définies dans le fichier `pom.xml`.

---
## Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## Ressources utiles

- Documentation officielle MkDocs
- Thème Material for MkDocs
