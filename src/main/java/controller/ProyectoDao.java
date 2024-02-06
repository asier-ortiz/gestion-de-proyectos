package controller;

import model.Proveedor;
import model.Proyecto;
import Enums.SearchType;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.DialogManagerUtil;
import util.HibernateUtil;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProyectoDao implements Dao<Proyecto> {

    @Override
    public String save(final Proyecto proyecto) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            String code = (String) session.save(proyecto);
            tx.commit();
            DialogManagerUtil.showInfoDialog("Proyecto insertado correctamente");
            return code;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible insertar el proyecto");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Proyecto> get(final String code) {
        Transaction tx = null;
        Proyecto retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.get(Proyecto.class, code);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible recuperar la información del proyecto");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    public Optional<Proyecto> getInitilized(final String code) {
        Transaction tx = null;
        Proyecto retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.get(Proyecto.class, code);
            Hibernate.initialize(retrieved.getGestiones());
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible recuperar la información del proyecto");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    public Optional<Proyecto> getByIndex(int index) {
        Transaction tx = null;
        Proyecto retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.createQuery("from Proyecto", Proyecto.class)
                    .setFirstResult(index)
                    .setMaxResults(1)
                    .getSingleResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido recuperar la información del proyecto");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    @Override
    public List<Proyecto> getAll() {
        Transaction tx = null;
        List<Proyecto> proyectos = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            proyectos = session.createQuery("from Proyecto ", Proyecto.class).list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return proyectos;
    }

    @Override
    public ArrayList<Proyecto> search(final SearchType searchType, final String searchValue) {
        Transaction tx = null;
        List<Proyecto> list = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            list = session.createQuery("from " + searchType.getaClass().getSimpleName() +
                            " where " + searchType.getTableColumn() +
                            " like " + "'" + "%" + searchValue + "%" + "'"
                    , Proyecto.class).getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean update(final Proyecto proyecto) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(proyecto);
            tx.commit();
            DialogManagerUtil.showInfoDialog("Proyecto actualizado correctamente");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible actualizar el proyecto");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(final Object code) {
        Object[] options = {"Si", "No"};
        int reply = DialogManagerUtil.showOptionDialog(
                options,
                "¿Desea eliminar este proyecto?",
                "¡Atención!",
                JOptionPane.YES_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                1);
        if (reply == JOptionPane.YES_OPTION) {
            Transaction tx = null;
            Proyecto proyecto;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                tx = session.beginTransaction();
                proyecto = session.get(Proyecto.class, (Serializable) code);
                session.delete(proyecto);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                if (e.getClass().equals(javax.persistence.PersistenceException.class)) {
                    DialogManagerUtil.showErrorDialog("Error : No es posible eliminar el proyecto." +
                            "\nSu clave primaria es referenciada en otras tablas");
                }
                e.printStackTrace();
            }
        }
        return false;
    }

    public ArrayList<String> getAllCodes() {
        Transaction tx = null;
        List<String> list = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            list = session.createQuery("select codigo from Proyecto", String.class).getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return new ArrayList<>();
    }

    public int getRowCount() {
        Transaction tx = null;
        long count = 0;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            count = (long) session.createQuery("select count(*) from Proyecto ").getSingleResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return (int) count;
    }

    public int getRowIndex(String code) {
        Transaction tx = null;
        List<String> list = null;
        int index = 0;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            list = session.createQuery("select codigo from Proyecto", String.class).getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (list != null) {
            index = list.indexOf(code);
        }
        return index;
    }
}