package com.diro.ift2255.Repository;

import com.diro.ift2255.model.Course;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implémentation factice du repository de cours.
 * Utilisée exclusivement pour les tests unitaires,
 * sans accès au système de fichiers.
 */
public class FakeCourseRepository extends CourseRepository {

    private final Map<String, Course> data = new HashMap<>();

    /**
     * Recherche un cours par son sigle dans le stockage en mémoire.
     *
     * @param id sigle du cours
     * @return un Optional contenant le cours s’il existe, sinon vide
     */
    @Override
    public Optional<Course> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    /**
     * Sauvegarde ou met à jour un cours dans le stockage en mémoire.
     *
     * @param course cours à sauvegarder
     */
    @Override
    public void save(Course course) {
        data.put(course.getId(), course);
    }

    /**
     * Retourne tous les cours stockés en mémoire.
     *
     * @return une map associant le sigle du cours à l’objet Course
     */
    @Override
    public Map<String, Course> findAll() {
        return data;
    }
}
