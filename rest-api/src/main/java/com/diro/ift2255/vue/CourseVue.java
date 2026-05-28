package com.diro.ift2255.vue;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.model.Avis;
import com.diro.ift2255.model.EnsembleDeCours;
import com.diro.ift2255.model.ComparaisonDesCours;
import com.diro.ift2255.model.Program;
import com.diro.ift2255.model.ProgramCoursesResponse;
import com.diro.ift2255.Repository.CourseRepository;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.service.AvisService;
import com.diro.ift2255.service.ComparaisonService;
import com.diro.ift2255.service.ResultatAcademiqueService;
import com.diro.ift2255.service.EligibilityService;
import com.diro.ift2255.service.ProgramService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Vue console responsable de l’interaction utilisateur
 * pour l’exploration, la comparaison et l’analyse des cours.
 */
public class CourseVue {

    private final CourseService service;
    private final CourseRepository repo;
    private final ComparaisonService comparaisonService;
    private final ResultatAcademiqueService resultatService;
    private final AvisService avisService;
    private final ProgramService programService;

    /**
     * Initialise la vue avec les services nécessaires.
     *
     * @param service service d’accès aux cours
     * @param repo dépôt local des cours
     * @param compService service de comparaison
     * @param resultatService service des résultats académiques
     * @param avisService service des avis étudiants
     * @param programService service des programmes
     */
    public CourseVue(
        CourseService service,
        CourseRepository repo,
        ComparaisonService compService,
        ResultatAcademiqueService resultatService,
        AvisService avisService,
        ProgramService programService
    ) {
        this.service = service;
        this.repo = repo;
        this.comparaisonService = compService;
        this.resultatService = resultatService;
        this.avisService = avisService;
        this.programService= programService;
    }
    // --------------------------------------------------------------------
    // 1. Recherche par sigle (ID)
    // --------------------------------------------------------------------
    /**
     * Recherche un cours par sigle exact ou partiel (préfixe).
     *
     * @param id sigle ou préfixe du cours
     * @return Optional contenant le cours si trouvé par sigle exact,
     *         Optional vide sinon
     */
    public Optional<Course> searchById(String id) {
        if (id == null || id.isBlank()) {
            System.out.println("Veuillez entrer un sigle valide.");
            return Optional.empty();
        }
    
    
        // 1. Recherche dans le cache

        Optional<Course> local = repo.findById(id);
        if (local.isPresent()) {
            Course c = local.get();
            System.out.println("- " + c.getId() + " | " + c.getName());
            return local;
        }
    
        // 2. Recherche par préfixe (IFT, MAT, etc.)
        List<Course> prefixMatches = repo.findByPrefix(id);

        if (!prefixMatches.isEmpty()) {
            System.out.println("\nCours trouvés pour le préfixe \"" + id + "\" :");
            prefixMatches.forEach(c ->
                    System.out.println("- " + c.getId() + " | " + c.getName())
            );
            return Optional.empty(); // volontaire
        }
        
        // 3. Recherche dans l'API - sigle exact
        
        Optional<Course> api = service.getCourseById(id);
        if (api.isPresent()) {
            Course c = api.get();
            System.out.println("- " + c.getId() + " | " + c.getName());
            return api;
        }
    
        
        // 4. Aucun résultat
        
        System.out.println("Aucun cours trouvé pour : " + id);
        return Optional.empty();
    }

