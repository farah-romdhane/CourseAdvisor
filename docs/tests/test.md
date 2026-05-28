## Oracle de tests

## CourseServiceTest

| Entrée (jeu d’arguments) | Sortie | Effets de bord attendus | Type | Description | Cas d’utilisation | Responsable | 
|---------------------------|--------|--------------------------|------|-------------|-------------------|-------------| 
| Repo contient :<br>• Course{id="IFT2255", name="Génie Logiciel", credits=3}<br>Appel : getCourseById("IFT2255") | result.isPresent() = true et id = "IFT2255" | Aucun appel API<br>Lecture seulement dans le dépôt local | Succès | Vérifie que lorsque le cours existe dans le cache, le service le retourne immédiatement sans effectuer d’appel externe. Cela améliore les performances et limite les requêtes inutiles. | Recherche de cours (retour depuis le cache) | Hamza | 
| Repo vide<br>API renvoie mockCourse<br>Appel : getCourseById("IFT9999") | result.isPresent() = true | Le cours récupéré via l’API est **ajouté automatiquement au dépôt local** (fakeRepo.save(mockCourse)) | Succès | Vérifie que si le cours n’est pas trouvé dans le cache, le service appelle l’API, récupère les données externes, puis met à jour le dépôt. | Recherche de cours (chargement via API) | Hamza | 
| API renvoie :<br>• [mockCourse]<br>Appel : getAllCourses() | result.size() = 1 et id = "IFT2255" | Aucun changement du dépôt local (simple lecture de l’API) | Succès | Vérifie que la liste complète des cours est bien renvoyée à partir de l’API, sans modification du cache local. | Recherche de cours (obtenir liste complète) | Hamza | 
| API renvoie :<br>• ["MAT1720", "IFT2015"]<br>Appel : getPrerequisites("IFT2255") | Liste contenant "MAT1720" et "IFT2015" | Aucun changement dans le dépôt (lecture uniquement) | Succès | Vérifie que les prérequis d’un cours sont récupérés correctement via l’API. Fonction cruciale pour aider les étudiants dans leur cheminement. | Pré-requis (vérifier éligibilité) | Hamza |
| API renvoie :<br>• Course{id="IFT2255", schedules = null}<br>Appel : getCourseScheduleForSemester("IFT2255", "H25") | result.isPresent() = true<br>result.get().getSchedules() ≠ null | Aucun appel supplémentaire à l’API<br>Initialisation de la liste `schedules` (liste vide) | Succès | Vérifie que le service gère correctement une réponse API incomplète en initialisant la liste des horaires afin d’éviter toute exception lors de l’affichage ou du traitement ultérieur. | Consultation de l’horaire d’un cours pour un trimestre | Hamza |

## ResultatAcademiqueServiceTest


| Entrée (jeu d’arguments) | Sortie | Effets de bord attendus | Type | Description | Cas d’utilisation | Responsable |
|---------------------------|--------|--------------------------|------|-------------|-------------------|-------------|
| Repo contient :<br>• AcademicResult{sigle="IFT2255", moyenne="3.4", score=4}<br>Appel : getResultatsParCours("IFT2255") | result.isPresent() = true et sigle = "IFT2255" | Lecture uniquement dans le dépôt en mémoire (aucun accès fichier CSV) | Succès | Vérifie que le service retourne correctement les résultats académiques lorsqu’ils existent pour un cours donné. | Consultation des résultats académiques d’un cours | Hamza |
| Repo contient :<br>• AcademicResult{sigle="IFT2255"}<br>Appel : hasResultats("IFT2255") | true | Aucun effet de bord (simple lecture du dépôt) | Succès | Vérifie que la méthode `hasResultats` détecte correctement la présence de résultats académiques pour un cours. | Consultation des résultats académiques d’un cours | Hamza |
| Repo contient :<br>• AcademicResult{sigle="IFT2255"}<br>Appel : getTousLesResultats() | Liste de taille 1 contenant "IFT2255" | Aucun effet de bord (lecture complète du dépôt) | Succès | Vérifie que le service retourne l’ensemble des résultats académiques disponibles, utile pour statistiques ou affichages globaux. | Consultation des résultats académiques d’un cours | Hamza |
| Repo vide<br>Appel : getResultatsParCours("IFT9999") | result.isEmpty() = true | Aucun effet de bord | Échec | Vérifie que le service retourne un résultat vide lorsqu’aucune donnée académique n’est associée au cours demandé. | Consultation des résultats académiques d’un cours | Hamza |
| Repo contient :<br>• AcademicResult{sigle="IFT2255"}<br>Appel : getResultatsParCours(null) | result.isEmpty() = true | Aucun effet de bord | Échec | Vérifie que le service gère correctement une entrée invalide (`null`) et évite toute exception. | Consultation des résultats académiques d’un cours | Hamza |
| Repo vide<br>Appel : hasResultats("IFT2255") | false | Aucun effet de bord | Échec | Vérifie que la méthode `hasResultats` retourne correctement `false` lorsqu’aucun résultat académique n’est disponible. | Consultation des résultats académiques d’un cours | Hamza |

