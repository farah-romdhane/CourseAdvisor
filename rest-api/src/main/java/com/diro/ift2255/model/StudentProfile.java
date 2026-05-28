package com.diro.ift2255.model;

import java.util.List;

/**
 * Profil étudiant utilisé pour personnaliser l'expérience utilisateur.
 * Ce profil inclut des informations sur le programme de l'étudiant,
 * son cheminement et les cours qu'il souhaite suivre.
 */
public class StudentProfile {

    /** Programme de l'étudiant */
    private String programme;

    /** Liste des cours que l'étudiant doit encore suivre dans son programme. */
    private List<String> coursRestants;

    /** Liste des cours que l'étudiant souhaite suivre. */
    private List<String> coursSouhaites;



    public StudentProfile() {}

    /**
     * Constructeur permettant de créer un profil étudiant complet.
     *
     * @param programme       programme d'études de l'étudiant
     * @param coursRestants   liste des cours restants à compléter
     * @param coursSouhaites  liste des cours que l'étudiant souhaite suivre
     */
    public StudentProfile(String programme, List<String> coursRestants, List<String> coursSouhaites) {
        this.programme = programme;
        this.coursRestants = coursRestants;
        this.coursSouhaites = coursSouhaites;
    }


    /**
     * Retourne le programme d'études de l'étudiant.
     *
     * @return le programme d'études
     */
    public String getProgramme() { 
        return programme; 
    }

    /**
     * Définit le programme d'études de l'étudiant.
     *
     * @param program le programme d'études
     */
    public void setProgramme(String program) {
    this.programme = program;
    }

    /**
     * Retourne la liste des cours restants à compléter.
     *
     * @return liste des cours restants
     */
    public List<String> getCoursRestants() { 
        return coursRestants; 
    }

    /**
     * Définit la liste des cours restants à compléter.
     *
     * @param coursRestants liste des cours restants
     */
    public void setCoursRestants(List<String> coursRestants) {
        this.coursRestants = coursRestants;
    }

    /**
     * Retourne la liste des cours souhaités par l'étudiant.
     *
     * @return liste des cours souhaités
     */
    public List<String> getCoursSouhaites() { 
        return coursSouhaites; 
    }
    /**
     * Définit la liste des cours souhaités par l'étudiant.
     *
     * @param coursSouhaites liste des cours souhaités
     */
    public void setCoursSouhaites(List<String> coursSouhaites) {
        this.coursSouhaites = coursSouhaites;
    }
}
