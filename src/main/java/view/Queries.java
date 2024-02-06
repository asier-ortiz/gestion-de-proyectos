package view;

import Enums.SearchType;
import controller.Dao;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

import controller.PiezaDao;
import controller.ProveedorDao;
import controller.ProyectoDao;
import gui.ComboxItem;
import gui.ObjectInfoListModel;
import util.DialogManagerUtil;

public class Queries {

    private JButton searchButton;
    private JComboBox<Object> fieldComboBox;
    private JPanel queriesWindow;
    private JTextField searchTextField;
    private JLabel searchFieldLabel;
    private JList<String> infoJlist;
    private Dao<?> dao;
    private final SearchType searchType;
    private ArrayList<?> objectsList = new ArrayList<>();
    private Object selectedItem;

    public Queries(SearchType searchType) {
        this.searchType = searchType;
        init();
    }

    public JPanel getQueriesWindow() {
        return queriesWindow;
    }

    private void init() {
        switch (searchType) {
            case PROVIDERS_CODE -> {
                dao = new ProveedorDao();
                searchFieldLabel.setText("Código");
                searchButton.setText("Buscar Proveedor");
            }
            case PROVIDERS_NAME -> {
                dao = new ProveedorDao();
                searchFieldLabel.setText("Nombre");
                searchButton.setText("Buscar Proveedor");
            }
            case PROVIDERS_SURNAME -> {
                dao = new ProveedorDao();
                searchFieldLabel.setText("Apellidos");
                searchButton.setText("Buscar Proveedor");
            }
            case PROVIDERS_ADDRESS -> {
                dao = new ProveedorDao();
                searchFieldLabel.setText("Dirección");
                searchButton.setText("Buscar Proveedor");
            }
            case PART_CODE -> {
                dao = new PiezaDao();
                searchFieldLabel.setText("Código");
                searchButton.setText("Buscar Pieza");
            }
            case PART_NAME -> {
                dao = new PiezaDao();
                searchFieldLabel.setText("Nombre");
                searchButton.setText("Buscar Pieza");
            }
            case PART_PRICE -> {
                dao = new PiezaDao();
                searchFieldLabel.setText("Precio");
                searchButton.setText("Buscar Pieza");
            }
            case PART_DESCRIPTION -> {
                dao = new PiezaDao();
                searchFieldLabel.setText("Descripción");
                searchButton.setText("Buscar Pieza");
            }

            case PROJECT_CODE -> {
                dao = new ProyectoDao();
                searchFieldLabel.setText("Código");
                searchButton.setText("Buscar Proyecto");
            }
            case PROJECT_NAME -> {
                dao = new ProyectoDao();
                searchFieldLabel.setText("Nombre");
                searchButton.setText("Buscar Proyecto");
            }
            case PROJECT_CITY -> {
                dao = new ProyectoDao();
                searchFieldLabel.setText("Ciudad");
                searchButton.setText("Buscar Proyecto");
            }
        }
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) infoJlist.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        setListeners();
    }

    private void setListeners() {

        searchButton.addActionListener(e -> {
            if (!searchTextField.getText().isEmpty() && !searchTextField.getText().isBlank()) {
                objectsList.clear();
                objectsList = dao.search(searchType, searchTextField.getText().trim());
                fieldComboBox.setModel(new DefaultComboBoxModel<>());
                objectsList.forEach(object -> fieldComboBox.addItem(new ComboxItem(searchType, object)));
                if (!objectsList.isEmpty()) {
                    selectedItem = ((ComboxItem) Objects.requireNonNull(fieldComboBox.getSelectedItem())).getValue();
                    infoJlist.setModel(ObjectInfoListModel.createObjectInfoListModel(selectedItem, false));
                } else {
                    infoJlist.setModel(new DefaultListModel<>());
                    DialogManagerUtil.showInfoDialog("Sin coincidencias");
                }
            }
        });

        fieldComboBox.addActionListener(e -> {
            selectedItem = ((ComboxItem) Objects.requireNonNull(fieldComboBox.getSelectedItem())).getValue();
            infoJlist.setModel(ObjectInfoListModel.createObjectInfoListModel(selectedItem, false));
        });
    }
}