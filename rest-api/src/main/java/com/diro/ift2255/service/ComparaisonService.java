package com.diro.ift2255.service;

import com.diro.ift2255.model.*;
import com.diro.ift2255.enums.Charge;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsable des fonctionnalités de comparaison liées aux cours.
 * Ce service centralise la logique métier permettant :
 * - la comparaison de plusieurs cours (2 à 4) pour une session donnée
 * - la création et l’analyse d’un ensemble de cours (2 à 6)
 * - la comparaison de deux ensembles de cours
 */

public class ComparaisonService {

    private final CourseService courseService;
    private final AvisService avisService;
    private final ResultatAcademiqueService resultatService;

    /**
     * Construit le service de comparaison.
     *
     * @param courseService service d’accès aux cours
     * @param avisService service d’accès aux avis étudiants
     * @param resultatService service d’accès aux résultats académiques
     */
    public ComparaisonService(CourseService courseService, AvisService avisService, ResultatAcademiqueService resultatService) {
        this.courseService = courseService;
        this.avisService = avisService;
        this.resultatService = resultatService;
    }

    // ===================== COMPARAISON DE COURS =====================

    /**
     * Compare entre 2 et 4 cours 
     * et retourne un objet ComparaisonDesCours.
     * @param courseCodes liste des codes de cours (2 à 4)
     * @return objet {@link ComparaisonDesCours} représentant le résultat
     * @throws IllegalArgumentException si les paramètres sont invalides
     */

    public ComparaisonDesCours compare(List<String> courseCodes) {
        
        if (courseCodes == null || courseCodes.size() < 2 || courseCodes.size() > 4) {
            throw new IllegalArgumentException("L'étudiant doit choisir entre 2 et 4 cours.");
        }
        
        // 1. Charger les cours à partir des codes (via CourseService)
        List<Course> courses = new ArrayList<>();
        for (String code : courseCodes) {
            Optional<Course> opt = courseService.getCourseById(code);
            if (opt.isEmpty()) {
                throw new IllegalArgumentException("Cours introuvable : " + code);
            }
            courses.add(opt.get());
        }

        // 2. Disponibilité par trimestre (tous les cours, comme avant)
        List<String> availableInAutumn = filterByTerm(courses, "autumn");
        List<String> availableInWinter = filterByTerm(courses, "winter");
        List<String> availableInSummer = filterByTerm(courses, "summer");

        // 3. Liste des crédits
        List<String> creditsList = buildCreditsList(courses);

        // 4. Pré-requis
        List<String> prerequis = computePrerequisSimple(courses);

        // 5. Charge de travail (avis)
        List<String> chargeTravail = computeChargeTravail(courseCodes);

        // 6. Difficulté (résultats académiques)
        List<String> difficulte = computeDifficulte(courseCodes);

        // 7. Construire l'objet ComparaisonDesCours final
        ComparaisonDesCours comparaison = new ComparaisonDesCours();
        comparaison.setCourses(courseCodes);
        comparaison.setAvailableInAutumn(availableInAutumn);
        comparaison.setAvailableInWinter(availableInWinter);
        comparaison.setAvailableInSummer(availableInSummer);
        comparaison.setCredits(creditsList);
        comparaison.setPrerequis(prerequis);
        comparaison.setChargeTravail(chargeTravail);
        comparaison.setDifficulte(difficulte);

        return comparaison;
    }

    


    // ===================== COMPARAISON D’ENSEMBLES ==================

