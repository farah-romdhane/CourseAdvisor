package com.diro.ift2255.Repository;

import com.diro.ift2255.model.AcademicResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation factice du repository des résultats académiques.
 * Utilisée pour les tests unitaires afin d’éviter l’accès au fichier CSV.
 */
public class FakeResultatAcademiqueRepository
        extends ResultatAcademiqueRepository {

    private final List<AcademicResult> data = new ArrayList<>();

    /**
     * Ajoute un résultat académique au stockage en mémoire.
     *
     * @param result résultat académique à sauvegarder
     */
    public void save(AcademicResult result) {
        if (result != null) {
            data.add(result);
        }
    }

    /**
     * Retourne tous les résultats académiques stockés en mémoire.
     *
     * @return liste des résultats académiques
     */
    @Override
    public List<AcademicResult> findAll() {
        return new ArrayList<>(data); 
    }

    /**
     * Recherche un résultat académique à partir du sigle du cours.
     *
     * @param sigle sigle du cours
     * @return un Optional contenant le résultat s’il existe, sinon vide
     */
    @Override
    public Optional<AcademicResult> findBySigle(String sigle) {
        if (sigle == null || sigle.isBlank()) {
            return Optional.empty();
        }

        return data.stream()
                .filter(r -> sigle.equalsIgnoreCase(r.getSigle()))
                .findFirst();
    }

    /**
     * Vide complètement le stockage en mémoire.
     * Utile pour réinitialiser l’état entre deux tests.
     */
    public void clear() {
        data.clear();
    }
}
