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

public class SuministrosPorProveedor {

    private JComboBox<ComboxItem> providerComboBox;
    private JList<String> providerJlist;
    private JPanel suministrosPorProveedorWindow;
    private JPanel projectsTablePanel;
    private JPanel partsTablePanel;
    private JTextField partsTotalTextField;
    private JTextField numberOfProjectsTextField;
    private JTextField selectedPartTotalLabel;
    private JTextField distinctPartTotalTextField;
    private final ProyectoDao proyectoDao;
    private final ProveedorDao proveedorDao;
    private final PiezaDao piezaDao;
    private final GestionDao gestionDao;
    private Proveedor selectedProvider;
    private Proyecto selectedProject;
    private Pieza selectedPart;
    private final JTable proyectosTable = new JTable(ProyectoTableModel.createDataModel());
    private final JTable piezasTable = new JTable(PiezaTableModel.createDataModel());
    private PaginatedTable<Proyecto> paginatedProjectTable;
    private PaginatedTable<Pieza> paginatedPartTable;
    private ArrayList<Proyecto> projects = new ArrayList<>();
    private ArrayList<Pieza> parts = new ArrayList<>();


    public SuministrosPorProveedor() {
        proveedorDao = new ProveedorDao();
        piezaDao = new PiezaDao();
        proyectoDao = new ProyectoDao();
        gestionDao = new GestionDao();
        createProjectTable();
        createPartTable();
        if (HibernateUtil.getSessionFactory() != null) {
            init();
        } else {
            DialogManagerUtil.showErrorDialog(" Error : No ha sido posible establecer una conexión con la base de datos");
        }
    }

    public JPanel getSuministrosPorProveedorWindow() {
        return suministrosPorProveedorWindow;
    }

    private void init() {
        updateInfo(true);
        setListeners();
    }

    private void setListeners() {

        providerComboBox.addActionListener(e -> {
            if (providerComboBox.getItemCount() != 0) {
                parts = new ArrayList<>();
                createPartTable();
                updateInfo(false);
            }
        });

        proyectosTable.getSelectionModel().addListSelectionListener(e -> {
            parts = new ArrayList<>();
            selectedPartTotalLabel.setText("0");
            if (proyectosTable.getSelectedRow() > -1) {
                selectedProject = paginatedProjectTable.getItemAt(proyectosTable.getSelectedRow());
                proveedorDao.getPartsSuppliedByProviderForProject(selectedProvider, selectedProject).forEach(pieza -> {
                    Optional<Pieza> optionalPieza = piezaDao.getInitilized(pieza.getCodigo());
                    optionalPieza.ifPresent(retrieved -> parts.add(retrieved));
                });
                Collections.sort(parts);
                createPartTable();
            }
        });

        piezasTable.getSelectionModel().addListSelectionListener(e -> {
            if (piezasTable.getSelectedRow() > -1) {
                selectedPart = paginatedPartTable.getItemAt(piezasTable.getSelectedRow());
                int amount = piezaDao.getTotalAmountSuppliedOfPartForProject(selectedPart, selectedProject);
                selectedPartTotalLabel.setText(String.valueOf(amount));
            }
        });
    }


    private void updateInfo(boolean dataHasBeenRefreshed) {
        if (dataHasBeenRefreshed) {
            ArrayList<String> providersCodeList = proveedorDao.getAllCodes();
            providerComboBox.removeAllItems();
            if (!providersCodeList.isEmpty()) {
                Collections.sort(providersCodeList);
                providersCodeList.forEach(code -> providerComboBox.addItem(new ComboxItem(SearchType.PROVIDERS_CODE, new Proveedor(code))));
            }
        }
        if (providerComboBox.getSelectedItem() != null) {
            Optional<Proveedor> optionalProvider = proveedorDao.getInitilized(Objects.requireNonNull(providerComboBox.getSelectedItem()).toString());
            optionalProvider.ifPresentOrElse(proveedor -> {
                        selectedProvider = proveedor;
                        providerJlist.setModel(ObjectInfoListModel.createObjectInfoListModel(selectedProvider, true));
                        partsTotalTextField.setText(String.valueOf(proveedorDao.getTotalAmountOfPartsSuppliedBy(selectedProvider)));
                        projects = new ArrayList<>();
                        projects.addAll(proveedorDao.getProjectsHavingProvider(selectedProvider));
                        Collections.sort(projects);
                        numberOfProjectsTextField.setText(String.valueOf(projects.size()));
                        distinctPartTotalTextField.setText(String.valueOf(proveedorDao.getTotalAmountOfDistinctPartsSuppliedBy(selectedProvider)));
                        createProjectTable();
                    },
                    () -> {
                        selectedProvider = null;
                        providerJlist.setModel(new DefaultListModel<>());
                        partsTotalTextField.setText("0");
                        numberOfProjectsTextField.setText("0");
                        distinctPartTotalTextField.setText("0");
                    });
        } else {
            selectedProvider = null;
            providerJlist.setModel(new DefaultListModel<>());
            partsTotalTextField.setText("0");
            numberOfProjectsTextField.setText("0");
            distinctPartTotalTextField.setText("0");
        }
    }

    private void createProjectTable() {
        projectsTablePanel.removeAll();
        setTableLookAndFeel(proyectosTable, false);
        paginatedProjectTable = PaginatedTable.decorate(proyectosTable, createProjectTableData(), new int[]{10, 20, 30, 40, 50}, 10);
        projectsTablePanel.add(paginatedProjectTable.getContentPanel());
        projectsTablePanel.revalidate();
        projectsTablePanel.repaint();
    }

    private void createPartTable() {
        partsTablePanel.removeAll();
        setTableLookAndFeel(piezasTable, false);
        paginatedPartTable = PaginatedTable.decorate(piezasTable, createPartTableData(), new int[]{10, 20, 30, 40, 50}, 10);
        partsTablePanel.add(paginatedPartTable.getContentPanel());
        partsTablePanel.revalidate();
        partsTablePanel.repaint();
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

    private PaginationDataProvider<Pieza> createPartTableData() {

        return new PaginationDataProvider<>() {
            @Override
            public int getTotalRowCount() {
                return parts.size();
            }

            @Override
            public List<Pieza> getRows(int startIndex, int endIndex) {
                return parts.subList(startIndex, endIndex);
            }
        };
    }
}