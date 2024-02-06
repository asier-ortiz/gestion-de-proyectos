package gui;

import model.Proyecto;

import javax.swing.table.TableModel;

public class ProyectoTableModel {

    private final static String[] columnNames = {"CODIGO", "NOMBRE", "CIUDAD"};

    public static TableModel createDataModel() {
        return new ObjectTableModel<Proyecto>() {
            @Override
            public Object getValueAt(Proyecto proyecto, int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> proyecto.getCodigo();
                    case 1 -> proyecto.getNombre();
                    case 2 -> proyecto.getCiudad() == null ? "" : proyecto.getCiudad();
                    default -> throw new IllegalArgumentException("Invalid column index");
                };
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }
        };
    }
}
