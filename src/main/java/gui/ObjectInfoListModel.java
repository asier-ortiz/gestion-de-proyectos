package gui;

import model.Pieza;
import model.Proveedor;
import model.Proyecto;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ObjectInfoListModel {

    public static DefaultListModel<String> createObjectInfoListModel (Object o, boolean horizontal) {
        String name = "<html><h2 style='width: 500px; text-align:center; text-decoration: underline;'>";
        String value = "<html><p style='width: 500px; text-align:center;'>";
        DefaultListModel<String> objectInfoModel = new DefaultListModel<>();
        ArrayList<String> fieldsNames = new ArrayList<>();
        for (Field f : o.getClass().getDeclaredFields()) {
            fieldsNames.add(f.getName().toUpperCase());
        }
        ArrayList<String> fieldValues = new ArrayList<>();
        switch (o.getClass().getSimpleName()) {
            case "Proveedor" -> {
                fieldValues.add(((Proveedor) o).getCodigo());
                fieldValues.add(((Proveedor) o).getNombre());
                fieldValues.add(((Proveedor) o).getApellidos());
                fieldValues.add(((Proveedor) o).getDireccion());
            }
            case "Pieza" -> {
                fieldValues.add(((Pieza) o).getCodigo());
                fieldValues.add(((Pieza) o).getNombre());
                fieldValues.add(String.valueOf(((Pieza) o).getPrecio()));
                fieldValues.add(((Pieza) o).getDescripccion());
            }
            case "Proyecto" -> {
                fieldValues.add(((Proyecto) o).getCodigo());
                fieldValues.add(((Proyecto) o).getNombre());
                fieldValues.add(((Proyecto) o).getCiudad());
            }
        }
        for (int i = 0; i < fieldsNames.size() - 1; i++) {
            if (horizontal) {
                objectInfoModel.addElement(String.format("%s %-15s %s %-15s", "", fieldsNames.get(i), "", fieldValues.get(i)));
            } else {
                objectInfoModel.addElement(name + fieldsNames.get(i));
                objectInfoModel.addElement(value + "\"" + fieldValues.get(i) + "\"");
                objectInfoModel.addElement(" ");
            }
        }
        return objectInfoModel;
    }
}