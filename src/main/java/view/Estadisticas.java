package view;

import controller.GestionDao;
import controller.PiezaDao;
import controller.ProveedorDao;
import controller.ProyectoDao;
import gui.EstadisticaPiezasTableModel;
import gui.EstadisticaProveedorTableModel;
import gui.PaginatedTable;
import gui.PaginationDataProvider;
import model.EstadisticaPieza;
import model.EstadisticaProveedor;
import model.Pieza;
import model.Proveedor;
import util.DialogManagerUtil;
import util.HibernateUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static util.MethodsUtil.setTableLookAndFeel;

public class Estadisticas {

    private JPanel estadisticasWindow;
    private JTextField partCodeTextField;
    private JTextField partAmmountTextField;
    private JTextField partProjectCodeTextField;
    private JTextField partProjectAmmountTextField;
    private JTextField providerPartQuantityCodeTextField;
    private JTextField providerPartQuantityAmmountTextField;
    private JTextField providerProjectCodeTextField;
    private JTextField providerProjectAmmountTextField;
    private JTextField providerPartCodeTextField;
    private JTextField providerPartAmmountTextField;
    private JPanel partsStatisticsTablePanel;
    private JPanel providersStatisticsTablePanel;
    private final ProyectoDao proyectoDao;
    private final ProveedorDao proveedorDao;
    private final PiezaDao piezaDao;
    private final GestionDao gestionDao;
    private final JTable partsStatisticsTable = new JTable(EstadisticaPiezasTableModel.createDataModel());
    private PaginatedTable<EstadisticaPieza> paginatedPartStatisticsTable;
    private ArrayList<Pieza> piezas = new ArrayList<>();
    private ArrayList<EstadisticaPieza> estadisticaPiezasList = new ArrayList<>();
    private final JTable providersStatisticsTable = new JTable(EstadisticaProveedorTableModel.createDataModel());
    private PaginatedTable<EstadisticaProveedor> paginatedProviderStatisticsTable;
    private ArrayList<Proveedor> provedores = new ArrayList<>();
    private ArrayList<EstadisticaProveedor> estadisticaProveedoresList = new ArrayList<>();

    public Estadisticas() {
        proveedorDao = new ProveedorDao();
        piezaDao = new PiezaDao();
        proyectoDao = new ProyectoDao();
        gestionDao = new GestionDao();
        createEstadisticasPiezasTable();
        createEstadisticasProveedorTable();
        if (HibernateUtil.getSessionFactory() != null) {
            init();
        } else {
            DialogManagerUtil.showErrorDialog(" Error : No ha sido posible establecer una conexión con la base de datos");
        }
    }

    public JPanel getEstadisticasWindow() {
        return estadisticasWindow;
    }

    private void init() {
        updateInfo();
    }

