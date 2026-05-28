package com.diro.ift2255.model;

import java.util.List;

public class EnsembleDesCoursResult {
    private List<String> courses;
    private String session;

    private boolean conflictSchedule;
    private List<String> scheduleConflicts;

    private List<String> horaire;

    private double creditsTotal;
    private String difficulteGlobale;
    private String chargeTravailGlobale;
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
    public List<String> getHoraire() {
        return horaire;
    }
    public void setHoraire(List<String> horaire) {
        this.horaire = horaire;
    }
    public double getCreditsTotal() {
        return creditsTotal;
    }
    public void setCreditsTotal(double creditsTotal) {
        this.creditsTotal = creditsTotal;
    }
    public String getDifficulteGlobale() {
        return difficulteGlobale;
    }
    public void setDifficulteGlobale(String difficulteGlobale) {
        this.difficulteGlobale = difficulteGlobale;
    }
    public String getChargeTravailGlobale() {
        return chargeTravailGlobale;
    }
    public void setChargeTravailGlobale(String chargeTravailGlobale) {
        this.chargeTravailGlobale = chargeTravailGlobale;
    }
    
}
