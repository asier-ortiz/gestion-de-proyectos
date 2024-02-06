package gui;

import model.EstadisticaProveedor;

import javax.swing.table.TableModel;

public class EstadisticaProveedorTableModel {

    private final static String[] columnNames = {"CODIGO", "NOMBRE", "APELLIDOS", "Nº PROYECTOS", "Nº PIEZAS", "CANTIDAD TOTAL"};

    public static TableModel createDataModel() {

        return new ObjectTableModel<EstadisticaProveedor>() {
            @Override
            public Object getValueAt(EstadisticaProveedor estadisticaProveedor, int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> estadisticaProveedor.getCodigo();
                    case 1 -> estadisticaProveedor.getNombre();
                    case 2 -> estadisticaProveedor.getApellidos();
                    case 3 -> estadisticaProveedor.getNumeroProyectos();
                    case 4 -> estadisticaProveedor.getNumeroPiezas();
                    case 5 -> estadisticaProveedor.getCantidadTotal();
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
