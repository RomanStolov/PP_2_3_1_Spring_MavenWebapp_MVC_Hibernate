package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOEntityManagerImpl implements UserDAO {
    private final EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserDAOEntityManagerImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<User> getAllUsers() {
        String hql = "SELECT u FROM User u";
        TypedQuery<User> typedQuery = entityManager.createQuery(hql, User.class);
        return typedQuery.getResultList();
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    /**
     * Дополнительно два варианта реализации метода "getUser"
     * <p>
     * // Вариант №2 - Работает !!!
     *
     * @Override public User getUser(Long id) {
     * String hql = "SELECT u FROM User u WHERE u.id=:id";
     * TypedQuery<User> typedQuery = entityManager.createQuery(
     * "SELECT u FROM User u WHERE u.id=:id", User.class);
     * typedQuery.setParameter("id", id);
     * return typedQuery.getResultList().stream().findAny().orElse(null);
     * }
     * <p>
     * // Вариант №3 - Работает !!!
     * @Override public User getUser(Long id) {
     * String hql = "SELECT u FROM User u WHERE u.id=?1";
     * TypedQuery<User> typedQuery = entityManager.createQuery(hql, User.class);
     * typedQuery.setParameter(1, id);
     * return typedQuery.getSingleResult();
     * }
     */

    // Вариант №1 - Работает !!!
    @Override
    public User getUser(Long id) {
        User returnUser = entityManager.find(User.class, id);
        entityManager.detach(returnUser);
        return returnUser;
    }

    /**
     * Дополнительно вариант реализации метода "updateUser"
     * <p>
     * // Вариант №2 - Работает !!!
     *
     * @Override public void updateUser(Long id, User newUser) {
     * User findUser = entityManager.find(User.class, id);
     * entityManager.detach(findUser);
     * findUser.setName(newUser.getName());
     * findUser.setSurname(newUser.getSurname());
     * findUser.setAge(newUser.getAge());
     * findUser.setEmail(newUser.getEmail());
     * entityManager.merge(findUser);
     * }
     */

    // Вариант №1 - Работает !!!
    @Override
    public void updateUser(Long id, User newUser) {
        User findUser = getUser(id);
        findUser.setName(newUser.getName());
        findUser.setSurname(newUser.getSurname());
        findUser.setAge(newUser.getAge());
        findUser.setEmail(newUser.getEmail());
        entityManager.merge(findUser);
    }

    @Override
    public void deleteUser(Long id) {
        User deleteUser = entityManager.find(User.class, id);
        entityManager.remove(deleteUser);
    }

}
