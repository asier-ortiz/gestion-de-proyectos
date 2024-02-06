package gui;

import model.EstadisticaPieza;
import javax.swing.table.TableModel;

public class EstadisticaPiezasTableModel {

    private final static String[] columnNames = {"CODIGO", "NOMBRE", "PRECIO", "Nº PROYECTOS", "Nº PROVEEDORES", "CANTIDAD TOTAL"};

    public static TableModel createDataModel() {

        return new ObjectTableModel<EstadisticaPieza>() {
            @Override
            public Object getValueAt(EstadisticaPieza estadisticaPieza, int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> estadisticaPieza.getCodigo();
                    case 1 -> estadisticaPieza.getNombre();
                    case 2 -> estadisticaPieza.getPrecio();
                    case 3 -> estadisticaPieza.getNumeroProyectos();
                    case 4 -> estadisticaPieza.getNumeroProveedores();
                    case 5 -> estadisticaPieza.getCantidadTotal();
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