    // --------------------------------------------------------------------
    // 2. Recherche par nom
    // --------------------------------------------------------------------
    /**
     * Recherche des cours par nom ou mot-clé.
     *
     * @param name nom ou fragment du nom du cours
     * @return liste des cours correspondants
     */
    public List<Course> searchByName(String name) {
        if (name == null || name.isBlank()) {
            System.out.println("Veuillez entrer un nom valide.");
            return List.of();
        }

        // Cherche dans le cache local
        Map<String, Course> data = repo.findAll();

        List<Course> matches = data.values().stream()
                .filter(c -> c.getName() != null &&
                        c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        if (!matches.isEmpty()) {
            return matches;
        }

        // Cherche dans l’API
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        List<Course> apiResults = service.getAllCourses(params);

        if (apiResults.isEmpty()) {
            System.out.println("Aucun cours trouvé pour : " + name);
        }

        return apiResults;
    }

    // --------------------------------------------------------------------
    // 3. Voir les détails complets d’un cours
    // --------------------------------------------------------------------
    /**
     * Affiche les informations complètes d’un cours.
     *
     * @param id sigle du cours
     */
    public void showDetails(String id) {
        Optional<Course> course = service.getCourseById(id);

        if (course.isEmpty()) {
            System.out.println("Aucun cours trouvé.");
            return;
        }

        Course c = course.get();

        System.out.println("=== Détails du cours ===");
        System.out.println("Sigle: " + c.getId());
        System.out.println("Nom: " + c.getName());
        System.out.println("Crédits: " + c.getCredits());
        System.out.println("Description: " + c.getDescription());
        System.out.println("Terms disponibles: " + c.getAvailableTerms());
        System.out.println("Périodes disponibles: " + c.getAvailablePeriods());

        if (c.getSchedules() != null) {
            System.out.println("Sessions:");
            c.getSchedules().forEach(s ->
                    System.out.println("- " + s.getSemester())
            );
        }

        displaySchedule(c);

        // PRÉREQUIS
        System.out.println("Prérequis :");

        List<String> prereqs = c.getPrerequisiteCourses();
        if (prereqs == null || prereqs.isEmpty()) {
            System.out.println("- Aucun prérequis.");
        } else {
            prereqs.forEach(p -> System.out.println("- " + p));
        }

        
        // AVIS DES ÉTUDIANTS
        System.out.println("\nAvis des étudiants :");

        // Le service gère la règle des 5 avis minimum
        List<com.diro.ift2255.model.Avis> avisList = avisService.getAvisAffichables(c.getId());

        if (avisList.isEmpty()) {
            System.out.println("- Aucun avis disponible");
        } else {
                for (Avis a : avisList) {
                // Mapping Difficulté
                int noteDiff = switch (a.getDifficulte()) {
                    case TRES_FACILE -> 1;
                    case FACILE -> 2;
                    case MOYEN -> 3;
                    case DIFFICILE -> 4;
                    case TRES_DIFFICILE -> 5;
                    };

                // Mapping Charge
                int noteCharge = switch (a.getChargeTravail()) {
                    case TRES_LEGER -> 1;
                    case LEGER -> 2;
                    case MOYEN -> 3;
                    case LOURDE -> 4;
                    case EXTREME -> 5;
                };

                System.out.printf("- [Diff: %d/5, Charge: %d/5] %s\n", 
                                noteDiff, noteCharge, a.getCommentaire());
                }
        }

        // ÉQUIVALENTS
        System.out.println("Équivalences :");
        List<String> eq = c.getEquivalentCourses();
        if (eq == null || eq.isEmpty()) {
            System.out.println("- Aucune équivalence.");
        } else {
            eq.forEach(e -> System.out.println("- " + e));
        }

        // CONCOMITANTS
        System.out.println("Concomitants :");
        List<String> conc = c.getConcomitantCourses();
        if (conc == null || conc.isEmpty()) {
            System.out.println("- Aucun cours concomitant.");
        } else {
            conc.forEach(co -> System.out.println("- " + co));
        }

        // RÉSULTATS ACADÉMIQUES
        System.out.println("Résultats académiques :");

        resultatService.getResultatsParCours(c.getId())
            .ifPresentOrElse(
                r -> {
                    System.out.println("- Moyenne : " + r.getMoyenne());
                    System.out.println("- Score de réussite : " + r.getScore() + " / 5");
                },
                () -> System.out.println("- Aucun résultat académique disponible.")
    );
    }
    /**
     * Affiche l’horaire détaillé d’un cours.
     *
     * @param c cours dont l’horaire doit être affiché
     */
    private void displaySchedule(Course c) {
        if (c.getSchedules() == null || c.getSchedules().isEmpty()) {
            System.out.println("Aucun horaire disponible.");
            return;
        }
    
        System.out.println("\n=== Horaire détaillé ===");
    
        c.getSchedules().forEach(schedule -> {
            System.out.println("\nSession : " + schedule.getSemester());
    
            schedule.getSections().forEach(section -> {
                System.out.println("  Groupe : " + section.getName());
                System.out.println("    Prof(s) : " + String.join(", ", section.getTeachers()));
    
                section.getVolets().forEach(volet -> {
                    System.out.println("    → " + volet.getName());  // TH, TP, Intra, Final
    
                    volet.getActivities().forEach(activity -> {
                        String days = String.join("/", activity.getDays());
                        String place = activity.getPavillon() + " — Salle " + activity.getRoom();
    
                        System.out.printf(
                                "      - %s %s–%s | %s\n",
                                days,
                                activity.getStartTime(),
                                activity.getEndTime(),
                                place
                        );
                    });
                });
            });
        });
    }

    // --------------------------------------------------------------------
    // 4. Comparer entre 2 et 4 cours
    // --------------------------------------------------------------------
    /**
     * Compare entre 2 et 4 cours.
     *
     * @param ids liste des sigles des cours à comparer
     * @param term trimestre ciblé
     */
    public void compareCourses(List<String> ids, String term) {

        if (ids == null || ids.size() < 2 || ids.size() > 4) {
            System.out.println("Veuillez entrer 2 à 4 cours.");
            return;
        }
    
        try {
            ComparaisonDesCours comp = comparaisonService.compare(ids);
    
            System.out.println("\n=== COMPARAISON DES COURS ===");
            System.out.println("Cours comparés : " + String.join(", ", comp.getCourses()));
            System.out.println("--------------------------------------------------");
    
            // ===== DISPONIBILITÉ =====
            System.out.println("\nDisponibilité par trimestre :");
            System.out.println("  Automne : " + comp.getAvailableInAutumn());
            System.out.println("  Hiver   : " + comp.getAvailableInWinter());
            System.out.println("  Été     : " + comp.getAvailableInSummer());
    
            // ===== CRÉDITS =====
            System.out.println("\nCrédits :");
            comp.getCredits().forEach(c ->
                    System.out.println("  - " + c)
            );
    
            // ===== PRÉREQUIS =====
            System.out.println("\nPrérequis :");
            comp.getPrerequis().forEach(p ->
                    System.out.println("  - " + p)
            );
    
            // ===== CHARGE DE TRAVAIL =====
            System.out.println("\nCharge de travail estimée :");
            comp.getChargeTravail().forEach(ct ->
                    System.out.println("  - " + ct)
            );
    
            // ===== DIFFICULTÉ =====
            System.out.println("\nDifficulté estimée :");
            comp.getDifficulte().forEach(d ->
                    System.out.println("  - " + d)
            );
    
            System.out.println("\n=== FIN COMPARAISON ===\n");
    
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // --------------------------------------------------------------------
    // 5. Vérifier l’éligibilité à un cours
    // --------------------------------------------------------------------
    /**
     * Vérifie l’éligibilité d’un étudiant à un cours donné.
     *
     * @param courseId sigle du cours à vérifier
     * @param programId identifiant du programme
     * @param completedCourses liste des cours complétés
     * @param eligibilityService service d’éligibilité
     */
    public void checkEligibility(
        String courseId,
        String programId,
        List<String> completedCourses,
        EligibilityService eligibilityService
    ) {
        if (courseId == null || courseId.isBlank()
                || programId == null || programId.isBlank()) {
            System.out.println("Entrée invalide pour la vérification d’éligibilité.");
            return;
        }

        System.out.println("\n=== Vérification d’éligibilité ===");
        System.out.println("Cours à vérifier : " + courseId);
        System.out.println("Programme        : " + programId);
        System.out.println("Cours complétés  : " + completedCourses);

        boolean eligible = eligibilityService.isCourseEligible(
                programId,
                courseId,
                completedCourses
        );

        if (eligible) {
            System.out.println("Vous êtes ÉLIGIBLE à ce cours.");
            return;
        }

        // ---- NON ÉLIGIBLE : expliquer pourquoi ----
        System.out.println("Vous n’êtes PAS éligible à ce cours.");

        // Récupérer les prérequis du cours
        Optional<Course> courseOpt = service.getCourseById(courseId);

        if (courseOpt.isEmpty()) {
            System.out.println("Impossible de déterminer les prérequis.");
            return;
        }

        Course course = courseOpt.get();
        List<String> prereqs = course.getPrerequisiteCourses();

        if (prereqs == null || prereqs.isEmpty()) {
            System.out.println("Aucun prérequis manquant détecté (cycle ou autre contrainte).");
            return;
        }

        // Calcul des cours manquants
        List<String> missing = prereqs.stream()
                .filter(p -> !completedCourses.contains(p.toUpperCase()))
                .toList();

        if (missing.isEmpty()) {
            System.out.println("Les prérequis sont satisfaits, mais le cours n’est pas accessible (cycle ou règles du programme).");
        } else {
            System.out.println("Cours manquants :");
            missing.forEach(m -> System.out.println("- " + m));
        }
    }
    // --------------------------------------------------------------------
    // 6. Programmes
    // --------------------------------------------------------------------
    /**
     * Affiche tous les cours associés à un programme.
     *
     * @param programId identifiant du programme
     */
    public void showCoursesByProgram(String programId) {

        if (programId == null || programId.isBlank()) {
            System.out.println("ID de programme invalide.");
            return;
        }
    
        Optional<ProgramCoursesResponse> responseOpt =
                programService.getProgramWithCourses(programId);
    
        if (responseOpt.isEmpty()) {
            System.out.println("Programme introuvable.");
            return;
        }
    
        ProgramCoursesResponse response = responseOpt.get();
    
        if (response.getPrograms().isEmpty()) {
            System.out.println("Aucune information de programme trouvée.");
            return;
        }
    
        Program program = response.getPrograms().get(0);
    
        System.out.println("\n=== Programme ===");
        System.out.println(program.getId() + " — " + program.getName());
    
        System.out.println("\n=== Cours offerts ===");
    
        response.getCourses().forEach(c ->
            System.out.printf(
                "- %s | %s | %.1f crédits\n",
                c.getId(),
                c.getName(),
                c.getCredits()
            )
        );
    
        System.out.println("\nTotal : " + response.getCourses().size() + " cours");
    }

    /**
     * Affiche les cours d’un programme offerts pour un trimestre donné.
     *
     * @param programId identifiant du programme
     * @param semester trimestre ciblé
     */
    public void showCoursesByProgramAndSemester(String programId, String semester) {

        // 1. Validation des entrées
        if (programId == null || programId.isBlank()) {
            System.out.println("ID du programme invalide.");
            return;
        }
    
        if (semester == null || !semester.matches("[HAE][0-9]{2}")) {
            System.out.println("Format du trimestre invalide (ex: H25, A24, E24).");
            return;
        }
    
        // 2. Conversion trimestre -> clé API
        String apiTerm;
        switch (semester.charAt(0)) {
            case 'H': apiTerm = "winter"; break;
            case 'A': apiTerm = "autumn"; break;
            case 'E': apiTerm = "summer"; break;
            default:
                System.out.println("Trimestre invalide.");
                return;
        }
    
        // 3. Appel au ProgramService
        Optional<ProgramCoursesResponse> responseOpt =
                programService.getProgramWithCourses(programId);
    
        if (responseOpt.isEmpty()) {
            System.out.println("Programme introuvable ou erreur API.");
            return;
        }
    
        List<Course> courses = responseOpt.get().getCourses();
    
        if (courses == null || courses.isEmpty()) {
            System.out.println("Aucun cours associé à ce programme.");
            return;
        }
    
        // 4. Filtrage par trimestre
        List<Course> filteredCourses = courses.stream()
                .filter(c -> c.getAvailableTerms() != null)
                .filter(c -> Boolean.TRUE.equals(c.getAvailableTerms().get(apiTerm)))
                .sorted(Comparator.comparing(Course::getId))
                .toList();
    
        if (filteredCourses.isEmpty()) {
            System.out.println("Aucun cours offert pour le trimestre " + semester);
            return;
        }
    
        // 5. Affichage
        System.out.println("\nCours offerts pour le programme " + programId +
                " au trimestre " + semester + " :\n");
    
        for (Course c : filteredCourses) {
            System.out.printf(
                    "- %s | %s | %.1f crédits%n",
                    c.getId(),
                    c.getName(),
                    c.getCredits()
            );
        }
    }
    // --------------------------------------------------------------------
    // 7. Voir l’horaire d’un cours pour un trimestre donné
    // --------------------------------------------------------------------
    /**
     * Affiche l’horaire d’un cours pour un trimestre donné.
     *
     * @param courseId sigle du cours
     * @param semester trimestre ciblé
     */
    public void showScheduleForCourseAndSemester(String courseId, String semester) {

        if (courseId == null || courseId.isBlank()
            || semester == null || semester.isBlank()) {
        System.out.println("Entrée invalide.");
        return;
        }

        // Normalisation (H25 -> h25)
        String normalizedSemester = semester.toLowerCase();

        Optional<Course> courseOpt =
                service.getCourseScheduleForSemester(courseId, normalizedSemester);

        if (courseOpt.isEmpty()) {
            System.out.println("Aucun horaire trouvé pour ce cours et ce trimestre.");
            return;
        }

        Course course = courseOpt.get();

        System.out.println("\n=== Horaire du cours ===");
        System.out.println("Cours : " + course.getId() + " | " + course.getName());
        System.out.println("Trimestre : " + semester);

        displaySchedule(course);

    };

    // --------------------------------------------------------------------
    // 8. Créer un ensemble de cours et voir l’horaire résultant
    // --------------------------------------------------------------------
    /**
     * Crée un ensemble de cours et affiche l’horaire résultant.
     *
     * @param courseIds liste des cours de l’ensemble
     * @param semester trimestre ciblé
     */
    public void createCourseSetAndShowSchedule(List<String> courseIds, String semester) {

        if (courseIds == null || courseIds.isEmpty() || courseIds.size() > 6) {
            System.out.println("Veuillez fournir entre 1 et 6 cours.");
            return;
        }
    
        if (semester == null || semester.isBlank()) {
            System.out.println("Trimestre invalide.");
            return;
        }
    
        try {
            // Appel au service métier
            EnsembleDeCours ensemble =
                    comparaisonService.createEnsemble(courseIds, semester);
    
            System.out.println("\n=== ENSEMBLE DE COURS ===");
            System.out.println("Cours : " + String.join(", ", courseIds));
            System.out.println("Trimestre : " + semester);
            System.out.println("Crédits totaux : " + ensemble.getCredits());
    
            // ----- Horaire -----
            System.out.println("\n=== Horaire ===");
    
            Map<String, Map<String, List<String>>> horaire =
                    ensemble.getHoraire();
    
            if (horaire == null || horaire.isEmpty()) {
                System.out.println("Aucun horaire disponible.");
            } else {
                horaire.forEach((courseId, sections) -> {
                    System.out.println("\nCours : " + courseId);
    
                    sections.forEach((section, lignes) -> {
                        System.out.println("  " + section);
                        lignes.forEach(l -> System.out.println("    - " + l));
                    });
                });
            }
    
            // ----- Conflits -----
            if (ensemble.isConflictSchedule()) {
                System.out.println("\n⚠ Conflits d’horaire détectés :");
                ensemble.getScheduleConflicts()
                        .forEach(c -> System.out.println("- " + c));
            } else {
                System.out.println("\nAucun conflit d’horaire détecté.");
            }
    
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // --------------------------------------------------------------------
    // 9. Comparer deux ensembles de cours
    // --------------------------------------------------------------------
    /**
     * Compare deux ensembles de cours pour un trimestre donné.
     *
     * @param setA premier ensemble de cours
     * @param setB second ensemble de cours
     * @param semester trimestre ciblé
     */
    public void compareCourseSets(
        List<String> setA,
        List<String> setB,
        String semester
    ) {
        // ===== Validation des entrées =====
        if (setA == null || setB == null
                || setA.size() < 2 || setA.size() > 6
                || setB.size() < 2 || setB.size() > 6) {
            System.out.println("Chaque ensemble doit contenir entre 2 et 6 cours.");
            return;
        }

        if (semester == null || semester.isBlank()) {
            System.out.println("Trimestre invalide.");
            return;
        }

        try {
            // ===== Appel au service =====
            var comparaison =
                    comparaisonService.compareEnsembles(setA, setB, semester);

            // ===== Affichage =====
            System.out.println("\n=== COMPARAISON DE DEUX ENSEMBLES DE COURS ===");
            System.out.println("Trimestre : " + semester);
            System.out.println("--------------------------------------------------");

            // ===== ENSEMBLE A =====
            System.out.println("\nEnsemble A :");
            System.out.println("Cours : " + String.join(", ", setA));
            System.out.println("Crédits totaux : " + comparaison.getCreditsTotalA());
            System.out.println("Charge de travail : " + comparaison.getChargeTravailA());
            System.out.println("Difficulté : " + comparaison.getDifficulteA());

            // ===== ENSEMBLE B =====
            System.out.println("\nEnsemble B :");
            System.out.println("Cours : " + String.join(", ", setB));
            System.out.println("Crédits totaux : " + comparaison.getCreditsTotalB());
            System.out.println("Charge de travail : " + comparaison.getChargeTravailB());
            System.out.println("Difficulté : " + comparaison.getDifficulteB());

            System.out.println("\n=== FIN COMPARAISON DES ENSEMBLES ===\n");

        } catch (IllegalArgumentException e) {
            System.out.println("Erreur lors de la comparaison : " + e.getMessage());
        }
    }

}