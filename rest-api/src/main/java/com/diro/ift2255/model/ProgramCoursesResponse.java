package com.diro.ift2255.model;

import java.util.List;

/**
 * Représente la réponse retournée par l’API des programmes,
 * contenant les informations du programme et la liste des cours associés.
 */
public class ProgramCoursesResponse {

    private List<Program> programs;
    private List<Course> courses;

    public List<Program> getPrograms() { return programs; }
    public List<Course> getCourses() { return courses; }
}

