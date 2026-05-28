package com.diro.ift2255;

import com.diro.ift2255.Repository.AvisRepository;
import com.diro.ift2255.Repository.CourseRepository;
import com.diro.ift2255.Repository.ResultatAcademiqueRepository;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.service.AvisService;
import com.diro.ift2255.service.ComparaisonService;
import com.diro.ift2255.service.ResultatAcademiqueService;
import com.diro.ift2255.service.EligibilityService;
import com.diro.ift2255.service.ProgramService;
import com.diro.ift2255.vue.CourseVue;
import com.diro.ift2255.util.HttpClientApi;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainTestCourse {

    /**
     * Point d’entrée de l’application console CourseAdvisor.
     *
     * Initialise les services, repositories et la vue console,
     * puis lance une boucle interactive permettant à l’utilisateur
     * d’explorer les cours, comparer des cours ou des ensembles,
     * consulter des horaires et vérifier l’éligibilité à un cours.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ================== Initialisation ==================
        HttpClientApi http = new HttpClientApi();
        
        CourseRepository repo = new CourseRepository();
        CourseService courseService = new CourseService(http, repo);
        AvisRepository avisRepo = new AvisRepository();
        

        ResultatAcademiqueRepository resRepo =
                new ResultatAcademiqueRepository();
        
        ResultatAcademiqueService resultatService =
                new ResultatAcademiqueService(resRepo);

        
        AvisService avisService= new AvisService(avisRepo, courseService);

        ComparaisonService comparaisonService =
                new ComparaisonService(courseService , avisService, resultatService);

        ResultatAcademiqueRepository resultRepo =
                new ResultatAcademiqueRepository();
        ResultatAcademiqueService resultService =
                new ResultatAcademiqueService(resultRepo);

        EligibilityService eligibilityService =
                new EligibilityService(http);

        ProgramService programService = new ProgramService(http);

        CourseVue vue =
                new CourseVue(courseService, repo, comparaisonService, resultService,avisService, programService);

        // System.out.println("=== Test Console CourseVue ===");
        System.out.println("""
        ====================================================
        
                    CoursAdvisor – IFT2255
            Aide au choix de cours – DIRO, UdeM

        ====================================================
        """);

        // ================== Menu ==================

        boolean continuer = true;
        while (continuer) {

            System.out.println("""
            
            ------------------ MENU PRINCIPAL ------------------

            Choisis une option (1 ou 2 ou ...):
            
            🔍 EXPLORATION DES COURS

            1. Recherche par sigle
            2. Recherche par nom
            3. Voir détails d’un cours
            

            📊 COMPARAISONS & ÉLIGIBILITÉ

            4. Comparer des cours (2 à 4)
            5. Vérifier l’éligibilité à un cours
            

            🎓 PROGRAMMES

            6. Voir les cours offerts dans un programme
            7. Voir les cours d’un programme pour un trimestre
            

            🕒 HORAIRES

            8. Voir l’horaire d’un cours pour un trimestre
            

            📦 ENSEMBLES DE COURS

            9. Créer un ensemble de cours et voir l’horaire
            10. Comparer deux ensembles de cours
            

            0. Quitter

            ----------------------------------------------------

            """);

            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            switch (choice) {

                // -------------------------------------------------
                // 1. Recherche par sigle
                // -------------------------------------------------
                case "1":
                    System.out.print("Sigle du cours (ex: IFT2255) : ");
                    vue.searchById(scanner.nextLine().trim().toUpperCase());
                    break;

                // -------------------------------------------------
                // 2. Recherche par nom
                // -------------------------------------------------
                case "2":
                    System.out.print("Nom ou mot-clé (ex: génie logiciel) : ");
                    vue.searchByName(scanner.nextLine())
                            .forEach(c ->
                                    System.out.println("- " + c.getId() + " | " + c.getName())
                            );
                    break;

                // -------------------------------------------------
                // 3. Détails d’un cours
                // -------------------------------------------------
                case "3":
                    System.out.print("Sigle du cours (ex: IFT2255) : ");
                    vue.showDetails(scanner.nextLine().trim().toUpperCase());
                    break;

                // -------------------------------------------------
                // 4. Comparer des cours
                // -------------------------------------------------
                case "4":
                    System.out.print("Combien de cours voulez-vous comparer ? (2, 3 ou 4) : ");
                    int n;

                    try {
                        n = Integer.parseInt(scanner.nextLine().trim());
                    } catch (Exception e) {
                        System.out.println("Entrée invalide.");
                        break;
                    }

                    if (n < 2 || n > 4) {
                        System.out.println("Vous devez choisir entre 2 et 4 cours.");
                        break;
                    }

                    List<String> ids = new ArrayList<>();

                    for (int i = 1; i <= n; i++) {
                        System.out.print("Sigle du cours #" + i + " : ");
                        ids.add(scanner.nextLine().trim().toUpperCase());
                    }

                    System.out.print("Session (autumn / winter / summer) : ");
                    String term = scanner.nextLine().trim().toLowerCase();

                    vue.compareCourses(ids, term);
                    break;

                // -------------------------------------------------
                // 5. Vérifier l’éligibilité
                // -------------------------------------------------
                case "5":
                    System.out.print("Sigle du cours à vérifier (ex: IFT2255) : ");
                    String courseToCheck =
                            scanner.nextLine().trim().toUpperCase();

                    System.out.print("ID du programme (ex: Bacc en info 117510) : ");
                    String programId =
                            scanner.nextLine().trim();

                    System.out.print(
                            "Cours complétés (séparés par virgule, ex: IFT1005,IFT2255) : "
                    );
                    String input = scanner.nextLine().trim();

                    List<String> completedCourses =
                            input.isBlank()
                                    ? List.of()
                                    : List.of(input.split(","))
                                            .stream()
                                            .map(String::trim)
                                            .map(String::toUpperCase)
                                            .toList();

                    vue.checkEligibility(
                            courseToCheck,
                            programId,
                            completedCourses,
                            eligibilityService
                    );
                    break;

                // -------------------------------------------------
                // 6. Voir les cours offerts dans un programme
                // -------------------------------------------------
                case "6":
                System.out.print("ID du programme (ex: 117510) : ");
                String programId2 = scanner.nextLine().trim();

                vue.showCoursesByProgram(programId2);
                break;

                // -------------------------------------------------
                // 7. Voir les cours d’un programme pour un trimestre
                // -------------------------------------------------
                case "7":
                System.out.print("ID du programme (ex: 117510) : ");
                String programId3 = scanner.nextLine().trim();

                System.out.print("Trimestre (H25, A24, E24) : ");
                String semester = scanner.nextLine().trim().toUpperCase();

                vue.showCoursesByProgramAndSemester(programId3, semester);
                break;

                // -------------------------------------------------
                // 8. Voir l’horaire d’un cours pour un trimestre
                // -------------------------------------------------
                case "8":
                System.out.print("Sigle du cours (ex: IFT2255) : ");
                String courseId =
                        scanner.nextLine().trim().toUpperCase();

                System.out.print("Trimestre (H25, A24, E24) : ");
                String semester1 =
                        scanner.nextLine().trim().toUpperCase();

                vue.showScheduleForCourseAndSemester(courseId, semester1);
                break;

                // -------------------------------------------------
                // 9. Créer un ensemble de cours et voir l’horaire
                // -------------------------------------------------
                case "9":
                System.out.print("Trimestre (Format: H25, A24, E24) : ");
                String semesterSet =
                        scanner.nextLine().trim().toUpperCase();

                System.out.print("Combien de cours dans l’ensemble ? (max 6) : ");
                int count;

                try {
                    count = Integer.parseInt(scanner.nextLine().trim());
                } catch (Exception e) {
                    System.out.println("Entrée invalide.");
                    break;
                }

                if (count < 1 || count > 6) {
                    System.out.println("Vous devez choisir entre 1 et 6 cours.");
                    break;
                }

                List<String> courseIds = new ArrayList<>();

                for (int i = 1; i <= count; i++) {
                    System.out.print("Sigle du cours #" + i + " : ");
                    courseIds.add(scanner.nextLine().trim().toUpperCase());
                }

                vue.createCourseSetAndShowSchedule(courseIds, semesterSet);
                break;

                // -------------------------------------------------
                // 10. Comparer deux ensembles de cours (BONUS)
                // -------------------------------------------------
                case "10":
                System.out.print("Trimestre (H25, A24, E24) : ");
                String semesterCompare =
                        scanner.nextLine().trim().toUpperCase();

                // ----- Ensemble A -----
                System.out.print("Nombre de cours dans l’ensemble A (max 6) : ");
                int countA;

                try {
                    countA = Integer.parseInt(scanner.nextLine().trim());
                } catch (Exception e) {
                    System.out.println("Entrée invalide.");
                    break;
                }

                if (countA < 1 || countA > 6) {
                    System.out.println("Nombre invalide.");
                    break;
                }

                List<String> setA = new ArrayList<>();
                for (int i = 1; i <= countA; i++) {
                    System.out.print("Cours A #" + i + " : ");
                    setA.add(scanner.nextLine().trim().toUpperCase());
                }

                // ----- Ensemble B -----
                System.out.print("Nombre de cours dans l’ensemble B (max 6) : ");
                int countB;

                try {
                    countB = Integer.parseInt(scanner.nextLine().trim());
                } catch (Exception e) {
                    System.out.println("Entrée invalide.");
                    break;
                }

                if (countB < 1 || countB > 6) {
                    System.out.println("Nombre invalide.");
                    break;
                }

                List<String> setB = new ArrayList<>();
                for (int i = 1; i <= countB; i++) {
                    System.out.print("Cours B #" + i + " : ");
                    setB.add(scanner.nextLine().trim().toUpperCase());
                }

                vue.compareCourseSets(setA, setB, semesterCompare);
                break;


                // -------------------------------------------------
                // 0. Quitter
                // -------------------------------------------------
                case "0":
                    System.out.println("Bye!");
                    return;

                default:
                    System.out.println("Option invalide.");
            }

            System.out.print("\nVoulez-vous choisir une autre option ? (oui/non) : ");
            String reponse = scanner.nextLine().trim().toLowerCase();

            if (!reponse.equals("oui")) {
                System.out.println("Bye!");
                break;
        }
    }
}
}