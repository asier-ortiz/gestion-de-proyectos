package gui;

import model.Gestion;

import javax.swing.table.TableModel;
import java.lang.reflect.Field;

public class GestionTableModel {

    private final static Field[] fields = Gestion.class.getDeclaredFields();

    public static TableModel createDataModel() {
        return new ObjectTableModel<Gestion>() {
            @Override
            public Object getValueAt(Gestion gestion, int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> gestion.getProyecto().getCodigo();
                    case 1 -> gestion.getProveedor().getCodigo();
                    case 2 -> gestion.getPieza().getCodigo();
                    case 3 -> gestion.getCantidad();
                    default -> throw new IllegalArgumentException("Invalid column index");
                };
            }

            @Override
            public int getColumnCount() {
                return fields.length;
            }

            @Override
            public String getColumnName(int column) {
                return fields[column].getName().toUpperCase();
            }
        };
    }
}