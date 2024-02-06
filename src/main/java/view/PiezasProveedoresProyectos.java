package view;

import Enums.SearchType;
import Enums.TrasactionType;
import controller.GestionDao;
import controller.PiezaDao;
import controller.ProveedorDao;
import controller.ProyectoDao;
import gui.*;
import model.*;
import util.DialogManagerUtil;
import util.HibernateUtil;

import javax.swing.*;
import java.util.*;

import static util.MethodsUtil.setTableLookAndFeel;

public class PiezasProveedoresProyectos {

    private JButton clearButton;
    private JButton insertButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton updateButton;
    private JComboBox<ComboxItem> providerComboBox;
    private JComboBox<ComboxItem> partComboBox;
    private JComboBox<ComboxItem> projectComboBox;
    private JLabel selectedGestionLabel;
    private JList<String> partJlist;
    private JList<String> projectJlist;
    private JList<String> providerJlist;
    private JPanel piezasProveedoresProyectosWindow;
    private JPanel tablePanel;
    private JSpinner partSpinner;
    private final ProyectoDao proyectoDao;
    private final ProveedorDao proveedorDao;
    private final PiezaDao piezaDao;
    private final GestionDao gestionDao;
    private Proveedor selectedProvider;
    private Pieza selectedPart;
    private Proyecto selectedProject;
    private Gestion selectedGestion;
    private final JTable gestionesTable = new JTable(GestionTableModel.createDataModel());
    private PaginatedTable<Gestion> paginatedTable;

    public PiezasProveedoresProyectos() {
        proveedorDao = new ProveedorDao();
        piezaDao = new PiezaDao();
        proyectoDao = new ProyectoDao();
        gestionDao = new GestionDao();
        if (HibernateUtil.getSessionFactory() != null) {
            init();
        } else {
            DialogManagerUtil.showErrorDialog(" Error : No ha sido posible establecer una conexión con la base de datos");
        }
    }

    public JPanel getPiezasProveedoresProyectosWindow() {
        return piezasProveedoresProyectosWindow;
    }

    private void init() {
        partSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        updateComboBoxes();
        setListeners();
        createTable();
    }

    private void setListeners() {

        providerComboBox.addActionListener(e -> {
            if (projectComboBox.getItemCount() != 0) {
                updateInfo("providers", false);
            }
        });

        partComboBox.addActionListener(e -> {
            if (partComboBox.getItemCount() != 0) {
                updateInfo("parts", false);
            }
        });

        projectComboBox.addActionListener(e -> {
            if (projectComboBox.getItemCount() != 0) {
                updateInfo("projects", false);
            }
        });

        clearButton.addActionListener(e -> {
            providerComboBox.setSelectedIndex(-1);
            partComboBox.setSelectedIndex(-1);
            projectComboBox.setSelectedIndex(-1);
            partSpinner.setValue(1);
            gestionesTable.clearSelection();
            selectedGestionLabel.setText("Gestión seleccionada" + " { - }");
        });

        insertButton.addActionListener(e -> {
            if (selectedProvider != null && selectedPart != null && selectedProject != null) {
                Optional<Gestion> saved = gestionDao.save(new Gestion(
                        selectedProject,
                        selectedProvider,
                        selectedPart, (Integer) partSpinner.getValue()));
                if (saved.isPresent()) paginatedTable.refresh(TrasactionType.INSERTING, null);
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedGestion != null) {
                if (gestionDao.delete(selectedGestion)) {
                    clearButton.doClick();
                    paginatedTable.refresh(TrasactionType.DELETING, null);
                }
            }
        });

        updateButton.addActionListener(e -> {
            if (selectedGestion != null) {
                selectedGestion.setCantidad((Integer) partSpinner.getValue());
                Integer selectedRow = gestionesTable.getSelectedRow();
                if (gestionDao.update(selectedGestion)) {
                    paginatedTable.refresh(TrasactionType.UPDATING, selectedRow);
                }
            }
        });

        refreshButton.addActionListener(e -> {
            clearButton.doClick();
            createTable();
        });

        gestionesTable.getSelectionModel().addListSelectionListener(e -> {
            if (gestionesTable.getSelectedRow() > -1) {
                selectedGestion = paginatedTable.getItemAt(gestionesTable.getSelectedRow());
                selectedGestionLabel.setText("Gestión seleccionada" + " { " + selectedGestion.getProyecto().getCodigo() +
                        " " + selectedGestion.getProveedor().getCodigo() + " " + selectedGestion.getPieza().getCodigo() + " }");
                projectComboBox.getModel().setSelectedItem(new ComboxItem(SearchType.PROJECT_CODE, selectedGestion.getProyecto()));
                providerComboBox.getModel().setSelectedItem(new ComboxItem(SearchType.PROVIDERS_CODE, selectedGestion.getProveedor()));
                partComboBox.getModel().setSelectedItem(new ComboxItem(SearchType.PART_CODE, selectedGestion.getPieza()));
                partSpinner.setValue(selectedGestion.getCantidad());
            }
        });
    }

