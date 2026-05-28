---
title: Analyse des besoins - Risques
---

# Analyse des risques

## Identification des risques


### Risque 1 – Données insuffisantes pour les avis

- **Probabilité** : Élevée
- **Sévérité** : Moyenne 
- **Impact** : Si un nombre restreint d’étudiants fournit des retours, la plateforme manquera de crédibilité et de pertinence.
- **Plan de mitigation** :  
    - Promouvoir la participation sur Discord
    - Mettre en avant des statistiques visuelles pour inciter les étudiants à contribuer
    - Autoriser les retours anomymes pour augmenter la participation

### Risque 2 - Confidentialité des étudiants
- **Probabilité** : Moyenne 
- **Sévérité** : Élevé 
- **Impact** : Risque de divulgation d'informations personnelles si les données ne sont pas bien gérées.  
- **Plan de mitigation** :  
    - Ne jamais stocker de noms avec des avis
    - Anonymiser tous les messages avant leurs enregistrement
    - Consultation avec le bureau de conformité de l'UdeM

### Risque 3 - Biais dans les avis des étudiants
- **Probabilité** : Élevée
- **Sévérité** : Moyenne
- **Impact** : Certains avis peuvent être exagérés ou non représentatifs, ce qui risque de donner une perception incorrecte d'un cours ou d'un professeur. Les différences entre les profils étudiants peuvent également introduire des biais, ce qui rend l'interprétation des avis plus complexe.
- **Plan de mitigation** :  
  - Ajouter des filtres personnalisés permettant de voir les avis des étudiants par session, par profil et par ancienneté
  - Afficher un indicateur de fiabilité basé sur la quantité et la diversité des avis reçus
  - Mentionner explicitement les biais potentiels afin d’aider les utilisateurs à interpréter correctement les avis


### Risque 4 - Dépendance aux données externes (Planifium et Discord)
- **Probabilité** : Moyenne  
- **Sévérité** : Moyenne 
- **Impact** : Si Planifium ou Discord arrêtent de fonctionner ou ne se mettent pas à jour à temps, certaines informations sur les cours risquent d’être incomplètes ou incorrectes sur la plateforme.   
- **Plan de mitigation** :  
  - Sauvegarder localement les données importantes pour réduire l'impact d'une panne si une source devient temporairement inaccessible
  - Permettre aux administrateurs de mettre à jour manuellement les informations si une API externe est en panne.
  - Avertir les utilisateurs quand certaines données ne peuvent pas être chargées à cause d’un problème externe.

### Risque 5 – Fiabilité et performance du système
- **Problème** : interne (architecture et charge du système)
- **Probabilité**  : Moyenne
- **Sévérité** : Élevée
- **Impact** : Si le serveur devient lent ou surchargé, certaines fonctionnalités peuvent temporairement ne plus répondre, ce qui entraîne une perte d'accès aux informations. Cela reste temporaire, mais pourrait nuire à l'expérience de l'utilisateur, surtout en période de forte utilisation.
- **Plan de mitigation** :
  - Mettre en place un cache local pour éviter d’appeler trop souvent l’API Planifium.
  - Effectuer des tests de charge avant la mise en ligne afin de simuler les périodes de forte demande.
  - Surveiller la performance du serveur afin de détecter les ralentissements rapidement.

## Modification du processus opérationnel

> Si la mise en place du système modifie des processus internes ou des pratiques actuelles, il est essentiel de les identifier ici.

L'intégration de Discord comme principale source d'avis change la façon dont les étudiants recueillent et partagent les informations sur les cours. Au lieu que les étudiants doivent chercher des avis un peu partout, tout sera maintenant centralisé sur une même plateforme.  

Ce changement implique :  
    - Une meilleure gestion de la confidentialité afin de proteger l'identité des étudiants  
    - Une dépendance à Discord pour obtenir des avis récents et variés  
    - Un suivi régulier pour garder les informations à jour et représentative aux cours chercher  