package com.diro.ift2255.service;

import java.util.List;

import com.diro.ift2255.Repository.UserRepository;
import com.diro.ift2255.model.User;

/**
 * Service responsable de la gestion des utilisateurs.
 *
 * Cette classe contient la logique métier liée aux opérations
 * de création, consultation, mise à jour et suppression des utilisateurs.
 */


public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }


    /**
     * Retourne la liste de tous les utilisateurs.
     * @return liste des utilisateurs
    */

    public List<User> getAllUsers(){
        return repo.findAll();
    }

    /**
     * Retourne un utilisateur à partir de son identifiant.
     * @param id identifiant de l'utilisateur
     * @return l'utilisateur correspondant ou null s'il n'existe pas
    */

    public User getUserById(int id) {
        return repo.findById(id);
    }


    /**
     * Crée un nouvel utilisateur.
     *
     * @param user utilisateur à créer
     * @return l'utilisateur créé
     * @throws IllegalArgumentException si l'adresse courriel existe déjà
     */

    public User createUser(User user) {

        // Vérifier si l'email existe déjà 
        for (User u : repo.findAll()) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new IllegalArgumentException("Email existe déjà");
            }
        }

        // Générer ID
        int newId = generateNewId();
        user.setId(newId);

        return repo.save(user);
    }

     /**
     * Met à jour un utilisateur existant.
     *
     * @param id identifiant de l'utilisateur à modifier
     * @param updatedUser nouvelles données de l'utilisateur
     * @return l'utilisateur mis à jour ou null s'il n'existe pas
     * @throws IllegalArgumentException si l'adresse courriel est déjà utilisée
     */


    public User updateUser(int id, User updatedUser) {
        User existing = repo.findById(id);

        if (existing == null) return null;

        // vérifier que l'email n'est pas utilisé par un autre user
        for (User u : repo.findAll()) {
            if (u.getEmail().equalsIgnoreCase(updatedUser.getEmail()) && u.getId() != id) {
                throw new IllegalArgumentException("Email déjà utilisé par un autre utilisateur");
            }
        }

        updatedUser.setId(id);
        return repo.update(updatedUser);
    }

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id identifiant de l'utilisateur
     * @return true si l'utilisateur a été supprimé, false sinon
    */

    public boolean deleteUser(int id) {
        return repo.delete(id);
    }

    /**
     * Génère un nouvel identifiant unique pour un utilisateur.
     *
     * @return nouvel identifiant
    */
    private int generateNewId() {
        int max = 0;
        for (User u : repo.findAll()) {
            if (u.getId() > max) max = u.getId();
        }
        return max + 1;
    }

}
