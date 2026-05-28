package com.diro.ift2255.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Résultat de la comparaison de cours (entre 2 et 4 cours).
 */
public class ComparaisonDesCours {

    /** Liste des codes des cours comparés. */
    private List<String> courses;


    /** Liste des crédits associés à chaque cours. */
    private List<String> credits;

    /** Liste des prérequis associés à chaque cours. */
    private List<String> prerequis;

    /** Indique si le cours est offert à l'automne. */
    @JsonProperty("disponibleEnAutomne")
    private List<String> availableInAutumn;

    /** Indique si le cours est offert à l'hiver. */
    @JsonProperty("disponibleEnHiver")
    private List<String> availableInWinter;

    /** Indique si le cours est offert à l'été. */
    @JsonProperty("disponibleEnEte")
    private List<String> availableInSummer;

    /** Estimation de la charge de travail pour chaque cours. */
    @JsonProperty("chargeTravail")
    private List<String> chargeTravail;

    /** Estimation de la difficulté pour chaque cours. */
    @JsonProperty("difficulte")
    private List<String> difficulte;

    // ===== Getters / Setters =====

    /**
     * Retourne les crédits associés aux cours.
     *
     * @return liste des crédits
     */
    public List<String> getCourses() {
        return courses;
    }

    /**
     * Définit les crédits associés aux cours.
     *
     * @param courses liste des crédits
     */
    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    /**
     * Retourne les crédits associés aux cours.
     *
     * @return liste des crédits
     */
    public List<String> getCredits() {
        return credits;
    }

    /**
     * Définit les crédits associés aux cours.
     *
     * @param credits liste des crédits
     */
    public void setCredits(List<String> credits) {
        this.credits = credits;
    }
    /**
     * Retourne les prérequis associés aux cours.
     *
     * @return liste des prérequis
     */
    public List<String> getPrerequis() {
        return prerequis;
    }
    /**
     * Définit les prérequis associés aux cours.
     *
     * @param prerequis liste des prérequis
     */
    public void setPrerequis(List<String> prerequis) {
        this.prerequis = prerequis;
    }
    /**
     * Retourne les cours disponibles à la session d’automne.
     *
     * @return liste des cours disponibles en automne
     */
    public List<String> getAvailableInAutumn() {
        return availableInAutumn;
    }
    /**
     * Définit les cours disponibles à la session d’automne.
     *
     * @param availableInAutumn liste des cours
     */

    public void setAvailableInAutumn(List<String> availableInAutumn) {
        this.availableInAutumn = availableInAutumn;
    }

    /**
     * Retourne les cours disponibles à la session d’hiver.
     *
     * @return liste des cours disponibles en hiver
     */
    public List<String> getAvailableInWinter() {
        return availableInWinter;
    }

     /**
     * Définit les cours disponibles à la session d’hiver.
     *
     * @param availableInWinter liste des cours
     */
    public void setAvailableInWinter(List<String> availableInWinter) {
        this.availableInWinter = availableInWinter;
    }

    /**
     * Retourne les cours disponibles à la session d’été.
     *
     * @return liste des cours disponibles en été
     */
    public List<String> getAvailableInSummer() {
        return availableInSummer;
    }

    /**
     * Définit les cours disponibles à la session d’été.
     *
     * @param availableInSummer liste des cours
     */
    public void setAvailableInSummer(List<String> availableInSummer) {
        this.availableInSummer = availableInSummer;
    }

    /**
     * Retourne l’estimation de la charge de travail.
     *
     * @return liste des estimations de charge de travail
     */
    public List<String> getChargeTravail() {
        return chargeTravail;
    }

    /**
     * Définit l’estimation de la charge de travail.
     *
     * @param chargeTravail liste des estimations
     */
    public void setChargeTravail(List<String> chargeTravail) {
        this.chargeTravail = chargeTravail;
    }

    /**
     * Retourne l’estimation de la difficulté des cours.
     *
     * @return liste des estimations de difficulté
     */
    public List<String> getDifficulte() {
        return difficulte;
    }

    /**
     * Définit l’estimation de la difficulté des cours.
     *
     * @param difficulte liste des estimations
     */
    public void setDifficulte(List<String> difficulte) {
        this.difficulte = difficulte;
    }
}
