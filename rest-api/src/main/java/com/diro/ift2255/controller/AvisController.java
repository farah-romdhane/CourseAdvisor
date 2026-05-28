package com.diro.ift2255.controller;

import com.diro.ift2255.enums.*;
import com.diro.ift2255.model.Avis;
import com.diro.ift2255.service.AvisService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;


/**
 * Controller responsable de gérer les endpoints liés aux avis.
 * Ne contient AUCUNE logique métier 
 */

public class AvisController {
    
    private final AvisService service;

    /**
     * Constructeur injectant le service nécessaire.
     * * @param service Le service gérant la logique des avis.
     */
    public AvisController(AvisService service) {
        this.service = service;
    }


    /**
     * POST /avis
     * Extrait les données JSON du contexte pour créer et sauvegarder un nouvel avis.
     * * @param ctx Contexte Javalin (doit contenir un JSON valide).
     */
    public void createAvis(Context ctx) {

    Map<String, Object> data = ctx.bodyAsClass(Map.class);

    try {
        // extraction des champs
        String cours = (String) data.get("coursCode");
        String difficulteStr = (String) data.get("difficulte");
        String chargeStr = (String) data.get("chargeTravail");
        String commentaire = (String) data.get("commentaire");
        String sessionStr = (String) data.get("session");
        Object anneeObj = data.get("annee");

        // Conversion vers enums / types
        NiveauDifficulte difficulte = parseDifficulte(difficulteStr);
        Charge charge = parseCharge(chargeStr);
        Trimestre session = parseSession(sessionStr); 
        Integer annee = parseAnnee(anneeObj);

        // Construire l'avis 
        Avis avis = new Avis(cours,difficulte,charge,commentaire,session,annee);

        // Enregistrer l'avis
        boolean added = service.soumettreAvis(avis);

        if (!added) {
            ctx.status(400).json(
                Map.of("error", "Cours inexistant ou avis invalide")
            );
            return;
        }
        ctx.status(201).json("Avis ajoute");
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Données invalides : " + e.getMessage()));
        }
    }
   

    /**
     * GET /avis/{coursCode}
     * Récupère la liste des avis affichables pour un cours spécifique.
     * Respecte la règle  (minimum 5 avis).
     * * @param ctx Contexte Javalin contenant le paramètre de chemin 'coursCode'.
     */
    public void getAvisByCours(Context ctx) {
        String coursCode = ctx.pathParam("coursCode");

        List<Avis> avis = service.getAvisAffichables(coursCode);

        if (avis.isEmpty()) {
            ctx.status(404).json(Map.of("error", "Pas assez d'avis pour afficher ce cours (min 5)"));
            return;
        }

        ctx.json(avis);
    }

    /**
     * GET /avis/{coursCode}/filtrer
     * Filtre les avis affichables selon plusieurs critères (difficulté, charge, session, année).
     * * @param ctx Contexte Javalin contenant les query parameters de filtrage.
     */
    public void filtrerAvis(Context ctx) {
        String coursCode = ctx.pathParam("coursCode");

        // lecture paramètres 
        String diffParam = ctx.queryParam("difficulte");
        String chargeParam = ctx.queryParam("chargeTravail");
        String sessionParam = ctx.queryParam("session");
        String anneeParam = ctx.queryParam("annee");

        // parsing
        NiveauDifficulte difficulte = parseDifficulte(diffParam);
        Charge charge = parseCharge(chargeParam);
        Trimestre session = parseSession(sessionParam);
        Integer annee = parseAnnee(anneeParam);

        // filtrage (inclus la regle des 5 avis)
        List<Avis> result = service.filtrerAvisAffichables(coursCode, difficulte, charge, session, annee);
        if (result.isEmpty()) {
            ctx.status(404).json(Map.of("error", "Pas assez d'avis pour filtrer ce cours (min 5)"));
            return;
        }

         ctx.json(result);
    } 

// ---------------------------------------------------------
 
    /**
     * Convertit une chaîne de caractères en enum de difficulté.
     * * @param value La valeur texte à convertir.
     * @return L'enum NiveauDifficulte correspondant ou null.
     */
    private NiveauDifficulte parseDifficulte(String value) {
        if (value == null || value.isEmpty()) return null;
        return NiveauDifficulte.valueOf(value.toUpperCase());
    }

     /**
     * Convertit une chaîne de caractères en enum de charge.
     * * @param value La valeur texte à convertir.
     * @return L'enum charge correspondant ou null.
     */
    private Charge parseCharge(String value) {
        if (value == null || value.isEmpty()) return null;
        return Charge.valueOf(value.toUpperCase());
    }

    /**
     * Convertit une chaîne de caractères en enum Trimestre.
     * * @param value La valeur texte à convertir.
     * @return L'enum Trimestre correspondant ou null.
     */
    private Trimestre parseSession(String value) {
        if (value == null || value.isEmpty()) return null;
        return Trimestre.valueOf(value.toUpperCase());
    }

    private Integer parseAnnee(Object annee) {
        if (annee == null) return null;

        if (annee instanceof Integer) return (Integer) annee;

        if (annee instanceof String s) {
            if (s.isEmpty()) return null;
             return Integer.parseInt(s);
        }

        return null;
    }

}
