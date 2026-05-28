package com.diro.ift2255.Repository;

import com.diro.ift2255.model.Course;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

/**
 * Repository responsable de la gestion locale des cours.
 * Les cours sont stockés et récupérés depuis un fichier JSON.
 */
public class CourseRepository {

    private static final String FILE_PATH = "data/courses.json";
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Charge tous les cours depuis le fichier JSON.
     * Si le fichier n'existe pas ou s'il est vide,
     * on retourne une map vide.
     */
    private Map<String, Course> loadAll() {
        try {
            File file = new File(FILE_PATH);

            // Dossier data/ absent -> on retourne une map vide
            if (!file.exists()) {
                return new HashMap<>();
            }

            // Le fichier existe mais est vide
            if (file.length() == 0) {
                return new HashMap<>();
            }

            // Lecture du JSON complet
            return mapper.readValue(
                    file,
                    new TypeReference<Map<String, Course>>() {}
            );

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de courses.json : " + e.getMessage());
            // JSON corrompu → ne pas faire crasher l’application
            return new HashMap<>();
        }
    }

    /**
     * Sauvegarde l'ensemble de la Map<String, Course>
     * dans le fichier JSON en format lisible.
     */
    private void saveAll(Map<String, Course> data) {
        try {
            File file = new File(FILE_PATH);

            // Créer le dossier data/ si absent
            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier JSON : " + e.getMessage());
        }
    }

    /**
     * Retourne un cours à partir de son sigle.
     *
     * @param id sigle du cours
     * @return un Optional contenant le cours s’il existe, sinon vide
     */
    public Optional<Course> findById(String id) {
        Map<String, Course> all = loadAll();
        return Optional.ofNullable(all.get(id));
    }

    /**
     * Sauvegarde ou met à jour un cours dans le stockage local.
     *
     * @param course cours à sauvegarder
     */
    public void save(Course course) {
        Map<String, Course> all = loadAll(); // Charger tout
        all.put(course.getId(), course);     // Ajouter / remplacer
        saveAll(all);                        // Sauvegarder tout
    }

    /**
     * Retourne tous les cours stockés localement.
     *
     * @return une map associant le sigle du cours à l’objet Course
     */
    public Map<String, Course> findAll() {
        return loadAll();
    }

    /**
     * Supprime un cours du stockage local.
     *
     * @param id sigle du cours à supprimer
     * @return true si le cours a été supprimé, false sinon
     */
    public boolean delete(String id) {
        Map<String, Course> all = loadAll();
        if (all.remove(id) != null) {
            saveAll(all);
            return true;
        }
        return false;
    }

    /**
     * Recherche tous les cours dont le sigle commence par un préfixe donné.
     *
     * @param prefix préfixe du sigle (ex. "IFT")
     * @return liste des cours correspondant au préfixe
     */
    public List<Course> findByPrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return List.of();
        }
    
        String upper = prefix.toUpperCase();
        Map<String, Course> all = loadAll();
    
        return all.values().stream()
                .filter(c -> c.getId() != null)
                .filter(c -> c.getId().startsWith(upper))
                .toList();
    }
}
