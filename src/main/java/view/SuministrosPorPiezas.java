package view;

import Enums.SearchType;
import controller.GestionDao;
import controller.PiezaDao;
import controller.ProveedorDao;
import controller.ProyectoDao;
import gui.*;
import model.Pieza;
import model.Proveedor;
import model.Proyecto;
import util.DialogManagerUtil;
import util.HibernateUtil;

import javax.swing.*;
import java.util.*;

import static util.MethodsUtil.setTableLookAndFeel;

public class SuministrosPorPiezas {

    private JComboBox<ComboxItem> partComboBox;
    private JList<String> partJlist;
    private JPanel suministrosPorPiezasWindow;
    private JPanel projectsTablePanel;
    private JPanel providersTablePanel;
    private JTextField numberOfProvidersTextField;
    private JTextField numberOfProjectsTextField;
    private JTextField partTotalTextField;
    private JTextField totalAmountSelectedProjectTextField;
    private JTextField totalAmountSelectedProjectProviderTextField;
    private final ProyectoDao proyectoDao;
    private final ProveedorDao proveedorDao;
    private final PiezaDao piezaDao;
    private final GestionDao gestionDao;
    private Proveedor selectedProvider;
    private Proyecto selectedProject;
    private Pieza selectedPart;
    private final JTable projectsTable = new JTable(ProyectoTableModel.createDataModel());
    private final JTable providersTable = new JTable(ProveedorTableModel.createDataModel());
    private PaginatedTable<Proyecto> paginatedProjectTable;
    private PaginatedTable<Proveedor> paginatedProviderTable;
    private ArrayList<Proyecto> projects = new ArrayList<>();
    private ArrayList<Proveedor> providers = new ArrayList<>();

    public SuministrosPorPiezas() {
        proveedorDao = new ProveedorDao();
        piezaDao = new PiezaDao();
        proyectoDao = new ProyectoDao();
        gestionDao = new GestionDao();
        createProjectTable();
        createProviderTable();
        if (HibernateUtil.getSessionFactory() != null) {
            init();
        } else {
            DialogManagerUtil.showErrorDialog(" Error : No ha sido posible establecer una conexión con la base de datos");
        }
    }

    public JPanel getSuministrosPorPiezasWindow() {
        return suministrosPorPiezasWindow;
    }

    private void init() {
        updateInfo(true);
        setListeners();
    }

    private void setListeners() {

        partComboBox.addActionListener(e -> {
            if (partComboBox.getItemCount() != 0) {
                updateInfo(false);
            }
        });

        projectsTable.getSelectionModel().addListSelectionListener(e -> {
            providersTable.clearSelection();
            totalAmountSelectedProjectProviderTextField.setText("0");
            if (projectsTable.getSelectedRow() > -1) {
                selectedProject = paginatedProjectTable.getItemAt(projectsTable.getSelectedRow());
                totalAmountSelectedProjectTextField.setText(
                        String.valueOf(piezaDao.getTotalAmountOfPartInProject(selectedPart, selectedProject)));
            }
        });

        providersTable.getSelectionModel().addListSelectionListener(e -> {
            if (selectedProject != null && providersTable.getSelectedRow() > -1) {
                selectedProvider = paginatedProviderTable.getItemAt(providersTable.getSelectedRow());
                totalAmountSelectedProjectProviderTextField.setText(
                        String.valueOf(piezaDao.getTotalAmountOfPartsSuppliedByProviderForProject
                                (selectedPart, selectedProvider, selectedProject))
                );
            }
        });
    }

    private void updateInfo(boolean dataHasBeenRefreshed) {
        if (dataHasBeenRefreshed) {
            ArrayList<String> partsCodeList = piezaDao.getAllCodes();
            partComboBox.removeAllItems();
            if (!partsCodeList.isEmpty()) {
                Collections.sort(partsCodeList);
                partsCodeList.forEach(code -> partComboBox.addItem(new ComboxItem(SearchType.PART_CODE, new Pieza(code))));
            }
        }
        if (partComboBox.getSelectedItem() != null) {
            selectedProject = null;
            selectedProvider = null;
            totalAmountSelectedProjectTextField.setText("0");
            totalAmountSelectedProjectProviderTextField.setText("0");
            Optional<Pieza> optionalPart = piezaDao.getInitilized(Objects.requireNonNull(partComboBox.getSelectedItem()).toString());
            optionalPart.ifPresentOrElse(pieza -> {
                        selectedPart = pieza;
                        partJlist.setModel(ObjectInfoListModel.createObjectInfoListModel(selectedPart, true));
                        projects = new ArrayList<>();
                        projects.addAll(piezaDao.getProjectsHavingPart(selectedPart));
                        Collections.sort(projects);
                        numberOfProjectsTextField.setText(String.valueOf(projects.size()));
                        createProjectTable();
                        providers = new ArrayList<>();
                        providers.addAll(piezaDao.getProvidersSuppliyingPart(selectedPart));
                        Collections.sort(providers);
                        numberOfProvidersTextField.setText(String.valueOf(providers.size()));
                        createProviderTable();
                        partTotalTextField.setText(String.valueOf(piezaDao.getTotalAmountSuppliedOfPart(selectedPart)));
                    },
                    () -> {
                        selectedPart = null;
                        partJlist.setModel(new DefaultListModel<>());
                        numberOfProjectsTextField.setText("0");
                        numberOfProvidersTextField.setText("0");
                        partTotalTextField.setText("0");
                    });
        } else {
            selectedPart = null;
            partJlist.setModel(new DefaultListModel<>());
            numberOfProjectsTextField.setText("0");
            numberOfProvidersTextField.setText("0");
            partTotalTextField.setText("0");
        }
    }

    private void createProjectTable() {
        projectsTablePanel.removeAll();
        setTableLookAndFeel(projectsTable, false);
        paginatedProjectTable = PaginatedTable.decorate(projectsTable, createProjectTableData(), new int[]{10, 20, 30, 40, 50}, 10);
        projectsTablePanel.add(paginatedProjectTable.getContentPanel());
        projectsTablePanel.revalidate();
        projectsTablePanel.repaint();
    }

    private void createProviderTable() {
        providersTablePanel.removeAll();
        setTableLookAndFeel(providersTable, false);
        paginatedProviderTable = PaginatedTable.decorate(providersTable, createProviderTableData(), new int[]{10, 20, 30, 40, 50}, 10);
        providersTablePanel.add(paginatedProviderTable.getContentPanel());
        providersTablePanel.revalidate();
        providersTablePanel.repaint();
    }

    private PaginationDataProvider<Proyecto> createProjectTableData() {

        return new PaginationDataProvider<>() {
            @Override
            public int getTotalRowCount() {
                return projects.size();
            }

            @Override
            public List<Proyecto> getRows(int startIndex, int endIndex) {
                return projects.subList(startIndex, endIndex);
            }
        };
    }

    private PaginationDataProvider<Proveedor> createProviderTableData() {

        return new PaginationDataProvider<>() {
            @Override
            public int getTotalRowCount() {
                return providers.size();
            }

            @Override
            public List<Proveedor> getRows(int startIndex, int endIndex) {
                return providers.subList(startIndex, endIndex);
            }
        };
    }
}