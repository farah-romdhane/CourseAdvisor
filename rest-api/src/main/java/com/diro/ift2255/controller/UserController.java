package com.diro.ift2255.controller;

import java.util.List;

import com.diro.ift2255.model.User;
import com.diro.ift2255.service.UserService;
import com.diro.ift2255.util.ResponseUtil;
import com.diro.ift2255.util.ValidationUtil;

import io.javalin.http.Context;

/**
 * Contrôleur responsable de la gestion des utilisateurs.
 *
 * Cette classe expose les opérations HTTP permettant de consulter,
 * créer, modifier et supprimer des utilisateurs.
 */


public class UserController {
    // Service qui contient la logique métier pour la manipulation des utilisateurs et la communication avec les services externes
    private final UserService service;
    
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Récupère la liste de tous les utilisateurs.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void getAllUsers(Context ctx) {
        List<User> users = service.getAllUsers();
        ctx.json(users);
    }

    /**
     * Récupère un utilisateur spécifique par son ID.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void getUserById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        User user = service.getUserById(id);

        if (user!= null) {
            ctx.json(user);
        } else {
            ctx.status(404).json(ResponseUtil.formatError("Aucun utilisateur ne correspond à l'ID: " + id));
        }
    }

    /**
     * Crée un nouvel utilisateur avec les données passées dans le body.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void createUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);

        // Verfication si email est valide
        if (!ValidationUtil.isEmail(user.getEmail())) {
            ctx.status(400).json(ResponseUtil.formatError("Invalid email format"));
            return;
        }
        try {
            service.createUser(user);
            ctx.status(201).json(user);
        } catch (IllegalArgumentException e){
            ctx.status(400).json(e.getMessage());
        }
    }

    /**
     * Met à jour un utilisateur existant avec les données passées dans le body.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void updateUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        User updated = ctx.bodyAsClass(User.class);

        // Vérifier email valide
    if (!ValidationUtil.isEmail(updated.getEmail())) {
        ctx.status(400).json("Invalid email format");
        return;
    }

    try {
        User result = service.updateUser(id, updated);

        if (result == null) {
            ctx.status(404).json("Utilisateur non trouvé");
            return;
        }
        ctx.json(updated);

    } catch (IllegalArgumentException e) {
        ctx.status(400).json(e.getMessage());
    }
        
        
    }

    /**
     * Supprime un utilisateur existant.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void deleteUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        boolean deleted = service.deleteUser(id);
        
        if (!deleted) {
            ctx.status(404).json(ResponseUtil.formatError("Utilisateur non trouvé"));
        } else {
            ctx.status(204);
        }
    }
}