## ComparaisonServiceTest

Les tests couvrent deux fonctionnalités principales :

- **Comparer des cours (2 à 4 cours)**
- **Comparer deux ensembles de cours**

### Comparer des cours

| Entrée (jeu d’arguments) | Sortie attendue | Type | Cas d’utilisation | Responsable |
|--------------------------|-----------------|------|-------------------|-------------|
| Liste valide : `["IFT2035","IFT2255"]` | Objet `ComparaisonDesCours` non nul contenant 2 cours | Succès | **Comparer des cours** | Farah |
| Liste avec 1 seul cours : `["IFT2035"]` | `IllegalArgumentException` | Échec | **Comparer des cours** | Farah |
| Liste avec plus de 4 cours : `["IFT2035","IFT2255","IFT1005","IFT2245","IFT3225"]` | `IllegalArgumentException` | Échec | **Comparer des cours** | Farah |
| Liste contenant un cours inexistant : `["IFT2035","IFT9999"]` | `IllegalArgumentException` | Échec | **Comparer des cours** | Farah |
| Liste de cours `null` | `IllegalArgumentException` | Échec | **Comparer des cours** | Farah |

### Comparer deux ensembles de cours

| Entrée (jeu d’arguments) | Sortie attendue | Type | Cas d’utilisation | Responsable |
|--------------------------|-----------------|------|-------------------|-------------|
| Ensemble A=`["IFT2035","IFT2255"]`, Ensemble B=`["IFT1005","IFT1015"]`, session=`"autumn"` | Objet `ComparaisonEnsembleDesCours` non nul | Succès | **Comparer deux ensembles** | Farah |
| Ensemble A avec 1 cours : `["IFT2035"]`, session=`"autumn"` | `IllegalArgumentException` | Échec | **Comparer deux ensembles** | Farah |
| Ensemble B avec plus de 6 cours | `IllegalArgumentException` | Échec | **Comparer deux ensembles** | Farah |
| Session `null` | `IllegalArgumentException` | Échec | **Comparer deux ensembles** | Farah |
| Ensemble contenant un cours non offert à la session (`IFT2905` en `"autumn"`) | `IllegalArgumentException` | Échec | **Comparer deux ensembles** | Farah |



## AvisServiceTest

Les tests couvrent deux fonctionnalités principales :

- **Soumettre un avis**

- **Consulter et filtrer les avis d’un cours**

| Entrée (jeu d’arguments) | Sortie attendue | Effets de bord attendus | Type | Cas d’utilisation | Responsable |
|---------------------------|----------------|------------------------|------|-------------------|-------------|
| Avis valide : coursCode="IFT2255", difficulté=MOYEN, charge=MOYEN, commentaire="Bon", session=HIVER 2025 | `true` | Le repository passe de 0 → 1 avis | Succès | **Soumettre un avis** | Nouh |
| Avis avec cours inexistant : coursCode="MAT999", difficulté=MOYEN, charge=MOYEN, session=HIVER 2025 | `false` | Aucun avis ajouté au repository | Échec | **Soumettre un avis** | Nouh |
| Avis invalide : coursCode="" (vide), difficulté=MOYEN, charge=MOYEN, session=HIVER 2025 | `false` | Aucun avis ajouté au repository | Échec | **Soumettre un avis** | Nouh |
| Avis invalide : difficulté=null, charge=null, session=null | `false` | Aucun avis ajouté au repository | Échec | **Soumettre un avis** | Nouh |
| Avis valide soumis pour IFT2255 | `true` | Le repository contient exactement 1 avis | Succès | **Soumettre un avis** | Nouh |
| 3 avis existants pour le cours IFT2255 | Liste vide | Aucun effet de bord  | Échec | **Consulter les avis d’un cours** | Nouh |
| 5 avis existants pour le cours IFT2255 | Liste de 5 avis | Aucun effet de bord  | Succès | **Consulter les avis d’un cours** | Nouh |
| 5 avis pour IFT2255 dont 1 avec difficulté=DIFFICILE, filtre difficulté=DIFFICILE | Liste contenant 1 avis | Aucun effet de bord | Succès | **Consulter les avis d’un cours** | Nouh |
| 5 avis pour IFT2255, filtre année=1990 | Liste vide | Aucun effet de bord | Échec | **Consulter les avis d’un cours** | Nouh |
| 5 avis pour IFT2255, aucun filtre appliqué | Liste contenant 5 avis | Aucun effet de bord | Succès | **Consulter les avis d’un cours** | Nouh |



## UserServiceTest

