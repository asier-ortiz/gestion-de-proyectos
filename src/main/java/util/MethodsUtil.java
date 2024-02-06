package util;

import gui.TableCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class MethodsUtil {

    public static void setTableLookAndFeel(JTable table, boolean sortable) {
        TableColumnModel tableColumnModel = table.getColumnModel();
        for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new TableCellRenderer());
        }
        table.setRowHeight(50);
        table.setSelectionBackground(Color.BLACK);
        table.setBackground(new Color(50, 50, 55));
        table.setForeground(new Color(50, 50, 55));
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        table.getTableHeader().setFont(new Font("monospaced", Font.BOLD, 16));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(true);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setEnabled(sortable);
    }
}
