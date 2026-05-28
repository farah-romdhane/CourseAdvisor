package com.diro.ift2255.model;

/**
 * Représente les résultats académiques agrégés d’un cours
 * (issus du fichier CSV historique).
 */
public class AcademicResult {

    // Code du cours (ex: IFT2255)
    private String sigle;

    // Nom complet du cours
    private String nom;

    // Moyenne littérale (A+, A, A-, B+, etc.)
    private String moyenne;

    // Score de réussite (1 = faible, 5 = élevé)
    private double score;

    // Nombre total d’étudiants
    private int participants;

    // Nombre de trimestres offerts
    private int trimestres;

    // Constructeur vide (Jackson / utilitaires)
    public AcademicResult() {}

    /**
     * Construit un résultat académique complet pour un cours.
     *
     * @param sigle code du cours
     * @param nom nom complet du cours
     * @param moyenne moyenne littérale obtenue
     * @param score score numérique de réussite
     * @param participants nombre total d’étudiants
     * @param trimestres nombre de trimestres observés
     */
    public AcademicResult(
            String sigle,
            String nom,
            String moyenne,
            double score,
            int participants,
            int trimestres
    ) {
        this.sigle = sigle;
        this.nom = nom;
        this.moyenne = moyenne;
        this.score = score;
        this.participants = participants;
        this.trimestres = trimestres;
    }

    // ---------- GETTERS & SETTERS ----------

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(String moyenne) {
        this.moyenne = moyenne;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public int getTrimestres() {
        return trimestres;
    }

    public void setTrimestres(int trimestres) {
        this.trimestres = trimestres;
    }
}