    /**
    * Compare deux ensembles de cours (max 6 cours par ensemble)
    * pour une session donnée.
    *  @param ensembleA liste des codes de cours du premier ensemble (2 à 6 cours)
    * @param ensembleB liste des codes de cours du second ensemble (2 à 6 cours)
    * @param session session ciblée (ex. {@code "autumn"}, {@code "winter"}, {@code "summer"})
    * @return un objet {@link ComparaisonEnsembleDesCours} contenant les analyses
    *         des deux ensembles
    * @throws IllegalArgumentException si l’un des ensembles est nul, invalide
    *         ou contient des cours non offerts pour la session
    */
        public ComparaisonEnsembleDesCours compareEnsembles(
            List<String> ensembleA,
            List<String> ensembleB,
            String session
    ) {
        if (ensembleA == null || ensembleB == null) {
            throw new IllegalArgumentException("Les deux ensembles sont obligatoires.");
        }

        AnalyseEnsemble a = analyserEnsemble(ensembleA, session);
        AnalyseEnsemble b = analyserEnsemble(ensembleB, session);

        ComparaisonEnsembleDesCours result =
                new ComparaisonEnsembleDesCours();

        result.setEnsembleA(ensembleA);
        result.setEnsembleB(ensembleB);
        result.setSession(session);

        result.setCreditsTotalA(a.credits);
        result.setCreditsTotalB(b.credits);

        result.setChargeTravailA(a.chargeTravail);
        result.setChargeTravailB(b.chargeTravail);

        result.setDifficulteA(a.difficulte);
        result.setDifficulteB(b.difficulte);

        return result;
    }

    // ===================== CREER ENSEMBLE ==================
    /**
     * Crée un ensemble de cours (2 à 6) pour une session donnée.
     * @param courses liste des codes de cours (2 à 6)
     * @param session session ciblée (autumn, winter ou summer)
     * @return objet {@link EnsembleDeCours} représentant l’ensemble
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public EnsembleDeCours createEnsemble(List<String> courses, String session) {

        if (courses == null || courses.size() < 2 || courses.size() > 6) {
            throw new IllegalArgumentException(
                "Un ensemble doit contenir entre 2 et 6 cours"
            );
        }

        if (session == null || session.isBlank()) {
            throw new IllegalArgumentException(
                "La session est obligatoire (ex : A24)"
            );
        }

        // Charger les cours
        List<Course> courseObjects = new ArrayList<>();
        for (String code : courses) {
            Course c = courseService.getCourseById(code)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Cours introuvable : " + code));
            courseObjects.add(c);
        }

        // Vérifier disponibilité par session (semester)
        List<String> coursNonDisponibles = new ArrayList<>();

        for (Course c : courseObjects) {
            boolean disponible = c.getSchedules() != null &&
                    c.getSchedules().stream()
                        .anyMatch(s -> session.equals(s.getSemester()));

            if (!disponible) {
                coursNonDisponibles.add(c.getId());
            }
        }

        if (!coursNonDisponibles.isEmpty()) {
            throw new IllegalArgumentException(
                "Cours non offerts à la session " + session + " : " +
                String.join(", ", coursNonDisponibles)
            );
        }

        // Tous les cours sont valides pour la session
        EnsembleDeCours result = new EnsembleDeCours();
        result.setCourses(courses);
        result.setSession(session);

        // Horaire : TOUTES les activités du CSV
        result.setHoraire(buildHoraire(courseObjects, session));



        // Conflits d’horaire
        List<String> conflicts =
                detectScheduleConflicts(courseObjects, session);
        result.setScheduleConflicts(conflicts);
        result.setConflictSchedule(!conflicts.isEmpty());

        // Crédits totaux
        double totalCredits = courseObjects.stream()
                .mapToDouble(Course::getCredits)
                .sum();
        result.setCredits(totalCredits);

        return result;
    }

    // ===================== VALIDER ENSEMBLE =====================
    /**
    * Valide un ensemble de cours pour une session donnée et retourne
    * les objets {@link Course} correspondants.
    *
    * La validation vérifie que :
    
     *   la liste contient entre 2 et 6 cours ;
     *   la session est fournie ;
     *   chaque cours existe ;
     *   chaque cours est offert durant la session demandée.
     * 
     * Si une des conditions n’est pas respectée, une exception est levée
     * avec un message explicite.
     *
     * @param courses liste des codes de cours à valider
     * @param session session ciblée (ex. {@code "A25"}, {@code "H24"})
     * @return la liste des objets {@link Course} validés
     * @throws IllegalArgumentException si l’ensemble est invalide, si un cours
     *         est introuvable ou s’il n’est pas offert pour la session
     */
    private List<Course> validerEnsemble(
            List<String> courses,
            String session
    ) {

        if (courses == null || courses.size() < 2 || courses.size() > 6) {
            throw new IllegalArgumentException(
                "Un ensemble doit contenir entre 2 et 6 cours."
            );
        }

        if (session == null || session.isBlank()) {
            throw new IllegalArgumentException(
                "La session est obligatoire (ex : A25)."
            );
        }

        List<Course> courseObjects = new ArrayList<>();
        List<String> coursNonDisponibles = new ArrayList<>();

        for (String code : courses) {

            Course c = courseService.getCourseById(code)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                "Cours introuvable : " + code
                            )
                    );

