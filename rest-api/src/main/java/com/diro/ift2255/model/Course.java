package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;
import java.util.Map;

/**
 * Représente un cours universitaire avec ses informations générales,
 * ses conditions d’accès et ses horaires.
 * Cette classe est utilisée pour la désérialisation des données API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

    @JsonProperty("id")
    @JsonAlias("_id") // faire des tests pour verifier si tt les options marchent
    private String id;

    private String name;
    private String description;

    @JsonProperty("credits")
    private Double credits;

    @JsonProperty("available_terms")
    private Map<String, Boolean> availableTerms;

    @JsonProperty("available_periods")
    private Map<String, Boolean> availablePeriods;

    @JsonProperty("schedules")
    private List<Schedule> schedules;

    // Conditions/prérequis
    @JsonProperty("requirement_text")
    private String requirementText;

    @JsonProperty("prerequisite_courses")
    private List<String> prerequisiteCourses;

    @JsonProperty("equivalent_courses")
    private List<String> equivalentCourses;

    @JsonProperty("concomitant_courses")
    private List<String> concomitantCourses;

    public Course() {}

    // ---- GETTERS / SETTERS ----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getCredits() { return credits; }
    public void setCredits(Double credits) { this.credits = credits; }

    public Map<String, Boolean> getAvailableTerms() { return availableTerms; }
    public void setAvailableTerms(Map<String, Boolean> availableTerms) { this.availableTerms = availableTerms; }

    public Map<String, Boolean> getAvailablePeriods() { return availablePeriods; }
    public void setAvailablePeriods(Map<String, Boolean> availablePeriods) { this.availablePeriods = availablePeriods; }

    public List<Schedule> getSchedules() { return schedules; }
    public void setSchedules(List<Schedule> schedules) { this.schedules = schedules; }

    public String getRequirementText() { return requirementText; }
    public void setRequirementText(String requirementText) { this.requirementText = requirementText; }

    public List<String> getPrerequisiteCourses() { return prerequisiteCourses; }
    public void setPrerequisiteCourses(List<String> prerequisiteCourses) { this.prerequisiteCourses = prerequisiteCourses; }

    public List<String> getEquivalentCourses() { return equivalentCourses; }
    public void setEquivalentCourses(List<String> equivalentCourses) { this.equivalentCourses = equivalentCourses; }

    public List<String> getConcomitantCourses() { return concomitantCourses; }
    public void setConcomitantCourses(List<String> concomitantCourses) { this.concomitantCourses = concomitantCourses; }

    // ========== INNER CLASSES ==========

    /**
     * Représente l’horaire d’un cours pour une session donnée.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schedule { // Horaire pour une session

        @JsonProperty("_id")
        private String internalId;

        @JsonProperty("sigle")
        private String sigle;

        @JsonProperty("semester")
        private String semester;

        @JsonProperty("semester_int")
        private Integer semesterInt;

        @JsonProperty("fetch_date")
        private String fetchDate;

        @JsonProperty("sections")
        private List<Section> sections;

        public Schedule() {}

        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }

        public List<Section> getSections() { return sections; }
        public void setSections(List<Section> sections) { this.sections = sections; }
    }

    /**
     * Représente un groupe (section) d’un cours.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Section { // groupe d'un cours

        @JsonProperty("name")
        private String name;

        @JsonProperty("teachers")
        private List<String> teachers;

        @JsonProperty("number_inscription")
        private String numberInscription;

        @JsonProperty("capacity")
        private String capacity;

        @JsonProperty("volets")
        private List<Volet> volets;

        public Section() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<String> getTeachers() { return teachers; }
        public void setTeachers(List<String> teachers) { this.teachers = teachers; }

        public List<Volet> getVolets() { return volets; }
        public void setVolets(List<Volet> volets) { this.volets = volets; }
    }

    /**
     * Représente un volet pédagogique d’un cours
     * (théorie, TP, laboratoire, intra, final).
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Volet { // parties du cours: TH,TP,intra, final

        @JsonProperty("name")
        private String name;

        @JsonProperty("activities")
        private List<Activity> activities;

        public Volet() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<Activity> getActivities() { return activities; }
        public void setActivities(List<Activity> activities) { this.activities = activities; }
    }

    /**
     * Représente une activité horaire d’un cours
     * (bloc de temps associé à un jour).
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Activity { // évenement d'uncours = bloc horaire

        @JsonProperty("days")
        private List<String> days;

        @JsonProperty("start_time")
        private String startTime;

        @JsonProperty("end_time")
        private String endTime;

        @JsonProperty("start_date")
        private String startDate;

        @JsonProperty("end_date")
        private String endDate;

        @JsonProperty("campus")
        private String campus;

        @JsonProperty("place")
        private String place;

        @JsonProperty("pavillon_name")
        private String pavillon;

        @JsonProperty("room")
        private String room;

        @JsonProperty("mode")
        private String mode;

        public Activity() {}

        // --- Getters & Setters ---

        public List<String> getDays() {
            return days;
        }

        public void setDays(List<String> days) {
            this.days = days;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getCampus() {
            return campus;
        }

        public void setCampus(String campus) {
            this.campus = campus;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getPavillon() {
            return pavillon;
        }

        public void setPavillon(String pavillon) {
            this.pavillon = pavillon;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
