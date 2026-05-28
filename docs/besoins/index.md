---
title: Analyse des besoins - Présentation générale
---

# Présentation du projet

## Méthodologie pour la cueillette des données
- Rencontre d’équipe : discussion initiale pour définir la vision générale du projet, les attentes et les fonctionnalités principales
- Brainstorming : échange collectif permettant d’identifier les besoins des étudiants, les fonctionnalités clés et les problèmes liés au processus d’inscription aux cours

## Description du domaine

### Processus métier existants:
À l'heure actuelle, un étudiant doit naviguer entre multiples sources d'information pour organiser son emploi du temps :

Il se réfère d’abord à Planifium pour obtenir les données officielles
sur les cours (les horaires, les programmes, les crédits et les
prérequis). Ces informations sont précises, mais elles n'offrent aucun
aperçu de la charge effective ou de la complexité.

Ensuite, il doit consulter les plans de cours afin de comprendre la
structure, le contenu et les méthodes d’évaluation.

Pour compléter ces données , l’étudiant cherche des avis informels
(Discord, forums, amis ) pour recueillir des avis : volume de travail,
degré de difficulté, recommandations. Ces données, bien que
fréquemment utiles, sont généralement dispersées.

L'étudiant peut également prendre rendez-vous avec un TGDE ou un conseiller pédagogique. Ces rencontres permettent de vérifier son cheminement, de confirmer les prérequis, d’éviter les conflits d’horaire et d’obtenir des recommandations adaptées à son programme ou à sa situation académique.

Enfin, il doit combiner toutes ces informations avec ses propres
contraintes (emploi, rythme, intérêts), tout en vérifiant les prérequis,
les cycles et les éventuels conflits d’horaire.

Par conséquent, chaque étudiant doit lui-même combiner et
interpréter toutes ces données, ce qui prend du temps et peut
conduire à des décisions peu optimales.

### Fonctionnement
Le système est basé sur une architecture client–serveur:
- Le frontend (HTML, CSS, JavaScript) offre une interface qui permet aux étudiants de naviguer facilement dans la plateforme et interagir avec les fonctionalités principales
- Le backend (Java / Spring Boot) assure la logique, la gestion des requêtes et la communication entre le frontend et la base de données
- La base de données (PostgreSQL) stocke les cours, les utilisateurs et les avis
- Le serveur Nginx utilisé comme proxy inverse pour diriger les requêtes entre le frontend et le backend, tout en aidant à sécuriser les échanges.
- L’application est déployée sur un serveur Ubuntu 22.04


### Acteurs
| Acteur | Rôle principal | Objectifs |
|-------------|--------------------|----------------|
| Étudiant utilisateur | Utilisateur principal du système | Consulter les cours, s’inscrire ou se désinscrire, consulter ou publier des avis sur les cours et enseignants. |
| Professeurs / Auxiliaires d’enseignement | Fournisseurs d’information pédagogique et de résultats académiques | Fournissent les plans de cours et mettent à jour les informations en lien avec un cours|
| Conseiller pédagogique| Accompagnateur académique | Répondre aux questions sur le cheminement, valider les choix de cours, informer sur les règlements du programme. |
| TGDE | Support administratif | Vérifier les prérequis, résoudre les problèmes d’inscription et assurer le suivi administratif du dossier étudiant. |


### Dépendances
- Backend : Spring Boot 
- Frontend : HTML, CSS, JavaScript
- Serveur : Ubuntu 22.04
- Serveur web : Nginx


## Hypothèses et contraintes

### Hypothèses:
- **Disponibilité et stabilité des sources de données** :  
On suppose que l’API *Planifium* est accessible, stable et bien documentée, permettant de récupérer sans interruption les informations officielles sur les cours (titres, horaires, prérequis, crédits, enseignants).
- **Collaboration des étudiants pour les avis** :    
On suppose que les étudiants accepteront de partager leurs avis sur *Discord* ou via la plateforme, de manière volontaire et en respectant l’anonymat.
- **Format standardisé des données** :   
Les données issues des différentes sources (*Planifium*, résultats académiques en *CSV*, avis en *JSON*) sont supposées cohérentes, valides et intégrables sans transformation complexe

---  
### Contraintes:
- **Centralisation** :  
Toutes les sources (Planifium, CSV, JSON) doivent être regroupées dans une seule interface.

- **Seuil pour les avis** :  
Un cours doit avoir au moins 5 étudiants qui ont donné leur avis (via Discord) avant que ces avis soient visibles sur la plateforme.

- **Confidentialité** :  
 Aucune donnée personnelle identifiable ne doit être enregistrée ou affichée. Toutes les informations doivent être anonymes avant d’être stockées ou consultées.

- **Accessibilité** :  
La plateforme doit être simple, claire et utilisable par tous les étudiants, y compris ceux ayant des limitations technologiques ou fonctionnelles. L’interface doit offrir une navigation intuitive, une lisibilité adéquate et demeurer fonctionnelle sur tous les navigateurs.