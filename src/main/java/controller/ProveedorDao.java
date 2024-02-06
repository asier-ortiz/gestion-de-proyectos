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

public class ProveedorDao implements Dao<Proveedor> {

    @Override
    public String save(final Proveedor proveedor) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            String code = (String) session.save(proveedor);
            tx.commit();
            DialogManagerUtil.showInfoDialog("Proveedor insertado correctamente");
            return code;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible insertar al proveedor");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Proveedor> get(final String code) {
        Transaction tx = null;
        Proveedor retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.get(Proveedor.class, code);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible recuperar la información del proveedor");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    public Optional<Proveedor> getInitilized(final String code) {
        Transaction tx = null;
        Proveedor retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.get(Proveedor.class, code);
            Hibernate.initialize(retrieved.getGestiones());
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible recuperar la información del proveedor");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    public Optional<Proveedor> getByIndex(int index) {
        Transaction tx = null;
        Proveedor retrieved = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            retrieved = session.createQuery("from Proveedor ", Proveedor.class)
                    .setFirstResult(index)
                    .setMaxResults(1)
                    .getSingleResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido recuperar la información del proveedor");
            e.printStackTrace();
        }
        return Optional.ofNullable(retrieved);
    }

    @Override
    public List<Proveedor> getAll() {
        Transaction tx = null;
        List<Proveedor> proveedores = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            proveedores = session.createQuery("from Proveedor ", Proveedor.class).list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return proveedores;
    }

    public List<Proveedor> getAllInitialized() {
        Transaction tx = null;
        List<Proveedor> proveedores = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            proveedores = session.createQuery("from Proveedor", Proveedor.class).list();
            proveedores.forEach(proveedor -> Hibernate.initialize(proveedor.getGestiones()));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return proveedores;
    }

    @Override
    public ArrayList<Proveedor> search(final SearchType searchType, final String searchValue) {
        Transaction tx = null;
        List<Proveedor> list = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            list = session.createQuery("from " + searchType.getaClass().getSimpleName() +
                            " where " + searchType.getTableColumn() +
                            " like " + "'" + "%" + searchValue + "%" + "'"
                    , Proveedor.class).getResultList();
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
    public boolean update(final Proveedor proveedor) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(proveedor);
            tx.commit();
            DialogManagerUtil.showInfoDialog("Proveedor actualizado correctamente");
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            DialogManagerUtil.showErrorDialog("Error : No ha sido posible actualizar al proveedor");
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
            Proveedor proveedor;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                tx = session.beginTransaction();
                proveedor = session.get(Proveedor.class, (Serializable) code);
                session.delete(proveedor);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                if (e.getClass().equals(javax.persistence.PersistenceException.class)) {
                    DialogManagerUtil.showErrorDialog("Error : No es posible eliminar el proveedor." +
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
            list = session.createQuery("select codigo from Proveedor", String.class).getResultList();
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
            count = (long) session.createQuery("select count(*) from Proveedor ").getSingleResult();
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
            list = session.createQuery("select codigo from Proveedor", String.class).getResultList();
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

    public Set<Proyecto> getProjectsHavingProvider(Proveedor proveedor) {
        Set<Proyecto> proyectos = new HashSet<>();
        proveedor.getGestiones().forEach(gestion -> proyectos.add(gestion.getProyecto()));
        return proyectos;
    }

    public int getTotalAmmountOfProjectsHavingProvider(Proveedor proveedor) {
        Set<Proyecto> proyectos = new HashSet<>();
        proveedor.getGestiones().forEach(gestion -> proyectos.add(gestion.getProyecto()));
        return proyectos.size();
    }

    public Set<Pieza> getPartsSuppliedByProviderForProject(Proveedor proveedor, Proyecto proyecto) {
        Set<Pieza> piezas = new HashSet<>();
        proveedor.getGestiones().forEach(gestion -> {
            if (gestion.getProyecto().equals(proyecto)) {
                piezas.add(gestion.getPieza());
            }
        });
        return piezas;
    }

    public int getTotalAmountOfPartsSuppliedBy(Proveedor proveedor) {
        int amount = 0;
        amount += proveedor.getGestiones()
                .stream()
                .mapToInt(Gestion::getCantidad)
                .sum();
        return amount;
    }

    public int getTotalAmountOfDistinctPartsSuppliedBy(Proveedor proveedor) {
        Set<Pieza> piezas = new HashSet<>();
        proveedor.getGestiones().forEach(gestion -> piezas.add(gestion.getPieza()));
        return piezas.size();
    }
}