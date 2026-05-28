package com.diro.ift2255.config;

import com.diro.ift2255.Repository.AvisRepository;
import com.diro.ift2255.Repository.UserRepository;
import com.diro.ift2255.controller.AvisController;
import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.controller.UserController;
import com.diro.ift2255.controller.ComparaisonController;
import com.diro.ift2255.service.UserService;
import com.diro.ift2255.service.AvisService;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.service.ComparaisonService;
import com.diro.ift2255.Repository.ResultatAcademiqueRepository;
import com.diro.ift2255.controller.ResultatAcademiqueController;
import com.diro.ift2255.service.ResultatAcademiqueService;
import com.diro.ift2255.util.HttpClientApi;

import io.javalin.Javalin;

public class Routes {

    public static void register(Javalin app) {
        registerUserRoutes(app);
        registerCourseRoutes(app);
        registerAvisRoutes(app);
        registerComparaisonRoutes(app);
        registerResultatAcademiqueRoutes(app);
    }

    private static void registerUserRoutes(Javalin app) {
        UserRepository userRepo = new UserRepository();
        UserService userService = new UserService(userRepo);
        UserController userController = new UserController(userService);

        app.get("/users", userController::getAllUsers);
        app.get("/users/{id}", userController::getUserById);
        app.post("/users", userController::createUser);
        app.put("/users/{id}", userController::updateUser);
        app.delete("/users/{id}", userController::deleteUser);
    }

    private static void registerCourseRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseController courseController = new CourseController(courseService);

        app.get("/courses", courseController::getAllCourses);
        app.get("/courses/{id}", courseController::getCourseById);
    }

    private static void registerResultatAcademiqueRoutes(Javalin app) {

        ResultatAcademiqueRepository repo =
                new ResultatAcademiqueRepository();
    
        ResultatAcademiqueService service =
                new ResultatAcademiqueService(repo);
    
        ResultatAcademiqueController controller =
                new ResultatAcademiqueController(service);
    
        app.get("/results", controller::getAllResults);
        app.get("/results/{sigle}", controller::getResultBySigle);
    }

    private static void registerAvisRoutes(Javalin app) {
        HttpClientApi http = new HttpClientApi();          
        CourseService courseService = new CourseService(http);
        AvisRepository repo = new AvisRepository();
        AvisService service = new AvisService(repo, courseService);
        AvisController controller = new AvisController(service);

        app.post("/avis", controller::createAvis);
        app.get("/avis/{coursCode}", controller::getAvisByCours);
        app.get("/avis/{coursCode}/filtrer", controller::filtrerAvis);
   
    }

    private static void registerComparaisonRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        AvisRepository avisRepo = new AvisRepository();
        AvisService avisService = new AvisService(avisRepo,courseService);
        ResultatAcademiqueService resultatService =
        new ResultatAcademiqueService(new ResultatAcademiqueRepository());
        ComparaisonService comparaisonService = new ComparaisonService(courseService, avisService, resultatService);
        ComparaisonController comparaisonController = new ComparaisonController(comparaisonService);
        app.post("/comparaisonDesCours", comparaisonController::compare);
        app.post("/creer/ensemble", comparaisonController::createEnsemble);
        app.post("/comparaison/ensembles", comparaisonController::compareEnsembles);
}
} 