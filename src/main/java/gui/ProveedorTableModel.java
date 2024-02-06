package gui;

import model.Proveedor;

import javax.swing.table.TableModel;

public class ProveedorTableModel {

    private final static String[] columnNames = {"CODIGO", "NOMBRE", "APELLIDOS", "DIRECCION"};

    public static TableModel createDataModel() {

        return new ObjectTableModel<Proveedor>() {
            @Override
            public Object getValueAt(Proveedor proveedor, int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> proveedor.getCodigo();
                    case 1 -> proveedor.getNombre();
                    case 2 -> proveedor.getApellidos();
                    case 3 -> proveedor.getDireccion();
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