    private void updateInfo() {

        piezas.addAll(piezaDao.getAllInitialized());
        piezas.forEach(pieza -> {
            int numeroProyectos;
            int numeroProveedores;
            int cantidadTotal;
            numeroProyectos = piezaDao.getTotalAmmountOfProjectsHavingPart(pieza);
            numeroProveedores = piezaDao.getTotalAmmountOfProvidersSuppliyingPart(pieza);
            cantidadTotal = piezaDao.getTotalAmountSuppliedOfPart(pieza);
            EstadisticaPieza estadisticaPieza = new EstadisticaPieza(
                    pieza.getCodigo(),
                    pieza.getNombre(),
                    pieza.getPrecio(),
                    numeroProyectos,
                    numeroProveedores,
                    cantidadTotal
            );
            estadisticaPiezasList.add(estadisticaPieza);
        });
        createEstadisticasPiezasTable();

        provedores.addAll(proveedorDao.getAllInitialized());
        provedores.forEach(proveedor -> {
            int numeroProyectos;
            int numeroPiezas;
            int cantidadTotal;
            numeroProyectos = proveedorDao.getTotalAmmountOfProjectsHavingProvider(proveedor);
            numeroPiezas = proveedorDao.getTotalAmountOfDistinctPartsSuppliedBy(proveedor);
            cantidadTotal = proveedorDao.getTotalAmountOfPartsSuppliedBy(proveedor);
            EstadisticaProveedor estadisticaProveedor = new EstadisticaProveedor(
                    proveedor.getCodigo(),
                    proveedor.getNombre(),
                    proveedor.getApellidos(),
                    numeroProyectos,
                    numeroPiezas,
                    cantidadTotal
            );
            estadisticaProveedoresList.add(estadisticaProveedor);
        });
        createEstadisticasProveedorTable();


        Optional<EstadisticaPieza> estadisticaPiezaMaxAmmountOptional = estadisticaPiezasList
                .stream()
                .max(Comparator.comparing(EstadisticaPieza::getCantidadTotal));
        estadisticaPiezaMaxAmmountOptional.ifPresentOrElse(estadisticaPieza -> {
                    partCodeTextField.setText(estadisticaPieza.getCodigo());
                    partAmmountTextField.setText(String.valueOf(estadisticaPieza.getCantidadTotal()));
                }, () -> {
                    partCodeTextField.setText("");
                    partAmmountTextField.setText("");
                }
        );
        Optional<EstadisticaPieza> estadisticaPiezaMaxProjectsOptional = estadisticaPiezasList
                .stream()
                .max(Comparator.comparing(EstadisticaPieza::getNumeroProyectos));
        estadisticaPiezaMaxProjectsOptional.ifPresentOrElse(estadisticaPieza -> {
                    partProjectCodeTextField.setText(estadisticaPieza.getCodigo());
                    partProjectAmmountTextField.setText(String.valueOf(estadisticaPieza.getNumeroProyectos()));
                }, () -> {
                    partProjectCodeTextField.setText("");
                    partProjectAmmountTextField.setText("");
                }
        );
        Optional<EstadisticaProveedor> estadisticaProveedorMaxPartQuantityOptional = estadisticaProveedoresList
                .stream()
                .max(Comparator.comparing(EstadisticaProveedor::getNumeroPiezas));
        estadisticaProveedorMaxPartQuantityOptional.ifPresentOrElse(estadisticaProveedor -> {
                    providerPartQuantityCodeTextField.setText(estadisticaProveedor.getCodigo());
                    providerPartQuantityAmmountTextField.setText(String.valueOf(estadisticaProveedor.getNumeroPiezas()));
                }, () -> {
                    providerPartQuantityCodeTextField.setText("");
                    providerPartQuantityAmmountTextField.setText("");
                }
        );
        Optional<EstadisticaProveedor> estadisticaProveedorMaxProjectsOptional = estadisticaProveedoresList
                .stream()
                .max(Comparator.comparing(EstadisticaProveedor::getNumeroProyectos));
        estadisticaProveedorMaxProjectsOptional.ifPresentOrElse(estadisticaProveedor -> {
                    providerProjectCodeTextField.setText(estadisticaProveedor.getCodigo());
                    providerProjectAmmountTextField.setText(String.valueOf(estadisticaProveedor.getNumeroProyectos()));
                }, () -> {
                    providerProjectCodeTextField.setText("");
                    providerProjectAmmountTextField.setText("");
                }
        );
        Optional<EstadisticaProveedor> estadisticaProveedorMaxTotalPartQuantityOptional = estadisticaProveedoresList
                .stream()
                .max(Comparator.comparing(EstadisticaProveedor::getCantidadTotal));
        estadisticaProveedorMaxTotalPartQuantityOptional.ifPresentOrElse(estadisticaProveedor -> {
                    providerPartCodeTextField.setText(estadisticaProveedor.getCodigo());
                    providerPartAmmountTextField.setText(String.valueOf(estadisticaProveedor.getCantidadTotal()));
                }, () -> {
                    providerPartCodeTextField.setText("");
                    providerPartAmmountTextField.setText("");
                }
        );
    }

    private void createEstadisticasPiezasTable() {
        partsStatisticsTablePanel.removeAll();
        setTableLookAndFeel(partsStatisticsTable, false);
        paginatedPartStatisticsTable = PaginatedTable.decorate(partsStatisticsTable, createEstadisticaPiezasTableData(), new int[]{10, 20, 30, 40, 50}, 10);
        partsStatisticsTablePanel.add(paginatedPartStatisticsTable.getContentPanel());
        partsStatisticsTablePanel.revalidate();
        partsStatisticsTablePanel.repaint();
    }

    private void createEstadisticasProveedorTable() {
        providersStatisticsTablePanel.removeAll();
        setTableLookAndFeel(providersStatisticsTable, false);
        paginatedProviderStatisticsTable = PaginatedTable.decorate(providersStatisticsTable, createEstadisticaProveedoresTableData(), new int[]{10, 20, 30, 40, 50}, 10);
        providersStatisticsTablePanel.add(paginatedProviderStatisticsTable.getContentPanel());
        providersStatisticsTablePanel.revalidate();
        providersStatisticsTablePanel.repaint();
    }

    private PaginationDataProvider<EstadisticaPieza> createEstadisticaPiezasTableData() {

        return new PaginationDataProvider<>() {
            @Override
            public int getTotalRowCount() {
                return estadisticaPiezasList.size();
            }

            @Override
            public List<EstadisticaPieza> getRows(int startIndex, int endIndex) {
                return estadisticaPiezasList.subList(startIndex, endIndex);
            }
        };
    }

    private PaginationDataProvider<EstadisticaProveedor> createEstadisticaProveedoresTableData() {

        return new PaginationDataProvider<>() {
            @Override
            public int getTotalRowCount() {
                return estadisticaProveedoresList.size();
            }

            @Override
            public List<EstadisticaProveedor> getRows(int startIndex, int endIndex) {
                return estadisticaProveedoresList.subList(startIndex, endIndex);
            }
        };
    }
}