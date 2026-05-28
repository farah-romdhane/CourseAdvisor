package com.diro.ift2255.controller;

import com.diro.ift2255.model.AcademicResult;
import com.diro.ift2255.service.ResultatAcademiqueService;
import com.diro.ift2255.util.ResponseUtil;

import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsable de l'exposition des endpoints
 * liés aux résultats académiques des cours.
 */
public class ResultatAcademiqueController {

    private final ResultatAcademiqueService service;

    /**
     * Construit le contrôleur des résultats académiques.
     *
     * @param service service d’accès aux résultats académiques
     */
    public ResultatAcademiqueController(ResultatAcademiqueService service) {
        this.service = service;
    }

    /**
     * GET /results
     * Retourne tous les résultats académiques disponibles.
     * @param ctx contexte HTTP Javalin
     */
    public void getAllResults(Context ctx) {
        List<AcademicResult> results = service.getTousLesResultats();
        ctx.json(results);
    }

    /**
     * GET /results/:sigle
     * Retourne les résultats académiques d’un cours donné.
     * @param ctx contexte HTTP Javalin contenant le paramètre {@code sigle}
     */
    public void getResultBySigle(Context ctx) {
        String sigle = ctx.pathParam("sigle");

        if (!isValidSigle(sigle)) {
            ctx.status(400)
               .json(ResponseUtil.formatError("Le sigle du cours est invalide."));
            return;
        }

        Optional<AcademicResult> result = service.getResultatsParCours(sigle);

        if (result.isPresent()) {
            ctx.json(result.get());
        } else {
            ctx.status(404)
               .json(ResponseUtil.formatError(
                   "Aucun résultat académique trouvé pour le cours : " + sigle
               ));
        }
    }

    // ------------------ UTILITAIRE ------------------

    /**
     * Validation simple du sigle de cours.
     * @param sigle sigle du cours à valider
     * @return {@code true} si le sigle est valide, {@code false} sinon
     */
    private boolean isValidSigle(String sigle) {
        return sigle != null && sigle.trim().length() >= 6;
    }
}
