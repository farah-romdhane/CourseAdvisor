package com.diro.ift2255.Repository;

import java.util.List;

import com.diro.ift2255.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.ArrayList;


    /**
     * Repository responsable de la gestion des utilisateurs.
     * Cette classe permet de charger, sauvegarder et manipuler les utilisateurs
     * stockés dans le fichier JSON.
     */

public class UserRepository {

    /** Permet de lire et écrire le fichier JSON */
    private final ObjectMapper mapper = new ObjectMapper();

    
    /** Chemin du fichier contenant les utilisateurs */
    private final String filePath = "rest-api/data/users.json";

     /** Liste des users chargés en mémoire */ 
    private List<User> users;

     /**
     * Constructeur du repository.
     * Charge les utilisateurs depuis le fichier au démarrage.
     */
    public UserRepository() {
        loadUsersFromFile(); // on lit les utilisateurs au demarrage
    }

    /**
     * Charge les utilisateurs depuis le fichier JSON.
     * Si le fichier n'existe pas ou qu'une erreur survient,
     * une liste vide est créée.
    */
    private void loadUsersFromFile() {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                users = new ArrayList<>();
                return;
            }

            // Lecture JSON 
            users = mapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
    }

    /**
     * Sauvegarde la liste des utilisateurs dans le fichier JSON.
    */
    private void saveUsersToFile() {
    try {
        mapper.writeValue(new File(filePath), users);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    /**
     * Retourne tous les utilisateurs.
     *
     * @return la liste des utilisateurs
     */

    public List<User> findAll() {
        return users;
    }


     /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return l'utilisateur correspondant ou null s'il n'existe pas
     */
    public User findById(int id) {
        for (User u : users) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }


    /**
     * Ajoute un nouvel utilisateur.
     *
     * @param user l'utilisateur à ajouter
     * @return l'utilisateur ajouté
     */
    
    public User save(User user) {
    users.add(user);
    saveUsersToFile();
    return user;
}

    /**
     * Supprime un utilisateur selon son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return true si la suppression a réussi, false sinon
     */

    public boolean delete(int id) {
    for (int i = 0; i < users.size(); i++) {
        if (users.get(i).getId() == id) {
            users.remove(i);
            saveUsersToFile();
            return true;
        }
    }
    return false;
}

    /**
     * Met à jour un utilisateur existant.
     *
     * @param updatedUser l'utilisateur avec les nouvelles informations
     * @return l'utilisateur mis à jour ou null si l'ID n'existe pas
     */

    public User update(User updatedUser) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == updatedUser.getId()) {
                    users.set(i, updatedUser);
                    saveUsersToFile();
                    return updatedUser;
                }
            }
            return null;
        }

}
