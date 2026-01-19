package mate.academy.dao.impl;

import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TicketDaoImpl implements TicketDao {
    @Override
    public Ticket add(Ticket ticket) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tr = session.beginTransaction();
            session.persist(ticket);
            tr.commit();
        } catch (RuntimeException e) {
            if (tr != null) {
                tr.rollback();
            }
            throw new DataProcessingException("Cant add new ticket", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return ticket;
    }
}
