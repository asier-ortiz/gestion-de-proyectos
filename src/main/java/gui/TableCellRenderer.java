package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRenderer extends DefaultTableCellRenderer {

    private final Font cellFont = new Font("monospaced", Font.PLAIN, 12);
    private final Border greenEmphasisBorder = new LineBorder(Color.CYAN, 2);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setForeground(Color.WHITE);
        cell.setFont(cellFont);
        setHorizontalAlignment(CENTER);
        if (!isSelected) {
            cell.setBackground(row % 2 == 0 ? Color.DARK_GRAY : Color.GRAY);
        }
        if (hasFocus) {
            setBorder(greenEmphasisBorder);
        }
        return cell;
    }
}
