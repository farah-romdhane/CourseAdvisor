package com.diro.ift2255.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.diro.ift2255.Repository.AvisRepository;
import com.diro.ift2255.enums.*;
import com.diro.ift2255.model.Avis;


/**
 * Tests unitaires pour AvisService.
 * 
 *important:
 * Les tests utilisent un fichier JSON temporaire afin de ne jamais modifier
 * la vraie: data/Avis.json.
 */
public class AvisServiceTest {

    private AvisService service;
    private AvisRepository repo;
    private File tempFile;

    /**
     * préparation d’un environnement isolé avant chaque test :
     * - création d'un fichier temporaire vide
     * - création d'un repository pointant vers ce fichier
     */

    @BeforeEach
    void setUp() throws Exception {
        tempFile = File.createTempFile("avis_test_service", ".json");
        tempFile.deleteOnExit();
        Files.writeString(tempFile.toPath(), "[]");

        repo = new AvisRepository(tempFile.getAbsolutePath());
        
        CourseService fakeCourseService = new FakeCourseService();
        service = new AvisService(repo, fakeCourseService);

    }
    // FONCTIONNALITÉ 1 – Soumettre un avis
    @Test
    void soumettreAvis_AvisValide_RetourneTrue() {
        Avis avis = new Avis("IFT2255", NiveauDifficulte.MOYEN, Charge.MOYEN, "Bon", Trimestre.HIVER, 2025);
        assertTrue(service.soumettreAvis(avis));
    }

    @Test
    void soumettreAvis_CoursInexistant_RetourneFalse() {
        Avis avis = new Avis("MAT999", NiveauDifficulte.MOYEN, Charge.MOYEN, "Note", Trimestre.HIVER, 2025);
        assertFalse(service.soumettreAvis(avis));
    }

    @Test
    void soumettreAvis_SigleVide_RetourneFalse() {
        Avis avis = new Avis("", NiveauDifficulte.MOYEN, Charge.MOYEN, "Note", Trimestre.HIVER, 2025);
        assertFalse(service.soumettreAvis(avis));
    }

    @Test
    void soumettreAvis_DonneesInvalides_RetourneFalse() {
        Avis avis = new Avis("IFT2255", null, null, "Commentaire", null, 2025);
        assertFalse(service.soumettreAvis(avis));
    }

    @Test
    void soumettreAvis_AvisValide_EstSauvegarde() {
        Avis avis = new Avis("IFT2255", NiveauDifficulte.FACILE, Charge.MOYEN, "Ok", Trimestre.HIVER, 2025);
        service.soumettreAvis(avis);
        assertEquals(1, repo.findAll().size());
    }

    // FONCTIONNALITÉ 2 – Consultation des avis
    @Test
    void consulterAvis_MoinsDeCinqAvis_RetourneListeVide() {
        for (int i = 0; i < 3; i++) {
            repo.save(new Avis("IFT2255", NiveauDifficulte.MOYEN, Charge.MOYEN, "Note", Trimestre.HIVER, 2025));
        }
        assertTrue(service.getAvisAffichables("IFT2255").isEmpty());
    }

    @Test
    void consulterAvis_CinqAvis_RetourneTousLesAvis() {
        for (int i = 0; i < 5; i++) {
            repo.save(new Avis("IFT2255", NiveauDifficulte.MOYEN, Charge.MOYEN, "Note", Trimestre.HIVER, 2025));
        }
        assertEquals(5, service.getAvisAffichables("IFT2255").size());
    }

    @Test
    void filtrerAvis_ParDifficulte_RetourneAvisCorrespondants() {
        for (int i = 0; i < 4; i++)
            repo.save(new Avis("IFT2255", NiveauDifficulte.FACILE, Charge.MOYEN, "Note", Trimestre.HIVER, 2025));

        repo.save(new Avis("IFT2255", NiveauDifficulte.DIFFICILE, Charge.MOYEN, "Cible", Trimestre.HIVER, 2025));

        List<Avis> res = service.filtrerAvisAffichables(
            "IFT2255",
            NiveauDifficulte.DIFFICILE,
            null,
            null,
            null
        );

        assertEquals(1, res.size());
        assertEquals(NiveauDifficulte.DIFFICILE, res.get(0).getDifficulte());
    }

    @Test
    void filtrerAvis_AnneeSansAvis_RetourneListeVide() {
        for (int i = 0; i < 5; i++)
            repo.save(new Avis("IFT2255", NiveauDifficulte.MOYEN, Charge.MOYEN, "Note", Trimestre.HIVER, 2025));

        List<Avis> res = service.filtrerAvisAffichables(
            "IFT2255",
            null,
            null,
            null,
            1990
        );

        assertTrue(res.isEmpty());
    }

    @Test
    void filtrerAvis_SansFiltres_RetourneTousLesAvisAffichables() {
        for (int i = 0; i < 5; i++)
            repo.save(new Avis("IFT2255", NiveauDifficulte.MOYEN, Charge.MOYEN, "Note", Trimestre.HIVER, 2025));

        List<Avis> res = service.filtrerAvisAffichables(
            "IFT2255",
            null,
            null,
            null,
            null
        );

        assertEquals(5, res.size());
    }

    
}

