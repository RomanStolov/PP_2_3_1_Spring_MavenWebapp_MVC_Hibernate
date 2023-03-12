package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOEntityManagerImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    // Дал имя бину и указал явно какой использовать, так как среда разработки сказала что существует
    // два варианта при @Autoware
    @Autowired
    public UserDAOEntityManagerImpl(@Qualifier("thisEntityManager") EntityManager entityManager) {
//    public UserDAOEntityManagerImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery(
                        "SELECT u FROM User u", User.class)
                .getResultList();
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUser(Long id) {
        TypedQuery<User> typedQuery = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.id=:id", User.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getResultList().stream().findAny().orElse(null);
    }

    @Override
    public void updateUser(Long id, User newUser) {
        User oldUser = getUser(id);
        oldUser.setName(newUser.getName());
        oldUser.setSurname(newUser.getSurname());
        oldUser.setAge(newUser.getAge());
        oldUser.setEmail(newUser.getEmail());
    }

    @Override
    public void deleteUser(Long id) {
        User deleteUser = getUser(id);
        entityManager.remove(deleteUser);
    }

}
