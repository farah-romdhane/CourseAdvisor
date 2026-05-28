package com.diro.ift2255.controller;

import com.diro.ift2255.model.ComparaisonDesCours;
import com.diro.ift2255.model.ComparaisonEnsembleDesCours;
import com.diro.ift2255.model.EnsembleDeCours;
import com.diro.ift2255.service.ComparaisonService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur responsable de la gestion des requêtes HTTP liées
 * à la comparaison de cours et d’ensembles de cours.
 */
public class ComparaisonController {

    private final ComparaisonService comparaisonService;

    public ComparaisonController(ComparaisonService comparaisonService) {
        this.comparaisonService = comparaisonService;
    }

    // ===================== DTOs =====================

    /** Requête pour comparer des cours */
    public static class ComparaisonRequest {
        private List<String> courses;

        public List<String> getCourses() {
            return courses;
        }
    }

    /** Requête pour créer un ensemble */
    public static class EnsembleRequest {
        private List<String> courses;
        private String session;

        public List<String> getCourses() {
            return courses;
        }

        public String getSession() {
            return session;
        }
    }

    /** Requête pour comparer deux ensembles */
    public static class ComparaisonEnsemblesRequest {
        private List<String> ensembleA;
        private List<String> ensembleB;
        private String session;

        public List<String> getEnsembleA() {
            return ensembleA;
        }

        public List<String> getEnsembleB() {
            return ensembleB;
        }

        public String getSession() {
            return session;
        }
    }

    // ===================== ENDPOINTS =====================

    /** Comparer plusieurs cours */
    public void compare(Context ctx) {
        try {
            ComparaisonRequest req = ctx.bodyAsClass(ComparaisonRequest.class);

            if (req.getCourses() == null
                    || req.getCourses().size() < 2
                    || req.getCourses().size() > 4) {

                ctx.status(400).json(Map.of(
                        "erreur", "L'étudiant doit choisir entre 2 et 4 cours."
                ));
                return;
            }

            ComparaisonDesCours comparaison =
                    comparaisonService.compare(req.getCourses());

            ctx.json(comparaison);

        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("erreur", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "erreur", "Erreur interne lors de la comparaison des cours."
            ));
        }
    }

    /** Créer un ensemble de cours */
    public void createEnsemble(Context ctx) {
        try {
            EnsembleRequest req = ctx.bodyAsClass(EnsembleRequest.class);

            EnsembleDeCours result =
                    comparaisonService.createEnsemble(
                            req.getCourses(),
                            req.getSession()
                    );

            ctx.json(result);

        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("erreur", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of(
                    "erreur", "Erreur interne lors de la création de l’ensemble"
            ));
        }
    }

    /** Comparer deux ensembles de cours */
    public void compareEnsembles(Context ctx) {
        try {
            ComparaisonEnsemblesRequest req =
                    ctx.bodyAsClass(ComparaisonEnsemblesRequest.class);

            ComparaisonEnsembleDesCours result =
                    comparaisonService.compareEnsembles(
                            req.getEnsembleA(),
                            req.getEnsembleB(),
                            req.getSession()
                    );

            ctx.json(result);

        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("erreur", e.getMessage()));
        }
    }
}