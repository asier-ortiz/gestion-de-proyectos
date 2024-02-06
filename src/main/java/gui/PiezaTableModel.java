package gui;

import model.Pieza;

import javax.swing.table.TableModel;

public class PiezaTableModel {

    private final static String[] columnNames = {"CODIGO", "NOMBRE", "PRECIO"};

    public static TableModel createDataModel() {

        return new ObjectTableModel<Pieza>() {
            @Override
            public Object getValueAt(Pieza pieza, int columnIndex) {
               return switch (columnIndex) {
                    case 0 -> pieza.getCodigo();
                    case 1 -> pieza.getNombre();
                    case 2 -> pieza.getPrecio();
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