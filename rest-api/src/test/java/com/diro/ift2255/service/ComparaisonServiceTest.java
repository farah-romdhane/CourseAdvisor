package com.diro.ift2255.service;

import com.diro.ift2255.Repository.FakeCourseRepository;
import com.diro.ift2255.Repository.FakeResultatAcademiqueRepository;
import com.diro.ift2255.model.ComparaisonDesCours;
import com.diro.ift2255.model.ComparaisonEnsembleDesCours;
import com.diro.ift2255.model.Course;
import com.diro.ift2255.util.FakeHttpClientApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ComparaisonServiceTest {

    private ComparaisonService service;

    private void addAutumnSchedule(Course course) {
        Course.Schedule schedule = new Course.Schedule();
        schedule.setSemester("autumn");
        schedule.setSections(new ArrayList<>()); // vide = OK
        course.setSchedules(List.of(schedule));
    }

    @BeforeEach
    void setup() {

        // Fake course repo + service réel 
        FakeCourseRepository fakeCourseRepo = new FakeCourseRepository();
        CourseService courseService =
                new CourseService(new FakeHttpClientApi(), fakeCourseRepo);

        // Fake avis service 
        FakeAvisService fakeAvisService =
                new FakeAvisService(courseService);

        // Fake résultat académique repo + service réel
        FakeResultatAcademiqueRepository fakeResultRepo =
                new FakeResultatAcademiqueRepository();
        ResultatAcademiqueService resultatService =
                new ResultatAcademiqueService(fakeResultRepo);

        // Service à tester
        service = new ComparaisonService(
                courseService,
                fakeAvisService,
                resultatService
        );

       Map<String, Boolean> terms = new HashMap<>();
        terms.put("autumn", true);
        terms.put("winter", false);
        terms.put("summer", true);

        Course c1 = new Course();
        c1.setId("IFT2035");
        c1.setCredits(3.0);
        c1.setAvailableTerms(terms);
        c1.setPrerequisiteCourses(new ArrayList<>());
        addAutumnSchedule(c1);

        Course c2 = new Course();
        c2.setId("IFT2255");
        c2.setCredits(3.0);
        c2.setAvailableTerms(terms);
        c2.setPrerequisiteCourses(new ArrayList<>());
        addAutumnSchedule(c2);

        Course c3 = new Course();
        c3.setId("IFT1005");
        c3.setCredits(3.0);
        c3.setAvailableTerms(terms);
        c3.setPrerequisiteCourses(new ArrayList<>());
        addAutumnSchedule(c3);

        Course c4 = new Course();
        c4.setId("IFT1015");
        c4.setCredits(3.0);
        c4.setAvailableTerms(terms);
        c4.setPrerequisiteCourses(new ArrayList<>());
        addAutumnSchedule(c4);

        Course c5 = new Course();
        c5.setId("IFT2905"); // inexistant
        c5.setCredits(3.0);
        Map<String, Boolean> winterOnly = new HashMap<>();
        winterOnly.put("autumn", false);
        winterOnly.put("winter", true);
        winterOnly.put("summer", false);
        c5.setAvailableTerms(winterOnly);
        c5.setPrerequisiteCourses(new ArrayList<>());

        fakeCourseRepo.save(c1);
        fakeCourseRepo.save(c2);
        fakeCourseRepo.save(c3);
        fakeCourseRepo.save(c4);
        fakeCourseRepo.save(c5);


    }

    // -------------------------------------------------------------
    // TEST 1 : comparer 2 cours valides

    @Test
    void compare_deuxCours_valides_retourneComparaison() {

        // Arrange
        List<String> courses = List.of("IFT2035", "IFT2255");

        // Act
        ComparaisonDesCours result = service.compare(courses);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getCourses().size());
        assertTrue(result.getCourses().contains("IFT2035"));
        assertTrue(result.getCourses().contains("IFT2255"));
    }
    @Test
    void compare_unSeulCours_lanceException() {
        // Arrange
        List<String> courses = List.of("IFT2035");

        // Act + Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> service.compare(courses)
        );
    }

    @Test
    void compare_plusDeQuatreCours_lanceException() {
        // Arrange
        List<String> courses = List.of(
            "IFT2035", "IFT2255", "IFT1005", "IFT1015", "IFT1025"
        );

        // Act + Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> service.compare(courses)
        );
    }

    @Test
    void compare_coursInexistant_lanceException() {
        // Arrange
        List<String> courses = List.of("IFT2035", "IFT9999"); // inexistant

        // Act + Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> service.compare(courses)
        );
    }

    @Test
    void compare_listeCoursNull_lanceException() {

        assertThrows(
            IllegalArgumentException.class,
            () -> service.compare(null)
        );
    }

    @Test
    void compareEnsembles_deuxEnsemblesValides_retourneResultat() {

        // Arrange
        List<String> ensembleA = List.of("IFT2035", "IFT2255");
        List<String> ensembleB = List.of("IFT1005", "IFT1015");
        String session = "autumn";

        // Act
        ComparaisonEnsembleDesCours result =
                service.compareEnsembles(ensembleA, ensembleB, session);

        // Assert
        assertNotNull(result);

        assertEquals(ensembleA, result.getEnsembleA());
        assertEquals(ensembleB, result.getEnsembleB());
        assertEquals(session, result.getSession());

        assertTrue(result.getCreditsTotalA() > 0);
        assertTrue(result.getCreditsTotalB() > 0);
    }
    @Test
    void compareEnsembles_ensembleA_unSeulCours_lanceException() {
        List<String> ensembleA = List.of("IFT2035");
        List<String> ensembleB = List.of("IFT1005", "IFT1015");
        String session = "autumn";

        assertThrows(
            IllegalArgumentException.class,
            () -> service.compareEnsembles(ensembleA, ensembleB, session)
        );
    }

    @Test
    void compareEnsembles_ensembleB_plusDeSixCours_lanceException() {
        List<String> ensembleA = List.of("IFT2035", "IFT2255");
        List<String> ensembleB = List.of(
            "IFT1005", "IFT1015", "IFT2035", "IFT2255", "IFT3000", "IFT4000", "IFT5000"
        );
        String session = "autumn";

        assertThrows(
            IllegalArgumentException.class,
            () -> service.compareEnsembles(ensembleA, ensembleB, session)
        );
    }

    @Test
    void compareEnsembles_sessionNull_lanceException() {
        List<String> ensembleA = List.of("IFT2035", "IFT2255");
        List<String> ensembleB = List.of("IFT1005", "IFT1015");

        assertThrows(
            IllegalArgumentException.class,
            () -> service.compareEnsembles(ensembleA, ensembleB, null)
        );
    }

     @Test
    void compareEnsembles_coursNonOffertPourLaSession_lanceException() {
        List<String> ensembleA = List.of("IFT2035", "IFT2905"); // IFT2905 = hiver
        List<String> ensembleB = List.of("IFT1005", "IFT1015");
        String session = "autumn";

        assertThrows(
                IllegalArgumentException.class,
                () -> service.compareEnsembles(ensembleA, ensembleB, session)
        );
    }

}