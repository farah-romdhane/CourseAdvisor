package com.diro.ift2255.service;

import com.diro.ift2255.model.ProgramCoursesResponse;
import com.diro.ift2255.util.HttpClientApi;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsable de l’accès aux informations
 * liées aux programmes d’études via l’API externe.
 */
public class ProgramService {

    private static final String BASE_URL =
            "https://planifium-api.onrender.com/api/v1/programs";

    private final HttpClientApi api;

    public ProgramService(HttpClientApi api) {
        this.api = api;
    }

    /**
     * Récupère un programme ainsi que la liste détaillée
     * des cours qui lui sont associés.
     *
     * @param programId identifiant du programme
     * @return un Optional contenant le programme et ses cours s’ils existent,
     *         sinon un Optional vide
     */
    public Optional<ProgramCoursesResponse> getProgramWithCourses(String programId) {
        try {
            Map<String, String> params = Map.of(
                "programs_list", programId,
                "include_courses_detail", "true",
                "response_level", "min"
            );

            URI uri = HttpClientApi.buildUri(BASE_URL, params);

            ProgramCoursesResponse response =
                    api.get(uri, ProgramCoursesResponse.class);

            return Optional.of(response);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}