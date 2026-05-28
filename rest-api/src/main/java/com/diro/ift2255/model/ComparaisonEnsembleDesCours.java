package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder({
    "ensembleA",
    "ensembleB",
    "session",
    "creditsTotalA",
    "creditsTotalB",
    "chargeTravailA",
    "chargeTravailB",
    "difficulteA",
    "difficulteB"
})
public class ComparaisonEnsembleDesCours {

    private List<String> ensembleA;
    private List<String> ensembleB;
    private String session;

    private double creditsTotalA;
    private double creditsTotalB;

    private String chargeTravailA;
    private String chargeTravailB;

    private String difficulteA;
    private String difficulteB;

    public ComparaisonEnsembleDesCours() {}

    // getters / setters
    public List<String> getEnsembleA() { return ensembleA; }
    public void setEnsembleA(List<String> ensembleA) { this.ensembleA = ensembleA; }

    public List<String> getEnsembleB() { return ensembleB; }
    public void setEnsembleB(List<String> ensembleB) { this.ensembleB = ensembleB; }

    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }

    public double getCreditsTotalA() { return creditsTotalA; }
    public void setCreditsTotalA(double creditsTotalA) { this.creditsTotalA = creditsTotalA; }

    public double getCreditsTotalB() { return creditsTotalB; }
    public void setCreditsTotalB(double creditsTotalB) { this.creditsTotalB = creditsTotalB; }

    public String getChargeTravailA() { return chargeTravailA; }
    public void setChargeTravailA(String chargeTravailA) { this.chargeTravailA = chargeTravailA; }

    public String getChargeTravailB() { return chargeTravailB; }
    public void setChargeTravailB(String chargeTravailB) { this.chargeTravailB = chargeTravailB; }

    public String getDifficulteA() { return difficulteA; }
    public void setDifficulteA(String difficulteA) { this.difficulteA = difficulteA; }

    public String getDifficulteB() { return difficulteB; }
    public void setDifficulteB(String difficulteB) { this.difficulteB = difficulteB; }
}
