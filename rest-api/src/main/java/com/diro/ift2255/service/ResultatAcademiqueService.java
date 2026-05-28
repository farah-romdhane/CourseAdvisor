package com.diro.ift2255.service;

import com.diro.ift2255.Repository.ResultatAcademiqueRepository;
import com.diro.ift2255.model.AcademicResult;

import java.util.List;
import java.util.Optional;

/**
 * Service responsable de la logique métier
 * liée aux résultats académiques des cours.
 */
public class ResultatAcademiqueService {

    private final ResultatAcademiqueRepository repository;

    /**
     * Initialise le service avec un repository de résultats académiques.
     *
     * @param repository repository utilisé pour accéder aux résultats académiques
     */
    public ResultatAcademiqueService(ResultatAcademiqueRepository repository) {
        this.repository = repository;
    }

    /**
     * Retourne les résultats académiques d’un cours donné.
     *
     * @param sigle Code du cours (ex: IFT2255)
     * @return Optional contenant les résultats si trouvés
     */
    public Optional<AcademicResult> getResultatsParCours(String sigle) {

        if (sigle == null || sigle.isBlank()) {
            return Optional.empty();
        }

        return repository.findBySigle(sigle.trim().toUpperCase());
    }

    /**
     * Retourne l’ensemble des résultats académiques disponibles.
     *
     * @return la liste de tous les résultats académiques
     */
    public List<AcademicResult> getTousLesResultats() {
        return repository.findAll();
    }

    /**
     * Indique si un cours possède des résultats académiques.
     *
     * @param sigle code du cours
     * @return true si des résultats existent pour ce cours, false sinon
     */
    public boolean hasResultats(String sigle) {
        return getResultatsParCours(sigle).isPresent();
    }
}
