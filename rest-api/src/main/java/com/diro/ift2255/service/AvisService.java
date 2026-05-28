package com.diro.ift2255.service;

import com.diro.ift2255.Repository.AvisRepository;
import com.diro.ift2255.enums.*;
import com.diro.ift2255.model.Avis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * service centralisant toute la logique métier liée à la gestion des avis.
 *
 * règles importantes :
 *   - Un avis doit être valide pour être enregistré.
 *   - Un cours doit avoir au moins 5 avis pour pouvoir être affiché.
 *   - Les filtres ne doivent s'appliquer que si la règle des 5 avis est respectée.
 */

public class AvisService {

    private final AvisRepository repo;
    private final CourseService courseService;

    public AvisService(AvisRepository repo ,CourseService courseService) {
        this.repo = repo;
        this.courseService = courseService;
    }


    /**
     * Soumettre un avis si valide
     * @param avis l'avis à soumettre
     * @return true si l'avis a été soumis, false sinon
     */
    public boolean soumettreAvis(Avis avis) {
        // avis null
        if (avis == null) {
            return false;
        }

        // validation de l'avis (sigle vide, champs manquants, etc.)
        if (!avis.estValide()) {
            return false;
        }

        // validation métier : le cours doit exister
        if (courseService.getCourseById(avis.getCoursCode()).isEmpty()) {
            return false;
        }
        // sauvegarde
        repo.save(avis);
        return true;
    }


    /**
     * Vérifie si le nombre d'avis pour un cours est atteint.
     * @param coursCode Le sigle du cours à vérifier.
     * @return true si le cours possède au moins 5 avis, false sinon.
     */
    public boolean hasMinimumAvis(String coursCode) {
        return repo.countAvisForCours(coursCode) >= 5;
    }



    /**
     * Obtenir les avis d’un cours
     * @param coursCode Le sigle du cours.
     * @return La liste complète des avis trouvés.
     */
    public List<Avis> getAvisCours(String coursCode) {
        return repo.findByCours(coursCode);
      }


     /**
     * Retourne les avis d'un cours uniquement s'il en a au moins 5.
     * Sinon retourne une liste vide.
     * @param coursCode Le sigle du cours.
     * @return Une liste d'avis ou une liste vide si le seuil de 5 n'est pas atteint.
     */
    public List<Avis> getAvisAffichables(String coursCode) {

        if (!hasMinimumAvis(coursCode)) {
            return new ArrayList<>();
        }
        return getAvisCours(coursCode);
    }
   

    /**
     *  @return Filtre les avis d'un cours uniquement si la règle des 5 avis est respectée.
     * Sinon retourne une liste vide.
     */
    public List<Avis> filtrerAvisAffichables(String coursCode,NiveauDifficulte difficulte,
        Charge charge,Trimestre session,Integer annee) {

        if (!hasMinimumAvis(coursCode)) {
            return new ArrayList<>();
        }

        return filtrerAvis(coursCode, difficulte, charge,session, annee);
    }


    
    /**
     * Exécute l'algorithme de filtrage multicritères.
     * @param coursCode Le sigle du cours.
     * @param difficulte Niveau de difficulté recherché.
     * @param charge Charge de travail recherchée.
     * @param session Session recherchée.
     * @param annee Année recherchée.
     * @return Liste résultante correspondant à tous les critères non-nuls fournis.
     */
    public List<Avis> filtrerAvis(String coursCode,
                              NiveauDifficulte difficulte,
                              Charge charge,
                              Trimestre session,
                              Integer annee) {

        List<Avis> avisList = repo.findByCours(coursCode);

        List<Avis> result = new ArrayList<>();

        
        for (Avis avis : avisList) {

            // filtre difficulté
            if (difficulte != null && avis.getDifficulte() != difficulte)
                continue;

            // filtre charge de travail
            if (charge != null && avis.getChargeTravail() != charge)
                continue;

            // filtre session
            if (session != null && avis.getSession() != session)
                continue;

            // filtre année
            if (annee != null && !annee.equals(avis.getAnnee())) 
                continue;
            result.add(avis);
        }
        return result;
    }
    /**
     * Avis exploitables pour le calcul de la charge de travail.
     * Centralise la règle des 5 avis.
     */
    public List<Avis> getAvisChargeExploitables(String coursCode) {

        if (!hasMinimumAvis(coursCode)) {
            return new ArrayList<>();
        }

        // 2. Récupération et filtrage des données nulles
        return repo.findByCours(coursCode).stream()
            .filter(a -> a.getChargeTravail() != null)
            .collect(Collectors.toList());
    }

    
}