    private void updateInfo(String collectionName, boolean collectionHasBeenModified) {
        switch (collectionName) {
            case "providers" -> {
                if (collectionHasBeenModified) {
                    ArrayList<String> providerCodesList = proveedorDao.getAllCodes();
                    providerComboBox.removeAllItems();
                    if (!providerCodesList.isEmpty()) {
                        Collections.sort(providerCodesList);
                        providerCodesList.forEach(code -> providerComboBox.addItem(new ComboxItem(SearchType.PROVIDERS_CODE, new Proveedor(code))));
                    }
                }
                if (providerComboBox.getSelectedItem() != null) {
                    Optional<Proveedor> optionalProvider = proveedorDao.get(Objects.requireNonNull(providerComboBox.getSelectedItem()).toString());
                    optionalProvider.ifPresentOrElse(proveedor -> {
                                selectedProvider = proveedor;
                                providerJlist.setModel(ObjectInfoListModel.createObjectInfoListModel(selectedProvider, true));
                            },
                            () -> {
                                selectedProvider = null;
                                providerJlist.setModel(new DefaultListModel<>());
                            });
                } else {
                    selectedProvider = null;
                    providerJlist.setModel(new DefaultListModel<>());
                }
            }
            case "parts" -> {
                if (collectionHasBeenModified) {
                    ArrayList<String> partsCodesList = piezaDao.getAllCodes();
                    partComboBox.removeAllItems();
                    if (!partsCodesList.isEmpty()) {
                        Collections.sort(partsCodesList);
                        partsCodesList.forEach(code -> partComboBox.addItem(new ComboxItem(SearchType.PART_CODE, new Pieza(code))));
                    }
                }
                if (partComboBox.getSelectedItem() != null) {
                    Optional<Pieza> optionalPart = piezaDao.get(Objects.requireNonNull(partComboBox.getSelectedItem()).toString());
                    optionalPart.ifPresentOrElse(pieza -> {
                                selectedPart = pieza;
                                partJlist.setModel(ObjectInfoListModel.createObjectInfoListModel(selectedPart, true));
                            },
                            () -> {
                                selectedPart = null;
                                partJlist.setModel(new DefaultListModel<>());
                            });
                } else {
                    selectedPart = null;
                    partJlist.setModel(new DefaultListModel<>());
                }
            }
            case "projects" -> {
                if (collectionHasBeenModified) {
                    ArrayList<String> projectsCodesList = proyectoDao.getAllCodes();
                    projectComboBox.removeAllItems();
                    if (!projectsCodesList.isEmpty()) {
                        Collections.sort(projectsCodesList);
                        projectsCodesList.forEach(code -> projectComboBox.addItem(new ComboxItem(SearchType.PROJECT_CODE, new Proyecto(code))));
                    }
                }
                if (projectComboBox.getSelectedItem() != null) {
                    Optional<Proyecto> optionalProject = proyectoDao.get(Objects.requireNonNull(projectComboBox.getSelectedItem()).toString());
                    optionalProject.ifPresentOrElse(proyecto -> {
                                selectedProject = proyecto;
                                projectJlist.setModel(ObjectInfoListModel.createObjectInfoListModel(selectedProject, true));
                            },
                            () -> {
                                selectedProject = null;
                                projectJlist.setModel(new DefaultListModel<>());
                            });
                } else {
                    selectedProject = null;
                    projectJlist.setModel(new DefaultListModel<>());
                }
            }
        }
    }

    private void createTable() {
        tablePanel.removeAll();
        setTableLookAndFeel(gestionesTable, false);
        gestionesTable.setAutoCreateRowSorter(true);
        paginatedTable = PaginatedTable.decorate(gestionesTable, createGestionTableData(), new int[]{10, 20, 30, 40, 50}, 10);
        tablePanel.add(paginatedTable.getContentPanel());
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private void updateComboBoxes() {
        updateInfo("providers", true);
        updateInfo("parts", true);
        updateInfo("projects", true);
    }

    private PaginationDataProvider<Gestion> createGestionTableData() {

        return new PaginationDataProvider<>() {
            @Override
            public int getTotalRowCount() {
                return (int) gestionDao.getRowCount();
            }

            @Override
            public List<Gestion> getRows(int startIndex, int endIndex) {
                return gestionDao.getInRange(startIndex, endIndex);
            }
        };
    }
}