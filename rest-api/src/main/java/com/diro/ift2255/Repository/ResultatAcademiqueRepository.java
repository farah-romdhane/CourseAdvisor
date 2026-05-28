package com.diro.ift2255.Repository;

import com.diro.ift2255.model.AcademicResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsable du chargement et de l'accès
 * aux résultats académiques depuis un fichier CSV.
 *
 * Le fichier CSV est chargé une seule fois en mémoire (cache)
 * afin d'éviter des lectures répétées sur le disque.
 */
public class ResultatAcademiqueRepository {

     private static final String FILE_PATH = "data/historique_cours_prog_117510.csv" ;


    // Cache mémoire
    private List<AcademicResult> cache;

    /**
     * Charge les résultats académiques depuis le fichier CSV
     * uniquement si le cache mémoire est vide.
     *
     * @return liste des résultats académiques chargés en mémoire
     */
    private List<AcademicResult> loadIfNeeded() {
        if (cache == null) {
            cache = loadCsv();
        }
        return cache;
    }

    /**
     * Lit entièrement le fichier CSV des résultats académiques
     * et convertit chaque ligne en objet {@link AcademicResult}.
     *
     * @return liste des résultats académiques lus depuis le CSV
     */
    private List<AcademicResult> loadCsv() {
        List<AcademicResult> results = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {

                // Ignorer l'en-tête
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                // Split CSV qui respecte les virgules dans les guillemets
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (parts.length < 6) continue;

                AcademicResult r = new AcademicResult(
                        parts[0].trim(),                         // sigle
                        parts[1].replace("\"", "").trim(),       // nom (enlever guillemets)
                        parts[2].trim(),                         // moyenne (B, B+, A-)
                        Double.parseDouble(parts[3].trim()),     // score
                        Integer.parseInt(parts[4].trim()),       // participants
                        Integer.parseInt(parts[5].trim())        // trimestres
                );

                results.add(r);
            }

        } catch (Exception e) {
            System.err.println(
                "Erreur lors du chargement du CSV des résultats académiques : "
                + e.getMessage()
            );
        }

        return results;
    }

    /**
     * Retourne l’ensemble des résultats académiques disponibles.
     *
     * @return liste de tous les résultats académiques (copie défensive)
     */
    public List<AcademicResult> findAll() {
        return new ArrayList<>(loadIfNeeded()); // copie défensive
    }

    /**
     * Recherche les résultats académiques correspondant à un cours donné.
     *
     * @param sigle sigle du cours (ex : IFT2255)
     * @return un Optional contenant le résultat si trouvé, sinon vide
     */
    public Optional<AcademicResult> findBySigle(String sigle) {
        if (sigle == null || sigle.isBlank()) {
            return Optional.empty();
        }

        return loadIfNeeded().stream()
                .filter(r -> sigle.equalsIgnoreCase(r.getSigle()))
                .findFirst();
    }

    /**
     * Vide le cache (utile pour tests).
     */
    public void clearCache() {
        cache = null;
    }
}