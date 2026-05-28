package com.diro.ift2255.service;

import java.util.List;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.model.User;

/**
 * Service pour vérifier si un étudiant est éligible à un cours
 * en fonction des prérequis et de son profil.
 */
public class CourseEligibilityService {

    /**
     * Détermine si un étudiant peut suivre un cours.
     *
     * @param user   Étudiant (cycle, cours complétés)
     * @param course Cours ciblé
     * @return true si l'étudiant satisfait les prérequis
     */
    public boolean isEligible(User user, Course course) {

        if (user == null || course == null) {
            throw new IllegalArgumentException("User ou cours manquant.");
        }

        List<String> prereqs = course.getPrerequisiteCourses();
        List<String> completed = user.getCompletedCourses();

        // Si aucun prérequis
        if (prereqs == null || prereqs.isEmpty()) {
            return true;
        }

        // Si l'étudiant n'a rien complété
        if (completed == null || completed.isEmpty()) {
            return false;
        }

        // Vérifier chaque prérequis
        for (String p : prereqs) {
            if (!completed.contains(p)) {
                return false;
            }
        }
        return true;
    }
}