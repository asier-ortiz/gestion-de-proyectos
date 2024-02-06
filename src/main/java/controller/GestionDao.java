package controller;

import model.Gestion;
import Enums.SearchType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.DialogManagerUtil;
import util.HibernateUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestionDao implements Dao<Gestion> {

    @Override
    public Optional<Gestion> save(Gestion gestion) {
        Transaction tx = null;
        Gestion saved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            saved = (Gestion) session.save(gestion);
            tx.commit();
            DialogManagerUtil.showInfoDialog("Gestión insertada correctamente");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            if (e.getClass().equals(javax.persistence.PersistenceException.class)) {
                DialogManagerUtil.showErrorDialog("Error : No es posible insertar la gestión." +
                        "\nClave primaria duplicada");
            } else {
                DialogManagerUtil.showErrorDialog("Error : No ha sido posible insertar la gestión");
            }
            e.printStackTrace();
        }
        return Optional.ofNullable(saved);
    }

    @Override
    public Optional<Gestion> get(String code) {
        Transaction tx = null;
        Gestion retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.get(Gestion.class, code);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible recuperar la información de la gestión");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    @Override
    public List<Gestion> getAll() {
        Transaction tx = null;
        List<Gestion> gestiones = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            gestiones = session.createQuery("from Gestion ", Gestion.class).list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return gestiones;
    }

    public List<Gestion> getInRange(int startIndex, int endIndex) {
        Transaction tx = null;
        List<Gestion> gestiones = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            gestiones = session.createQuery("from Gestion", Gestion.class)
                    .setFirstResult(startIndex)
                    .setMaxResults(endIndex)
                    .list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return gestiones;
    }

    @Override
    public ArrayList<Gestion> search(SearchType searchType, String searchValue) {
        return null;
    }

    @Override
    public boolean update(Gestion gestion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(gestion);
            tx.commit();
            DialogManagerUtil.showInfoDialog("Gestión actualizada correctamente");
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible actualizar la gestión");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Object gestion) {
        Object[] options = {"Si", "No"};
        int reply = DialogManagerUtil.showOptionDialog(
                options,
                "¿Desea eliminar esta gestión?",
                "¡Atención!",
                JOptionPane.YES_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                1);
        if (reply == JOptionPane.YES_OPTION) {
            Transaction tx = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                tx = session.beginTransaction();
                session.delete(gestion);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                DialogManagerUtil.showErrorDialog("Error : No ha sido posible eliminar la gestión");
                e.printStackTrace();
            }
        }
        return false;
    }

    public long getRowCount() {
        Transaction tx = null;
        long count = 0;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            count = (long) session.createQuery("select count(*) from Gestion ").getSingleResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return count;
    }
}