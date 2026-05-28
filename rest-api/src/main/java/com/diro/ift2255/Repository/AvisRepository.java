package com.diro.ift2255.Repository;

import com.diro.ift2255.model.Avis;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository gérant la persistance des avis dans un fichier JSON.
 */
public class AvisRepository {
    
    // objet Jackson pour sérialiser/désérialiser le JSON
    private final ObjectMapper mapper = new ObjectMapper();

    // chemin du fichier où les avis sont stockés
    private final String filePath;

    /**
     * Constructeur utilisé par l'application.
     * Le fichier final sera : data/Avis.json
     */
    public AvisRepository() {
        this.filePath = "data/Avis.json";
    }

    // constructeur utilisé uniquement pour les tests
    public AvisRepository(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Charge tous les avis à partir du fichier JSON.
     * * @return Une liste contenant tous les avis chargés, ou une liste vide en cas d'erreur ou de fichier absent.
     */
    private List<Avis> loadAll() {
        try {
            File file = new File(filePath);

            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }

            return mapper.readValue(file,new TypeReference<List<Avis>>() {});

        } catch (Exception e) {
            System.err.println("Erreur chargement avis.json : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /** * Sauvegarde la liste complète des avis dans le fichier JSON.
     * Crée automatiquement le dossier parent si nécessaire.
     * * @param avisList La liste d'avis 
     */
     private void saveAll(List<Avis> avisList) {
        try {
            File file = new File(filePath);

            // Crée le dossier "data" s'il n'existe pas
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, avisList);
        
        } catch (Exception e) {
            System.err.println("Erreur sauvegarde avis.json : " + e.getMessage());
        }
    }

    /**
     * Ajoute et sauvegarde un nouvel avis dans la base de données.
     * * @param avis L'objet Avis à enregistrer.
     */
    public void save(Avis avis) {
        List<Avis> all = loadAll();
        all.add(avis);
        saveAll(all);
    }

    /**
     * Récupère les avis stockés.
     * * @return Une liste de tous les objets Avis.
     */
    public List<Avis> findAll() {
        return loadAll();
    }


    /**
     * Recherche les avis associés à un cours spécifique.
     * * @param coursCode Le sigle du cours (ex: IFT2255).
     * @return Une liste d'avis filtrée par le code du cours.
     */
    public List<Avis> findByCours(String coursCode) {
        List<Avis> all = loadAll();
        List<Avis> result = new ArrayList<>();

        for (Avis a : all) {
            if (a.getCoursCode().equalsIgnoreCase(coursCode)) {
                result.add(a);
            }
        }
        return result;
    }  

     /**
     * Calcule le nombre d'avis existants pour un cours donné.
     * * @param coursCode Le sigle du cours.
     * @return Le nombre total d'avis trouvés pour ce cours.
     */
    public int countAvisForCours(String coursCode) {
        int count = 0;
        for (Avis a : loadAll()) {
            if (a.getCoursCode().equalsIgnoreCase(coursCode)) {
                count++;
            }
        }
        return count;
    }
}
