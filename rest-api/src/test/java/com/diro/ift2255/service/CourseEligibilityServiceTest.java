package com.diro.ift2255.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.model.User;

/**
 * Classe de tests unitaires pour la classe CourseEligibilityService.
 *
 * Cette classe valide le comportement de la méthode
 * en testant différents scénarios d'éligibilité basés sur les prérequis
 * d'un cours et les cours complétés par un étudiant.
 */

public class CourseEligibilityServiceTest {
    /**
     * Vérifie qu'un étudiant est éligible lorsque tous les prérequis
     * du cours ont été complétés.
     */

    @Test
    void testEligibleWhenAllPrereqCompleted() {
        User user = new User();
        user.setCompletedCourses(List.of("IFT1015", "IFT1025"));

        Course course = new Course();
        course.setPrerequisiteCourses(List.of("IFT1015", "IFT1025"));

        CourseEligibilityService svc = new CourseEligibilityService();

        assertTrue(svc.isEligible(user, course));
    }

    /**
     * Vérifie qu'un étudiant n'est pas éligible lorsqu'il manque au moins un prérequis requis par le cours.
     */
    @Test
    void testNotEligibleMissingOnePrereq() {
        User user = new User();
        user.setCompletedCourses(List.of("IFT1015"));

        Course course = new Course();
        course.setPrerequisiteCourses(List.of("IFT1015", "IFT1025"));

        CourseEligibilityService svc = new CourseEligibilityService();

        assertFalse(svc.isEligible(user, course));
    }

    /**
     * Vérifie qu'un étudiant est éligible lorsqu'un cours ne possède aucun prérequis.
     */

    @Test
    void testEligibleWhenNoPrereq() {
        User user = new User();
        user.setCompletedCourses(List.of()); 

        Course course = new Course();
        course.setPrerequisiteCourses(List.of()); // Aucun prérequis

        CourseEligibilityService svc = new CourseEligibilityService();

        assertTrue(svc.isEligible(user, course));
    }

    /**
     * Vérifie qu'un étudiant n'est pas éligible lorsqu'il n'a complété aucun cours alors que le cours cible
     * possède des prérequis. */
    @Test
    void testNotEligibleWhenStudentHasNoCompletedCourses() {
        User user = new User();
        user.setCompletedCourses(List.of()); // Aucun cours

        Course course = new Course();
        course.setPrerequisiteCourses(List.of("IFT2255"));

        CourseEligibilityService svc = new CourseEligibilityService();

        assertFalse(svc.isEligible(user, course));
    }

    /**
     * Vérifie qu'une exception est levée lorsque l'utilisateur fourni est nul.
     */
    @Test
    void testThrowsIfUserNull() {
        Course course = new Course();
        course.setPrerequisiteCourses(List.of("IFT1015"));

        CourseEligibilityService svc = new CourseEligibilityService();

        assertThrows(IllegalArgumentException.class, () -> {
            svc.isEligible(null, course);
        });
    }

     /**
     * Vérifie qu'une exception est levée lorsque le cours fourni est nul.
     */
    @Test
    void testThrowsIfCourseNull() {
        User user = new User();
        user.setCompletedCourses(List.of("IFT1015"));

        CourseEligibilityService svc = new CourseEligibilityService();

        assertThrows(IllegalArgumentException.class, () -> {
            svc.isEligible(user, null);
        });
    }

}
