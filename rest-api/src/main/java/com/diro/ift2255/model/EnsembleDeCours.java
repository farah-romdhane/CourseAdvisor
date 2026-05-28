package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

/**
 * Représente un ensemble de cours pour un trimestre donné
 * (max 6 cours).
 */
@JsonPropertyOrder({
    "courses",
    "session",
    "Crédits",
    "horaire",
    "conflictSchedule",
    "scheduleConflicts"
})

public class EnsembleDeCours {

    private List<String> courses;

    private String session;

    @JsonProperty("Crédits")
    private double credits;

    private Map<String, Map<String, List<String>>> horaire;

    private boolean conflictSchedule;

    private List<String> scheduleConflicts;


    // ===== Constructeur vide (obligatoire pour Jackson) =====
    public EnsembleDeCours() {}

    // ===== Getters / Setters =====

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public boolean isConflictSchedule() {
        return conflictSchedule;
    }

    public void setConflictSchedule(boolean conflictSchedule) {
        this.conflictSchedule = conflictSchedule;
    }

    public List<String> getScheduleConflicts() {
        return scheduleConflicts;
    }

    public void setScheduleConflicts(List<String> scheduleConflicts) {
        this.scheduleConflicts = scheduleConflicts;
    }

    public Map<String, Map<String, List<String>>> getHoraire() {
        return horaire;
    }

    public void setHoraire(Map<String, Map<String, List<String>>> horaire) {
        this.horaire = horaire;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

}
