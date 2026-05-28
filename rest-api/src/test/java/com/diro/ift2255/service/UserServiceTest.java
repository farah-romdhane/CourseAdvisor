package com.diro.ift2255.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.diro.ift2255.Repository.UserRepository;
import com.diro.ift2255.model.User;

class UserServiceTest {

   // Fake repository juste pour les tests
    private UserRepository makeFakeRepo() {
        return new UserRepository() {

            List<User> users = new ArrayList<>();

            @Override
            public List<User> findAll() {
                return users;
            }

            @Override
            public User findById(int id) {
                for (User u : users) {
                    if (u.getId() == id) return u;
                }
                return null;
            }

            @Override
            public User save(User user) {
                users.add(user);
                return user;
            }

            @Override
            public boolean delete(int id) {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId() == id) {
                        users.remove(i);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public User update(User u) {
                delete(u.getId());
                users.add(u);
                return u;
            }
        };
    }

    @Test
    void testCreateUser() {
        UserService service = new UserService(makeFakeRepo());
        User u = new User(0, "Sarah", "sarah@umontreal.ca");
        User created = service.createUser(u);

        assertEquals(1, created.getId());
        assertEquals("Sarah", created.getName());
    }

        @Test
    void testDuplicateEmail() {
        UserService service = new UserService(makeFakeRepo());

        // premier utilisateur
        service.createUser(new User(0, "Alice Tremblay", "alice@umontreal.ca"));

        // on essaye de créer un user avec le même email
        assertThrows(IllegalArgumentException.class, () -> 
            service.createUser(new User(0, "Bob Gagnon", "alice@umontreal.ca"))
        );
    }


    @Test
    void testGetUserNotFound() {
        UserService service = new UserService(makeFakeRepo());
        assertNull(service.getUserById(123)); // ID existe pas
    }

    @Test
    void testUpdateUser() {
        UserService service = new UserService(makeFakeRepo());

        service.createUser(new User(0, "Alex Tremblay", "alex.tremblay@umontreal.ca"));

        // on met à jour avec un nouveau nom et email
        User updated = new User(0, "Marie Dupont", "marie.dupont@umontreal.ca");
        User result = service.updateUser(1, updated);

        assertEquals("Marie Dupont", result.getName());
        assertEquals("marie.dupont@umontreal.ca", result.getEmail());
        assertEquals(1, result.getId());
    }

    @Test
    void testDeleteUser() {
        UserService service = new UserService(makeFakeRepo());

        // On crée un utilisateur
        service.createUser(new User(0, "Julie Martin", "julie.martin@umontreal.ca"));

        boolean deleted = service.deleteUser(1);

        assertTrue(deleted);
    }
}
