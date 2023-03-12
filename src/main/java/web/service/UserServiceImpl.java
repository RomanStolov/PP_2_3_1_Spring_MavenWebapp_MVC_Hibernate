package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDAO;
import web.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    // Использование сервисом DAO: UserDAOJdbcTemplateImpl
//    @Autowired
//    public UserServiceImpl(@Qualifier("userDAOJdbcTemplateImpl") UserDAO userDAO) {
//        this.userDAO = userDAO;
//    }

    // Использование сервисом DAO: UserDAOEntityManagerImpl
    @Autowired
    public UserServiceImpl(@Qualifier("userDAOEntityManagerImpl") UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userDAO.getUser(id);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User newUser) {
        userDAO.updateUser(id, newUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }

}
