package com.diro.ift2255.service;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.Repository.CourseRepository;
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.util.*;

/**
 * Service responsable de la logique métier liée aux cours.
 * Il gère l’accès aux cours via l’API externe et le cache local.
 */
public class CourseService {

    private final HttpClientApi clientApi;
    private final CourseRepository repo;

    private static final String BASE_URL = "https://planifium-api.onrender.com/api/v1/courses";

    public CourseService(HttpClientApi clientApi) {
        this.clientApi = clientApi;
        this.repo = new CourseRepository();
    }

    public CourseService(HttpClientApi api, CourseRepository repo) {
        this.clientApi = api;
        this.repo = repo;
    }
    

    /**
     * Récupère la liste des cours depuis l’API,
     * avec possibilité de filtrage via des paramètres de requête.
     *
     * @param queryParams paramètres de requête (ex : nom, sigle, etc.)
     * @return liste des cours correspondant aux paramètres
     */
    public List<Course> getAllCourses(Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;

        URI uri = HttpClientApi.buildUri(BASE_URL, params);

        return clientApi.get(uri, new TypeReference<List<Course>>() {});
    }

    /**
     * Recherche un cours par son sigle.
     * La recherche se fait d’abord dans le cache local,
     * puis via l’API si nécessaire.
     *
     * @param courseId sigle du cours (ex : IFT2255)
     * @return un Optional contenant le cours s’il est trouvé, sinon vide
     */
    public Optional<Course> getCourseById(String courseId) {

        // 1. Vérifier dans le cache local
        Optional<Course> localCourse = repo.findById(courseId);
        if (localCourse.isPresent()) {
            return localCourse;
        }

        // 2. Aller chercher à l’API
        Optional<Course> apiCourse = getFullCourseDetails(courseId);

        // 3. Sauvegarder si trouvé
        apiCourse.ifPresent(repo::save);

        return apiCourse;
    }

    /**
     * Récupère les informations complètes d’un cours depuis l’API,
     * incluant l’horaire, les prérequis, les équivalences et les concomitants.
     *
     * @param courseId sigle du cours
     * @return un Optional contenant le cours avec tous ses détails, sinon vide
     */
    public Optional<Course> getFullCourseDetails(String courseId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("include_schedule", "true");
            params.put("include_equivalents", "true");
            params.put("include_prerequisites", "true");

            URI uri = HttpClientApi.buildUri(BASE_URL + "/" + courseId, params);

            Course course = clientApi.get(uri, Course.class);

            // Certains cours retournent prerequisite_courses seulement séparément
            if (course.getPrerequisiteCourses() == null) {
                List<String> prereq = getPrerequisites(courseId);
                course.setPrerequisiteCourses(prereq);
            }

            // Si équivalences absentes → mettre liste vide (évite null dans ton JSON)
            if (course.getEquivalentCourses() == null) {
                course.setEquivalentCourses(new ArrayList<>());
            }

            // Même chose pour concomitants
            if (course.getConcomitantCourses() == null) {
                course.setConcomitantCourses(new ArrayList<>());
            }

            return Optional.of(course);

        } catch (Exception e) {
            // NE PAS throw : Javalin doit gérer l'erreur
            return Optional.empty();
        }
    }

    /**
     * Récupère uniquement la liste des prérequis d’un cours donné.
     *
     * @param courseId sigle du cours
     * @return liste des sigles des cours prérequis
     */
    public List<String> getPrerequisites(String courseId) {
        try {
            URI uri = URI.create(BASE_URL + "/" + courseId + "/prerequisites");
            return clientApi.get(uri, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    /**
     * Récupère l’horaire d’un cours pour un trimestre donné.
     *
     * @param courseId sigle du cours
     * @param semester trimestre ciblé (ex : H25, A24)
     * @return un Optional contenant le cours avec son horaire pour le trimestre,
     *         sinon vide
     */
    public Optional<Course> getCourseScheduleForSemester(String courseId, String semester) {

        if (courseId == null || courseId.isBlank()
                || semester == null || semester.isBlank()) {
            return Optional.empty();
        }

        try {
            Map<String, String> params = new HashMap<>();
            params.put("include_schedule", "true");
            params.put("schedule_semester", semester.toLowerCase());

            URI uri = HttpClientApi.buildUri(
                    BASE_URL + "/" + courseId,
                    params
            );

            Course course = clientApi.get(uri, Course.class);

            // Sécurité : éviter null dans la vue
            if (course.getSchedules() == null) {
                course.setSchedules(new ArrayList<>());
            }

            return Optional.of(course);

        } catch (Exception e) {
            // Exigence du devoir : ne jamais crasher
            return Optional.empty();
        }
    }

}