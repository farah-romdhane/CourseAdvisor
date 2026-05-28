---
title: Analyse des besoins - Flux principaux
---

# Flux principaux

## Objectif
Le but des **diagrammes d’activités** est de représenter les échanges entre les acteurs (étudiants et administrateur) et le système de la plateforme.
Ils permettent d’observer les différentes étapes des scénarios d’utilisation, depuis les actions de l’utilisateur jusqu’aux traitements internes du système et aux résultats affichés.

###Chaque diagramme illustre :

*)L’ordre logique des activités (par exemple : saisie de données, vérification, affichage).

*)Les décisions ou conditions pouvant perturber le déroulement (ex. : mot de passe incorrect, courriel déjà utilisé).

*)Les échanges avec des systèmes externes, comme l’API Planifium ou Discord.

*)Les résultats prévus selon les scénarios principaux et alternatifs.

---

Ainsi, les diagrammes montrent clairement comment les acteurs atteignent leurs objectifs que ce soit se connecter, s’inscrire, consulter ,comparer des cours ou recherche un cours 

### Diagrammes :
## Diagramme global
![Diagramme global](diagrammes/Activity%20Diagram%20Global.png)

## CU01 - Se connecter
![Diagramme CU01](diagrammes/Activity%20Diagram%20connexion.png)

## CU02 - S'inscrire à la plateforme
![Diagramme CU02](diagrammes/Activity%20Diagram%20Inscription.png)

## CU03 - Personaliser mon profil
![Diagramme CU03](diagrammes/Activity%20Diagram%20Profil.png)

## CU04 - Rechercher un cours
![Diagramme CU04](diagrammes/Activity%20Diagram%20Recherche%20cours.png)

## CU05 - Voir les détails d'un cours
![Diagramme CU05](diagrammes/Activity%20Diagram%20Infos%20cours.png)

## CU06 - Comparer les cours
![Diagramme CU06](diagrammes/Activity%20Diagram%20Comparaison%20de%20cours.png)

## CU07 - Comparer deux choix de cours
![Diagramme CU07](diagrammes/Activity%20Diagram%20Comparer%20deux%20choix%20de%20cours.png)


### Description des flux complexes

Dans certains cas, les activités ne se déroulent pas toujours de manière simple.Il peut y avoir plusieurs chemins possibles ou des interactions avec des services externes :

**CU01 – Connexion :** selon que les identifiants sont corrects ou non, l’étudiant peut être renvoyé pour ressaisir son mot de passe ou être invité à s’inscrire s’il n’a pas de compte.

**CU02 – Inscription :** le système vérifie que le courriel n’est pas déjà utilisé et que tous les champs obligatoires sont remplis. Si un champ est manquant, l’étudiant doit le compléter ; si le courriel existe déjà, un message l’informe et il peut choisir de se connecter à la place.

**CU03 – Profil :** l’étudiant peut modifier ses préférences. il dépend de la connexion préalable (CU01), et les modifications sont enregistrées pour personnaliser ses recommandations.

**CU04 – Recherche cours :** l’étudiant saisit un critère de recherche et le système interroge l’API Planifium. Si aucun cours ne correspond, un message est affiché. 

**CU05 – Infos cours :** lorsque l’étudiant sélectionne un cours, le système récupère les informations détaillées (plan, horaires, professeur, résultats académiques et avis). Si les avis sont insuffisants ou que certains résultats ne sont pas disponibles, le système adapte l’affichage en conséquence.

**CU06 – Comparaison de cours :** pour comparer plusieurs cours, les informations sont récupérées puis regroupées dans le tableau final à l’étudiant .Si l’étudiant n’a sélectionné qu’un seul cours, un message lui demande de choisir au moins deux cours. Si certaines données sont incomplètes ou si les services externes sont indisponibles, le tableau est généré avec ce qui est disponible.

**CU07 - Comparer deux choix de cours :** Le système compare deux ensembles de cours créés par l’étudiant. Il vérifie que les deux choix sont valides, récupère les données nécessaires (Planifium, résultats, avis) et signale les informations manquantes. Même en cas de données partielles ou de services externes indisponibles, un tableau comparatif est généré pour permettre à l’étudiant de choisir la meilleure combinaison.

---
Ces flux complexes montrent comment le système gère les erreurs, les boucles et la communication avec des services externes, tout en garantissant que les utilisateurs peuvent atteindre leurs objectifs dans différentes situations.