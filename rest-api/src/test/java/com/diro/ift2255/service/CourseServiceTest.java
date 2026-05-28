package com.diro.ift2255.service;

import com.diro.ift2255.Repository.FakeCourseRepository;
import com.diro.ift2255.model.Course;
import com.diro.ift2255.util.FakeHttpClientApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires du service {@link CourseService}.
 *
 * Vérifie le comportement métier du service en isolation :
 * - accès au cache local via {@link FakeCourseRepository}
 * - appels API simulés via {@link FakeHttpClientApi}
 *
 * L'objectif est de valider la logique sans dépendre
 * de l'API réelle ni du stockage disque.
 */
public class CourseServiceTest {

    private FakeHttpClientApi fakeApi;
    private FakeCourseRepository fakeRepo;
    private CourseService service;

    private Course mockCourse;

    /**
     * Initialise l'environnement de test avant chaque test :
     * - client HTTP factice
     * - repository en mémoire
     * - instance de CourseService
     * - cours fictif de référence
     */
    @BeforeEach
    void setup() {
        fakeApi = new FakeHttpClientApi();
        fakeRepo = new FakeCourseRepository();
        service = new CourseService(fakeApi, fakeRepo); 

        mockCourse = new Course();
        mockCourse.setId("IFT2255");
        mockCourse.setName("Génie Logiciel");
        mockCourse.setCredits(3.0);
    }

    // -------------------------------------------------------------
    // TEST 1 : Le service doit retourner un cours du cache local
    /**
     * Vérifie que {@link CourseService#getCourseById(String)}
     * retourne un cours présent dans le cache local
     * sans appeler l'API distante.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetCourseById_ReturnsLocalCourse() {
        // Arrange
        fakeRepo.save(mockCourse);

        // Act
        Optional<Course> result = service.getCourseById("IFT2255");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("IFT2255", result.get().getId());
    }

    // -------------------------------------------------------------
    // TEST 2 : Le cours n'est pas dans cache -> API appelée
    /**
     * Vérifie que {@link CourseService#getCourseById(String)}
     * appelle l'API lorsque le cours n'est pas en cache,
     * puis sauvegarde le cours récupéré dans le repository.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetCourseById_FetchesFromApiWhenNotCached() {
        // Arrange
        fakeApi.nextResult = mockCourse; // réponse API

        // Act
        Optional<Course> result = service.getCourseById("IFT9999");

        // Assert
        assertTrue(result.isPresent());
        assertTrue(fakeRepo.findById("IFT2255").isPresent()); // sauvegardé
    }

    // -------------------------------------------------------------
    // TEST 3 : getAllCourses() retourne une liste de cours

    /**
     * Vérifie que {@link CourseService#getAllCourses(Map)}
     * retourne correctement la liste de cours fournie
     * par l'API simulée.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetAllCourses_ReturnsListFromApi() {
        // Arrange
        List<Course> list = List.of(mockCourse);
        fakeApi.nextResult = list;

        // Act
        List<Course> result = service.getAllCourses(new HashMap<>());

        // Assert
        assertEquals(1, result.size());
        assertEquals("IFT2255", result.get(0).getId());
    }

    // -------------------------------------------------------------
    // TEST 4 : Récupérer la liste des prérequis

    /**
     * Vérifie que {@link CourseService#getPrerequisites(String)}
     * retourne la liste des prérequis d'un cours
     * lorsque l'API fournit des données valides.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetPrerequisites() {
        // Arrange
        fakeApi.nextResult = List.of("MAT1720", "IFT2015");

        // Act
        List<String> result = service.getPrerequisites("IFT2255");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains("MAT1720"));
        assertTrue(result.contains("IFT2015"));
    }

    // -------------------------------------------------------------
    // TEST 5 : Récupérer l'horaire d’un cours pour un trimestre donné

    /**
     * Vérifie que {@link CourseService#getCourseScheduleForSemester(String, String)}
     * retourne un cours avec une liste d'horaires non nulle,
     * même si l'API retourne des données incomplètes.
     *
     * @param aucun
     * @return aucun (assertions internes)
     */
    @Test
    void testGetCourseScheduleForSemester_ReturnsCourseWithNonNullSchedules() {
    // Arrange
    Course courseWithNoSchedule = new Course();
    courseWithNoSchedule.setId("IFT2255");
    courseWithNoSchedule.setSchedules(null); // simulons une API incomplète

    fakeApi.nextResult = courseWithNoSchedule;

    // Act
    Optional<Course> result =
            service.getCourseScheduleForSemester("IFT2255", "H25");

    // Assert
    assertTrue(result.isPresent());
    assertNotNull(result.get().getSchedules());
    }
}