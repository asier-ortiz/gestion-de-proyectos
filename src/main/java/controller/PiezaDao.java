package controller;

import Enums.SearchType;
import model.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.DialogManagerUtil;
import util.HibernateUtil;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

public class PiezaDao implements Dao<Pieza> {

    @Override
    public String save(final Pieza pieza) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            String code = (String) session.save(pieza);
            tx.commit();
            DialogManagerUtil.showInfoDialog("Pieza insertada correctamente");
            return code;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible insertar la pieza");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Pieza> get(final String code) {
        Transaction tx = null;
        Pieza retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.get(Pieza.class, code);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible recuperar la información de la pieza");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    public Optional<Pieza> getInitilized(final String code) {
        Transaction tx = null;
        Pieza retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.get(Pieza.class, code);
            Hibernate.initialize(retrieved.getGestiones());
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible recuperar la información de la pieza");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    public Optional<Pieza> getByIndex(int index) {
        Transaction tx = null;
        Pieza retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.createQuery("from Pieza", Pieza.class)
                    .setFirstResult(index)
                    .setMaxResults(1)
                    .getSingleResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido recuperar la información de la pieza");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    @Override
    public List<Pieza> getAll() {
        Transaction tx = null;
        List<Pieza> piezas = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            piezas = session.createQuery("from Pieza", Pieza.class).list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return piezas;
    }

    public List<Pieza> getAllInitialized() {
        Transaction tx = null;
        List<Pieza> piezas = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            piezas = session.createQuery("from Pieza", Pieza.class).list();
            piezas.forEach(pieza -> Hibernate.initialize(pieza.getGestiones()));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return piezas;
    }

    @Override
    public ArrayList<Pieza> search(SearchType searchType, String searchValue) {
        Transaction tx = null;
        List<Pieza> list = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            list = session.createQuery("from " + searchType.getaClass().getSimpleName() +
                            " where " + searchType.getTableColumn() +
                            " like " + "'" + "%" + searchValue + "%" + "'"
                    , Pieza.class).getResultList();
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
    public boolean update(final Pieza pieza) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(pieza);
            tx.commit();
            DialogManagerUtil.showInfoDialog("Pieza actualizada correctamente");
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible actualizar la pieza");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(final Object code) {
        Object[] options = {"Si", "No"};
        int reply = DialogManagerUtil.showOptionDialog(
                options,
                "¿Desea eliminar a este proveedor?",
                "¡Atención!",
                JOptionPane.YES_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                1);
        if (reply == JOptionPane.YES_OPTION) {
            Transaction tx = null;
            Pieza pieza;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                tx = session.beginTransaction();
                pieza = session.get(Pieza.class, (Serializable) code);
                session.delete(pieza);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                if (e.getClass().equals(javax.persistence.PersistenceException.class)) {
                    DialogManagerUtil.showErrorDialog("Error : No es posible eliminar la pieza." +
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
            list = session.createQuery("select codigo from Pieza", String.class).getResultList();
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
            count = (long) session.createQuery("select count(*) from Pieza ").getSingleResult();
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
            list = session.createQuery("select codigo from Pieza", String.class).getResultList();
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

    public Set<Proyecto> getProjectsHavingPart(Pieza pieza) {
        Set<Proyecto> proyectos = new HashSet<>();
        pieza.getGestiones().forEach(gestion -> proyectos.add(gestion.getProyecto()));
        return proyectos;
    }

    public int getTotalAmmountOfProjectsHavingPart(Pieza pieza) {
        Set<Proyecto> proyectos = new HashSet<>();
        pieza.getGestiones().forEach(gestion -> proyectos.add(gestion.getProyecto()));
        return proyectos.size();
    }


    public Set<Proveedor> getProvidersSuppliyingPart(Pieza pieza) {
        Set<Proveedor> proveedores = new HashSet<>();
        pieza.getGestiones().forEach(gestion -> proveedores.add(gestion.getProveedor()));
        return proveedores;
    }


    public int getTotalAmmountOfProvidersSuppliyingPart(Pieza pieza) {
        Set<Proyecto> proyectos = new HashSet<>();
        pieza.getGestiones().forEach(gestion -> proyectos.add(gestion.getProyecto()));
        return proyectos.size();
    }


    public int getTotalAmountSuppliedOfPart(Pieza pieza) {
        int amount = 0;
        amount += pieza.getGestiones()
                .stream()
                .mapToInt(Gestion::getCantidad).sum();
        return amount;
    }

    public int getTotalAmountSuppliedOfPartForProject(Pieza pieza, Proyecto proyecto) {
        return pieza.getGestiones()
                .stream()
                .filter(gestion -> gestion.getProyecto().equals(proyecto))
                .mapToInt(Gestion::getCantidad)
                .sum();
    }

    public int getTotalAmountOfPartInProject(Pieza pieza, Proyecto proyecto) {
        return pieza.getGestiones()
                .stream()
                .filter(gestion -> gestion.getProyecto().equals(proyecto))
                .mapToInt(Gestion::getCantidad)
                .sum();
    }

    public int getTotalAmountOfPartsSuppliedByProviderForProject(Pieza pieza, Proveedor proveedor, Proyecto proyecto) {
        return pieza.getGestiones()
                .stream()
                .filter(gestion -> gestion.getProveedor().equals(proveedor) && gestion.getProyecto().equals(proyecto))
                .mapToInt(Gestion::getCantidad)
                .sum();
    }
}