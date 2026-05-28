package com.diro.ift2255.service;

import com.diro.ift2255.Repository.FakeResultatAcademiqueRepository;
import com.diro.ift2255.model.AcademicResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires du service {@link ResultatAcademiqueService}.
 *
 * Ces tests valident la logique métier liée aux résultats académiques
 * en utilisant un {@link FakeResultatAcademiqueRepository} afin de
 * simuler le stockage des données en mémoire, sans accès au fichier CSV.
 */
public class ResultatAcademiqueServiceTest {

    private FakeResultatAcademiqueRepository fakeRepo;
    private ResultatAcademiqueService service;

    private AcademicResult mockResult;

    /**
     * Initialise l'environnement de test avant chaque exécution :
     * - repository factice en mémoire
     * - instance du service à tester
     * - résultat académique simulé
     */
    @BeforeEach
    void setup() {
        fakeRepo = new FakeResultatAcademiqueRepository();
        service = new ResultatAcademiqueService(fakeRepo);

        mockResult = new AcademicResult();
        mockResult.setSigle("IFT2255");
        mockResult.setMoyenne("3.4");
        mockResult.setScore(4);
    }

    // TEST 1 : Résultat trouvé pour un cours existant
    /**
     * Vérifie que {@link ResultatAcademiqueService#getResultatsParCours(String)}
     * retourne un résultat académique lorsqu'un cours existe
     * dans le repository.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetResultatsParCours_ReturnsResultWhenExists() {
        // Arrange
        fakeRepo.save(mockResult);

        // Act
        Optional<AcademicResult> result =
                service.getResultatsParCours("IFT2255");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("IFT2255", result.get().getSigle());
    }

    // TEST 2 : hasResultats retourne true si résultats présents
    /**
     * Vérifie que {@link ResultatAcademiqueService#hasResultats(String)}
     * retourne {@code true} lorsqu'un cours possède des résultats
     * académiques enregistrés.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testHasResultats_ReturnsTrueWhenResultExists() {
        // Arrange
        fakeRepo.save(mockResult);

        // Act
        boolean exists = service.hasResultats("IFT2255");

        // Assert
        assertTrue(exists);
    }

    // TEST 3 : getTousLesResultats retourne la liste complète
    /**
     * Vérifie que {@link ResultatAcademiqueService#getTousLesResultats()}
     * retourne la liste complète des résultats académiques
     * présents dans le repository.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetTousLesResultats_ReturnsAllResults() {
        // Arrange
        fakeRepo.save(mockResult);

        // Act
        List<AcademicResult> results =
                service.getTousLesResultats();

        // Assert
        assertEquals(1, results.size());
        assertEquals("IFT2255", results.get(0).getSigle());
    }

    // TEST 4 : sigle inexistant -> Optional vide
    /**
     * Vérifie que {@link ResultatAcademiqueService#getResultatsParCours(String)}
     * retourne un {@link Optional} vide lorsque le sigle du cours
     * n'existe pas dans le repository.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetResultatsParCours_ReturnsEmptyWhenNotFound() {
        // Act
        Optional<AcademicResult> result =
                service.getResultatsParCours("IFT9999");

        // Assert
        assertTrue(result.isEmpty());
    }

    // TEST 5 : sigle null -> Optional vide
    /**
     * Vérifie que {@link ResultatAcademiqueService#getResultatsParCours(String)}
     * retourne un {@link Optional} vide lorsque le sigle fourni est nul.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetResultatsParCours_ReturnsEmptyWhenSigleNull() {
        // Act
        Optional<AcademicResult> result =
                service.getResultatsParCours(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    // TEST 6 : hasResultats retourne false si aucun résultat
    /**
     * Vérifie que {@link ResultatAcademiqueService#hasResultats(String)}
     * retourne {@code false} lorsqu'aucun résultat académique
     * n'est disponible pour le cours demandé.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testHasResultats_ReturnsFalseWhenNoResult() {
        // Act
        boolean exists = service.hasResultats("IFT2255");

        // Assert
        assertFalse(exists);
    }
}

