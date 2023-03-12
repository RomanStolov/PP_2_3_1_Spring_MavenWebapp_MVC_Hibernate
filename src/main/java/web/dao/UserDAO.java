package web.dao;

import web.model.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUsers();

    void saveUser(User user);

    User getUser(Long id);

    void updateUser(Long id, User user);

    void deleteUser(Long id);

}
