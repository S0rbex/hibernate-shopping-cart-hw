package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tr = session.beginTransaction();
            session.persist(shoppingCart);
            tr.commit();
        } catch (RuntimeException e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new DataProcessingException("Cant add new shopping cart", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {
            return session.createQuery(
                            "from ShoppingCart sc "
                                    + "left join fetch sc.tickets "
                                    + "where sc.user = :user", ShoppingCart.class)
                    .setParameter("user", user)
                    .uniqueResultOptional();
        } catch (RuntimeException e) {
            throw new DataProcessingException("Cant get shopping cart by this user: " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tr = session.beginTransaction();
            session.merge(shoppingCart);
            tr.commit();
        } catch (RuntimeException e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new DataProcessingException("Cant update shopping cart " + shoppingCart, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
