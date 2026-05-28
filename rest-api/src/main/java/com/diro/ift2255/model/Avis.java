package com.diro.ift2255.model;
import com.diro.ift2255.enums.*;

    /**
     * class Représente un avis laissé par un étudiant sur un cours.
     * Cette classe contient les évaluations de difficulté, de charge de travail 
     * ainsi que les commentaires textuels.
     */
public class Avis {

    private String coursCode;
    private NiveauDifficulte difficulte;
    private Charge chargeTravail;
    private String commentaire;
    private Trimestre session; 
    private Integer annee;


   /**
     * Constructeur complet pour créer un avis.
     * * @param coursCode      Le sigle du cours (ex: IFT2255).
     * @param difficulte     Le niveau de difficulté évalué.
     * @param chargeTravail  L'évaluation de la charge de travail.
     * @param commentaire    Le texte descriptif de l'avis.
     * @param session        La session durant laquelle le cours a été suivi.
     * @param annee          L'année de suivi du cours.
     */
    public Avis(String coursCode, NiveauDifficulte difficulte,
                Charge chargeTravail, String commentaire ,Trimestre session, Integer annee) {
        this.coursCode = coursCode;
        this.difficulte = difficulte;
        this.chargeTravail = chargeTravail;
        this.commentaire = commentaire;
        this.session = session;
        this.annee = annee;
    }

    // constructeur pour Jackson
    public Avis() {}

    /**
     * Vérifie la validité de l'avis 
     * Un avis est valide si le code du cours est présent et qu'au moins 
     * un critère d'évaluation (difficulté, charge ou session) est fourni.
     * * @return true si l'avis respecte les critères minimaux, false sinon.
     */
    public boolean estValide() {

        // Vérification obligatoire : coursCode
        if (coursCode == null || coursCode.isEmpty()) {
            return false;
        }

        // au moins un critère doit être fourni
        boolean Critere = (difficulte != null) || (chargeTravail != null) || (session != null);

        return Critere;
    }


    // Getters
    
    public String getCoursCode() { return coursCode; }
    public NiveauDifficulte getDifficulte() { return difficulte; }
    public Charge getChargeTravail() { return chargeTravail; }
    public String getCommentaire() { return commentaire; }
    public Trimestre getSession() { return session; }
    public Integer getAnnee() { return annee; }
    
}