| Entrée (jeu d’arguments) | Sortie attendue | Effets / État après l’appel | Type | Description / Cas d’utilisation | Responsable |
|---------------------------|------------------|-------------------------------|------|----------------------------------|-------------|
| Repo vide<br>Appel : `createUser(new User(0, "Sarah", "sarah@umontreal.ca"))` | `created.getId() = 1`<br>`created.getName() = "Sarah"` | Le dépôt passe de **0 → 1** utilisateur | Succès | Vérifie la création d’un utilisateur et la génération automatique d’un identifiant unique. | Samah |
| Repo contient :<br>• `User{id=1, name="Alice Tremblay", email="alice@umontreal.ca"}`<br>Appel : `createUser(new User(0, "Bob", "alice@umontreal.ca"))` | `IllegalArgumentException` | Aucun changement | Échec | Vérifie qu’un email déjà existant empêche la création d’un nouvel utilisateur. | Samah |
| Repo vide<br>Appel : `getUserById(123)` | `null` | Aucun changement | Succès | Vérifie que la recherche d’un utilisateur inexistant retourne `null`. | Samah |
| Repo contient :<br>• `User{id=1, "Alex Tremblay", "alex.tremblay@umontreal.ca"}`<br>Appel : `updateUser(1, new User(0, "Marie Dupont", "Marie.dupont@umontreal.ca"))` | `result.getName() = "Marie Dupont"`<br>`result.getEmail() = "Marie.dupont@umontreal.ca"`<br>`result.getId() = 1` | L’utilisateur id=1 est remplacé par les nouvelles informations | Succès | Vérifie la mise à jour correcte d’un utilisateur tout en conservant son ID original. | Samah |
| Repo contient :<br>• `User{id=1, "Julie Martin", "julie.martin@umontreal.ca"}`<br>Appel : `deleteUser(1)` | `true` | Le dépôt passe de **1 → 0** utilisateur | Succès | Vérifie que la suppression d’un utilisateur existant retourne `true`. | Samah |


## CourseEligibilityServiceTest

| Entrée (jeu d’arguments) | Sortie attendue | Effets de bord attendus | Type | Description | Cas d’utilisation | Responsable |
|---------------------------|-----------------|--------------------------|------|-------------|-------------------|-------------|
| User.completedCourses = ["IFT1015","IFT1025"]<br>Course.prerequisites = ["IFT1015","IFT1025"] | `true` | Aucun | Succès | Vérifie que l’étudiant est éligible lorsque tous les prérequis du cours ont été complétés. | Vérifier l’éligibilité à un cours | Samah |
| User.completedCourses = ["IFT1015"]<br>Course.prerequisites = ["IFT1015","IFT1025"] | `false` | Aucun | Échec | Vérifie que l’étudiant n’est pas éligible lorsqu’au moins un prérequis requis est manquant. | Vérifier l’éligibilité à un cours | Samah |
| User.completedCourses = []<br>Course.prerequisites = [] | `true` | Aucun | Succès | Vérifie qu’un étudiant est éligible lorsque le cours ne possède aucun prérequis. | Vérifier l’éligibilité à un cours | Samah |
| User.completedCourses = []<br>Course.prerequisites = ["IFT2255"] | `false` | Aucun | Échec | Vérifie qu’un étudiant sans cours complétés n’est pas éligible à un cours avec prérequis. | Vérifier l’éligibilité à un cours | Samah |
| User = `null`<br>Course valide | `IllegalArgumentException` | Aucun | Échec | Vérifie qu’une exception est levée lorsque l’utilisateur fourni est nul. | Validation des paramètres | Samah |
| User valide<br>Course = `null` | `IllegalArgumentException` | Aucun | Échec | Vérifie qu’une exception est levée lorsque le cours fourni est nul. | Validation des paramètres | Samah |

## TDD

Tests généraux prévus pour Recherche de cours et Voir les détails, non implémentés.

| Cas d’utilisation | Entrée (jeu d’arguments) | Résultat attendu | Type | Description |
|-------------------|---------------------------|------------------|------|-------------|
| Recherche par sigle | recherche = "IFT2255" | cours correspondant | Succès | Recherche directe d’un cours par son code. |
| Recherche par sigle inexistant | recherche = "ZZZ9999" | liste vide | Succès | Aucun cours ne correspond au code fourni. |
| Recherche par nom | recherche = "programmation" | liste des cours associés | Succès | Recherche textuelle sur le titre, nom ou description du cours. |
| Recherche vide / invalide | recherche = "" ou "   " | 400 Bad Request | Échec | Une recherche doit contenir un terme valide non vide. |
| Voir les détails d’un cours existant | code = "IFT2255" | détails complets du cours | Succès | Informations complètes du cours incluant horaires, prérequis et équivalences. |
| Voir les détails d’un cours inexistant | code = "ZZZ000" | 404 Not Found | Échec | Aucun cours ne correspond au code dans la source de données. |
| Détails → code invalide | code = "" ou null | 400 Bad Request | Échec | L’identifiant du cours doit être non vide et valide. |