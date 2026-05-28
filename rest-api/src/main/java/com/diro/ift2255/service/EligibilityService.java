package com.diro.ift2255.service;

import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Service responsable de la vérification de l’éligibilité
 * d’un cours pour un programme donné via l’API externe.
 */
public class EligibilityService {

    private static final String BASE_URL =
        "https://planifium-api.onrender.com/api/v1/courseplan/eligible-courses";

    private final HttpClientApi client;
    private final ObjectMapper mapper = new ObjectMapper();

    public EligibilityService(HttpClientApi client) {
        this.client = client;
    }

    /**
     * Vérifie si un cours est éligible pour un programme donné,
     * en fonction des cours déjà complétés par l’étudiant.
     *
     * @param programId identifiant du programme
     * @param courseToCheck sigle du cours à vérifier
     * @param completedCourses liste des cours déjà complétés
     * @return true si le cours est éligible, false sinon
     */
    public boolean isCourseEligible(
            String programId,
            String courseToCheck,
            List<String> completedCourses
    ) {
        try {
            Map<String, Object> payload = Map.of(
                    "program_id", programId,
                    "completed_courses", completedCourses
            );

            String jsonBody = mapper.writeValueAsString(payload);

            var response = client.post(URI.create(BASE_URL), jsonBody);

            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                return false;
            }

            JsonNode root = mapper.readTree(response.getBody());
            JsonNode eligibleCourses = root.get("eligible_courses");

            if (eligibleCourses == null || !eligibleCourses.isArray()) {
                return false;
            }

            for (JsonNode c : eligibleCourses) {
                if (courseToCheck.equalsIgnoreCase(c.get("id").asText())) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false; // exigence : ne jamais crasher
        }
    }
}
