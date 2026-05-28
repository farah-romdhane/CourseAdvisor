package com.diro.ift2255.service;

import com.diro.ift2255.model.Course;

import java.util.Optional;
/**
 * Fake CourseService pour les tests unitaires.
 * Simule un annuaire minimal de cours :
 * - IFT2255 existe
 * - Tous les autres cours sont considérés inexistants
 */
public class FakeCourseService extends CourseService {
    
    /**
     * Constructeur du service factice.
     * Initialise le service sans accès réel à l’API.
     */
    public FakeCourseService() {
        super(null); // OK car on n'utilise pas le repo dans les tests
    }

    /**
     * Recherche un cours par son sigle.
     * Seul le cours "IFT2255" est considéré comme existant.
     *
     * @param courseId sigle du cours
     * @return un Optional contenant le cours si le sigle est "IFT2255",
     *         sinon un Optional vide
     */
    @Override
    public Optional<Course> getCourseById(String courseId) {

        // cas invalides
        if (courseId == null || courseId.isBlank()) {
            return Optional.empty();
        }

        // seul cours "existant"
        if (courseId.equals("IFT2255")) {
            Course c = new Course();
            c.setId("IFT2255");
            c.setName("Génie logiciel");
            c.setCredits(3.0);
            return Optional.of(c);
        }

        // tous les autres n'existent pas
        return Optional.empty();
    }
}