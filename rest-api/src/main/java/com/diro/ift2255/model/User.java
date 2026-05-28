package com.diro.ift2255.model;

import java.util.List;

import com.diro.ift2255.enums.Cycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Représente un utilisateur de l'application.
 * 
 * Cette classe contient les informations essentielles d'un étudiant ou
 * d'un utilisateur général, incluant son identifiant, son cycle d'études,
 * ses préférences ainsi que la liste des cours qu'il a déjà complétés.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    /** Identifiant unique de l'utilisateur. */
    private int id;

    /** Nom complet de l'utilisateur. */
    private String name;

    /** Adresse courriel de l'utilisateur. */
    private String email;

    /** Matricule étudiant, si applicable. */
    private String matricule;

    /** Mot de passe associé au compte utilisateur. */
    private String motDePasse;

    /** Cycle d'études de l'utilisateur
     */
    private Cycle cycle;

    /** Liste des préférences de l'utilisateur (optionnel). */
    private List<String> preference;

    /** Liste des sigles de cours complétés par l'utilisateur. */
    private List<String> completedCourses;

    /** Profil étudiant */
    private StudentProfile profile;

    /** 
     * Constructeur vide nécessaire pour lire les données 
     * depuis le fichier users.json. 
     */
    public User() {}

    /**
     * Constructeur permettant de créer un utilisateur avec ID, nom et courriel.
     *
     * @param id identifiant unique
     * @param name nom complet
     * @param email adresse courriel
     */
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /** @return l'identifiant unique de l'utilisateur */
    public int getId() { 
        return id; 
    }

    /** @param id définit l'identifiant unique de l'utilisateur */
    public void setId(int id) { 
        this.id = id; 
    }

    /** @return le nom de l'utilisateur */
    public String getName() { 
        return name; 
    }

    /** @param name définit le nom de l'utilisateur */
    public void setName(String name) { 
        this.name = name; 
    }

    /** @return l'adresse courriel de l'utilisateur */
    public String getEmail() { 
        return email; 
    }

    /** @param email définit l'adresse courriel de l'utilisateur */
    public void setEmail(String email) { 
        this.email = email; 
    }

    /** @return le matricule de l'utilisateur */
    public String getMatricule() { 
        return matricule; 
    }

    /** @param matricule définit le matricule étudiant */
    public void setMatricule(String matricule) { 
        this.matricule = matricule; 
    }

    /** @return le mot de passe de l'utilisateur */
    public String getMotDePasse() { 
        return motDePasse; 
    }

    /** @param motDePasse définit le mot de passe de l'utilisateur */
    public void setMotDePasse(String motDePasse) { 
        this.motDePasse = motDePasse; 
    }

    /** @return le cycle d'études de l'utilisateur */
    public Cycle getCycle() { 
        return cycle; 
    }

    /** @param cycle définit le cycle d'études de l'utilisateur */
    public void setCycle(Cycle cycle) { 
        this.cycle = cycle; 
    }

     /** @return le profil étudiant de l'utilisateur */
    public StudentProfile getProfile() {
        return profile;
    }

    /** @param profile définit le profil étudiant */
        public void setProfile(StudentProfile profile) {
            this.profile = profile;
        }

    /** @return la liste des préférences de l'utilisateur */
    public List<String> getPreference() { 
        return preference; 
    }

    /** @param preference définit la liste des préférences de l'utilisateur */
    public void setPreference(List<String> preference) { 
        this.preference = preference; 
    }

    /** @return le nom complet de l'utilisateur */
    public String getNomComplet() {
        return name; 
    }

    /** @return la liste des cours complétés par l'utilisateur */
    public List<String> getCompletedCourses() { 
        return completedCourses; 
    }

    /** @param completedCourses définit la liste des sigles de cours complétés */
    public void setCompletedCourses(List<String> completedCourses) { 
        this.completedCourses = completedCourses; 
    }
}