            boolean offert = c.getSchedules() != null &&
                    c.getSchedules().stream()
                            .anyMatch(s -> session.equals(s.getSemester()));

            if (!offert) {
                coursNonDisponibles.add(code);
            }

            courseObjects.add(c);
        }

        if (!coursNonDisponibles.isEmpty()) {
            throw new IllegalArgumentException(
                "Cours non offerts à la session " + session + " : " +
                String.join(", ", coursNonDisponibles)
            );
        }

        return courseObjects;
    }


    /**
    * Analyse interne d’un ensemble de cours.
    * @param courses liste des codes de cours de l’ensemble
    * @param session session ciblée (ex. {@code "A25"})
    * @return un objet {@link AnalyseEnsemble} contenant les métriques calculées
    * @throws IllegalArgumentException si l’ensemble est invalide ou si un cours
    *         n’est pas offert pour la session
    */
   
    private AnalyseEnsemble analyserEnsemble(
        List<String> courses,
        String session
    ) {
        // réutilise TA validation existante
        List<Course> courseObjects = validerEnsemble(courses, session);

        AnalyseEnsemble a = new AnalyseEnsemble();

        a.credits = courseObjects.stream()
                .mapToDouble(Course::getCredits)
                .sum();

        a.chargeTravail = calculerChargeEnsemble(courses); // avis.json
        a.difficulte = computeGlobalDifficulte(courses);   // csv

        return a;
    }

        private static class AnalyseEnsemble {
        double credits;
        String chargeTravail;
        String difficulte;
    }



    // ===================== Gestion des conflits d'horaire =====================

    /**
    * Représente une activité d’horaire sous une forme simplifiée
    * afin de faciliter la détection des conflits.
    */
    private static class FlatActivity {

        /** Code du cours associé à l’activité */
        String courseId;

        String section;

        /** Jour de l’activité (ex. "Lu", "Ma", "Me") */
        String day;

        /** Heure de début en minutes depuis 00:00 */
        int start;

        /** Heure de fin en minutes depuis 00:00 */
        int end;

       


        /**
         * Construit une activité aplatie à partir des données brutes.
         *
         * @param courseId code du cours
         * @param day jour de l’activité
         * @param startTime heure de début au format HH:mm
         * @param endTime heure de fin au format HH:mm
         */
        FlatActivity(String courseId, String section, String day, String startTime, String endTime) {
            this.courseId = courseId;
            this.section = section;
            this.day = day;
            this.start = toMinutes(startTime);
            this.end = toMinutes(endTime);
            
        }
    }


    /**
    * Extrait toutes les activités d’un cours pour une session donnée
    * et les convertit en {@link FlatActivity}.
    * @param c cours à analyser
    * @param semester session ciblée (ex. {@code "A25"})
    * @return liste d’activités aplaties utilisées pour la détection
    *         des conflits d’horaire
    */
    private List<FlatActivity> extractActivities(Course c, String semester) {
        List<FlatActivity> list = new ArrayList<>();

        if (c.getSchedules() == null) return list;

        for (Course.Schedule s : c.getSchedules()) {
            if (!semester.equals(s.getSemester())) continue;

            if (s.getSections() == null) continue;
            for (Course.Section sec : s.getSections()) {
                if (sec.getVolets() == null) continue;
                for (Course.Volet v : sec.getVolets()) {
                    if (v.getActivities() == null) continue;
                    for (Course.Activity a : v.getActivities()) {
                        if (a.getDays() == null) continue;

                        for (String day : a.getDays()) {
                            list.add(new FlatActivity(
                                    c.getId(),
                                    sec.getName(),
                                    day,
                                    a.getStartTime(),
                                    a.getEndTime()
                            ));
                        }
                    }
                }
            }
        }
        return list;
    }

    
    /**
     * Détecte les conflits d'horaire entre les cours.
     * Un conflit = même jour + chevauchement des heures.
     * On retourne au plus UNE entrée par paire de cours (tous jours confondus).
     * @param courses liste des cours à analyser
     * @param semester session ciblée (ex. {@code "A25"})
     * @return liste de descriptions textuelles des conflits d’horaire détectés ;
     *         la liste est vide s’il n’y a aucun conflit
     */
    private List<String> detectScheduleConflicts(List<Course> courses, String semester) {

    List<FlatActivity> all = new ArrayList<>();
    for (Course c : courses) {
        all.addAll(extractActivities(c, semester));
    }

    List<String> conflicts = new ArrayList<>();
    Set<String> seenPairs = new HashSet<>();

    for (int i = 0; i < all.size(); i++) {
        FlatActivity a1 = all.get(i);

        for (int j = i + 1; j < all.size(); j++) {
            FlatActivity a2 = all.get(j);

            if (!a1.day.equals(a2.day)) continue;
            if (a1.courseId.equals(a2.courseId)) continue;

            boolean overlap = a1.start < a2.end && a2.start < a1.end;
            if (!overlap) continue;

            FlatActivity fa1 = a1;
            FlatActivity fa2 = a2;

            String c1 = fa1.courseId;
            String c2 = fa2.courseId;

            if (c1.compareTo(c2) > 0) {
                // swap cours
                String tmp = c1;
                c1 = c2;
                c2 = tmp;

                // swap activités (sections + horaires)
                FlatActivity tmpA = fa1;
                fa1 = fa2;
                fa2 = tmpA;
            }

            String key = c1 + "|" + c2;
            if (seenPairs.contains(key)) continue;
            seenPairs.add(key);

            conflicts.add(
                "Conflit entre " + c1 + " (section " + fa1.section + ") et "
                + c2 + " (section " + fa2.section + ") le " + fa1.day
                + " (" + minutesToString(Math.max(fa1.start, fa2.start))
                + "-" + minutesToString(Math.min(fa1.end, fa2.end)) + ")"
            );


        }
    }
    return conflicts;
}

    // ======================= AVIS =======================

    /**
     * Calcule la charge de travail moyenne par cours
     * à partir des avis étudiants.
     * Pour chaque cours, la charge de travail dominante (la plus fréquente) est déterminée à partir des avis disponibles.
     * @param courses liste des codes de cours à analyser
     * @return liste de chaînes décrivant la charge de travailestimée pour chaque cours
     */
    private List<String> computeChargeTravail(List<String> courses) {

        List<String> result = new ArrayList<>();

        for (String code : courses) {

            List<Avis> avisList = avisService.getAvisChargeExploitables(code);

            if (avisList.isEmpty()) {
                result.add(code + " : aucune donnée");
                continue;
            }

            Map<Charge, Long> freq = avisList.stream()
                .collect(Collectors.groupingBy(
                        Avis::getChargeTravail,
                        Collectors.counting()
                ));

                Charge dominante = Collections.max(freq.entrySet(), Map.Entry.comparingByValue()).getKey();
                result.add(code + " : " + dominante.name());
        }
        return result;
    }


    // =================== RÉSULTATS ACADÉMIQUES ===================

    /**
     * Estime la difficulté à partir du score académique.
     * Le score académique moyen du cours est récupéré depuis les données CSV, puis converti en un niveau de difficulté qualitatif.
     * @param courses liste des codes de cours à analyser
     * @return liste de chaînes décrivant la difficulté estimée pour chaque cours
     * Si aucun résultat académique n’est disponible, la mention {@code "aucune donnée"} est retournée.<
     */
   private List<String> computeDifficulte(List<String> courses) {
    List<String> result = new ArrayList<>();

    for (String code : courses) {
        Optional<AcademicResult> res =
                resultatService.getResultatsParCours(code);

        if (res.isEmpty()) {
            result.add(code + " : aucune donnée");
        } else {
            double score = res.get().getScore();
            result.add(code + " : " + mapScoreToNiveau(score));
        }
    }
    return result;
    }
    /**
     * Convertit un score académique numérique
     * en un niveau de difficulté qualitatif.
     *
     * @param score score académique moyen du cours
     * @return niveau de difficulté correspondant
     */
    private String mapScoreToNiveau(double score) {
        if (score >= 4.5) return "TRES_FACILE";
        if (score >= 3.5) return "FACILE";
        if (score >= 2.5) return "MOYENNE";
        if (score >= 2.0) return "DIFFICILE";
        return "TRES_DIFFICILE";
    }



    
    // ========================== Fonctions utilitaires =========================

    private static int toMinutes(String hhmm) {
        if (hhmm == null || !hhmm.contains(":")) return 0;
        String[] parts = hhmm.split(":");
        try {
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            return h * 60 + m;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String minutesToString(int m) {
        int h = m / 60;
        int min = m % 60;
        return String.format("%02d:%02d", h, min);
    }

    /**
     * Retourne la liste des IDs de cours disponibles pour un trimestre donné.
     * (pour affichage dans le JSON)
     */
    private List<String> filterByTerm(List<Course> courses, String termKey) {
        List<String> result = new ArrayList<>();
        for (Course c : courses) {
            if (Boolean.TRUE.equals(c.getAvailableTerms().get(termKey))) {
                result.add(c.getId());
            }
        }
        return result;
    }


    /**
     * Construit la liste des crédits sous forme lisible :
     * ex : "IFT2035 : 3.0 crédits"
     */
    private List<String> buildCreditsList(List<Course> courses) {
        List<String> credits = new ArrayList<>();
        for (Course c : courses) {
            credits.add(c.getId() + " : " + c.getCredits() + " crédits");
        }
        return credits;
    }

    /**
     * Construit la chaîne des prérequis sous forme lisible.
     */
    private List<String> computePrerequisSimple(List<Course> courses) {
        List<String> list = new ArrayList<>();

        for (Course c : courses) {
            if (c.getPrerequisiteCourses().isEmpty()) {
                list.add(c.getId() + " : aucun prérequis");
            } else {
                list.add(c.getId() + " : " +
                        String.join(", ", c.getPrerequisiteCourses()));
            }
        }

        return list;
    }

private Map<String, Map<String, List<String>>> buildHoraire(
        List<Course> courses,
        String semester
) {
    Map<String, Map<String, List<String>>> horaire = new LinkedHashMap<>();
    Set<String> dejaAjoute = new HashSet<>();

    for (Course c : courses) {

        if (c.getSchedules() == null) continue;

        Course.Schedule schedule = c.getSchedules().stream()
                .filter(s -> semester.equals(s.getSemester()))
                .findFirst()
                .orElse(null);

        if (schedule == null || schedule.getSections() == null) continue;

        Map<String, List<String>> sectionsMap = new LinkedHashMap<>();

        for (Course.Section section : schedule.getSections()) {

            List<String> lignes = new ArrayList<>();

            // ordre voulu
            ajouterVolet(c, section, "TH", dejaAjoute, lignes);
            ajouterVolet(c, section, "TP", dejaAjoute, lignes);
            ajouterVolet(c, section, "LAB", dejaAjoute, lignes);
            ajouterVolet(c, section, "Intra", dejaAjoute, lignes);
            ajouterVolet(c, section, "Final", dejaAjoute, lignes);

            if (!lignes.isEmpty()) {
                sectionsMap.put("section " + section.getName(), lignes);
            }
        }

        if (!sectionsMap.isEmpty()) {
            horaire.put(c.getId(), sectionsMap);
        }
    }

    return horaire;
}

private void ajouterVolet(
        Course c,
        Course.Section section,
        String typeVolet,
        Set<String> dejaAjoute,
        List<String> horaire
) {
    if (section == null || section.getVolets() == null) return;

    String sectionName = section.getName(); // A, A101, etc.

    for (Course.Volet v : section.getVolets()) {

        if (!typeVolet.equalsIgnoreCase(v.getName())) continue;
        if (v.getActivities() == null) continue;

        for (Course.Activity a : v.getActivities()) {

            if (a.getDays() == null) continue;

            for (String day : a.getDays()) {

                // 🔑 clé unique AVEC section
                String key =
                        c.getId() + "|" +
                        sectionName + "|" +
                        typeVolet + "|" +
                        day + "|" +
                        a.getStartTime() + "-" + a.getEndTime();

                if (dejaAjoute.contains(key)) continue;
                dejaAjoute.add(key);

                String affichage;

                // Intra / Final → date complète
                if ("Intra".equalsIgnoreCase(typeVolet)
                        || "Final".equalsIgnoreCase(typeVolet)) {

                    String date = formatDateFrancais(a.getStartDate());

                    affichage =
                        c.getId() + " (" + sectionName + " – " + typeVolet + ") – " +
                        date + " " +
                        a.getStartTime() + "-" + a.getEndTime();

                } else {
                    // TH / TP → jour + heure
                    affichage =
                        c.getId() + " (" + sectionName + " – " + typeVolet + ") – " +
                        day + " " +
                        a.getStartTime() + "-" + a.getEndTime();
                }

                horaire.add(affichage);
            }
        }
    }
}



private String formatDateFrancais(String dateIso) {
    try {
        LocalDate date = LocalDate.parse(dateIso);
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
        return date.format(formatter);
    } catch (Exception e) {
        return dateIso; // fallback sécurité
    }
}



private String calculerChargeEnsemble(List<String> courseIds) {

    if (courseIds == null || courseIds.isEmpty()) {
        return "aucune donnée";
    }

    int total = 0;
    int count = 0;

    for (String code : courseIds) {

        List<Avis> avisList = avisService.getAvisAffichables(code);
        if (avisList.isEmpty()) continue;

        // Charge dominante du cours
        Map<Charge, Long> freq = avisList.stream()
                .collect(Collectors.groupingBy(
                        Avis::getChargeTravail,
                        Collectors.counting()
                ));

        Charge dominante = Collections.max(
                freq.entrySet(),
                Map.Entry.comparingByValue()
        ).getKey();

        // Conversion en score numérique
        switch (dominante) {
    case TRES_LEGER:
        total += 1;
        break;
    case LEGER:
        total += 2;
        break;
    case MOYEN:
        total += 3;
        break;
    case LOURDE:
        total += 4;
        break;
    case EXTREME:
        total += 5;
        break;
    }

        count++;
    }

    if (count == 0) return "aucune donnée";

    double moyenne = total / (double) count;

    if (moyenne < 1.7) return "FAIBLE";
    if (moyenne < 2.4) return "MOYENNE";
    return "ELEVEE";
}



private String computeGlobalDifficulte(List<String> courses) {

    double total = 0;
    int count = 0;

    for (String code : courses) {

        Optional<AcademicResult> res =
                resultatService.getResultatsParCours(code);

        if (res.isPresent()) {
            total += res.get().getScore();
            count++;
        }
    }

    if (count == 0) return "aucune donnée";

    double moyenne = total / count;
    return mapScoreToNiveau(moyenne);
}


    
}