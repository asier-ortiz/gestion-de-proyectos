package gui;

import Enums.TrasactionType;
import com.sun.istack.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaginatedTable<T> {

    private final JTable table;
    private final PaginationDataProvider<T> dataProvider;
    private final int[] pageSizes;
    private JPanel contentPanel;
    private int currentPageSize;
    private int currentPage = 1;
    private JPanel pageLinkPanel;
    private ObjectTableModel<T> objectTableModel;
    private static final int MaxPagingCompToShow = 9;
    private static final String Ellipses = "...";
    private List<T> rows;

    private PaginatedTable(JTable table, PaginationDataProvider<T> dataProvider, int[] pageSizes, int defaultPageSize) {
        this.table = table;
        this.dataProvider = dataProvider;
        this.pageSizes = pageSizes;
        this.currentPageSize = defaultPageSize;
    }

    public static <T> PaginatedTable<T> decorate(JTable table, PaginationDataProvider<T> dataProvider, int[] pageSizes, int defaultPageSize) {
        PaginatedTable<T> decorator = new PaginatedTable<>(table, dataProvider, pageSizes, defaultPageSize);
        decorator.init();
        return decorator;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    private void init() {
        initDataModel();
        initPaginationComponents();
        initListeners();
        paginate();
    }

    private void initListeners() {
        objectTableModel.addTableModelListener(this::refreshPageButtonPanel);
    }

    private void initDataModel() {
        TableModel model = table.getModel();
        if (!(model instanceof ObjectTableModel)) {
            throw new IllegalArgumentException("TableModel must be a subclass of ObjectTableModel");
        }
        objectTableModel = (ObjectTableModel<T>) model;
    }

    private void initPaginationComponents() {
        contentPanel = new JPanel(new BorderLayout());
        JPanel paginationPanel = createPaginationPanel();
        contentPanel.add(paginationPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(table));
    }

    private JPanel createPaginationPanel() {
        JPanel paginationPanel = new JPanel();
        pageLinkPanel = new JPanel(new GridLayout(1, MaxPagingCompToShow, 3, 3));
        paginationPanel.add(pageLinkPanel);
        if (pageSizes != null) {
            JComboBox<Integer> pageComboBox = new JComboBox<>(Arrays.stream(pageSizes).boxed().toArray(Integer[]::new));
            pageComboBox.addActionListener((ActionEvent e) -> {
                int currentPageStartRow = ((currentPage - 1) * currentPageSize) + 1;
                currentPageSize = (Integer) pageComboBox.getSelectedItem();
                currentPage = ((currentPageStartRow - 1) / currentPageSize) + 1;
                paginate();
            });
            paginationPanel.add(Box.createHorizontalStrut(15));
            paginationPanel.add(new JLabel("Tam. pag.: "));
            paginationPanel.add(pageComboBox);
            pageComboBox.setSelectedItem(currentPageSize);
        }
        return paginationPanel;
    }

    private void refreshPageButtonPanel(TableModelEvent tme) {
        pageLinkPanel.removeAll();
        int totalRows = dataProvider.getTotalRowCount();
        int pages = (int) Math.ceil((double) totalRows / currentPageSize);
        ButtonGroup buttonGroup = new ButtonGroup();
        if (pages > MaxPagingCompToShow) {
            addPageButton(pageLinkPanel, buttonGroup, 1);
            if (currentPage > (pages - ((MaxPagingCompToShow + 1) / 2))) {
                pageLinkPanel.add(createEllipsesComponent());
                addPageButtonRange(pageLinkPanel, buttonGroup, pages - MaxPagingCompToShow + 3, pages);
            } else if (currentPage <= (MaxPagingCompToShow + 1) / 2) {
                addPageButtonRange(pageLinkPanel, buttonGroup, 2, MaxPagingCompToShow - 2);
                pageLinkPanel.add(createEllipsesComponent());
                addPageButton(pageLinkPanel, buttonGroup, pages);
            } else {
                pageLinkPanel.add(createEllipsesComponent());
                int start = currentPage - (MaxPagingCompToShow - 4) / 2;
                int end = start + MaxPagingCompToShow - 5;
                addPageButtonRange(pageLinkPanel, buttonGroup, start, end);
                pageLinkPanel.add(createEllipsesComponent());
                addPageButton(pageLinkPanel, buttonGroup, pages);
            }
        } else {
            addPageButtonRange(pageLinkPanel, buttonGroup, 1, pages);
        }
        pageLinkPanel.getParent().validate();
        pageLinkPanel.getParent().repaint();
    }

    private Component createEllipsesComponent() {
        return new JLabel(Ellipses, SwingConstants.CENTER);
    }

    private void addPageButtonRange(JPanel parentPanel, ButtonGroup buttonGroup, int start, int end) {
        for (; start <= end; start++) {
            addPageButton(parentPanel, buttonGroup, start);
        }
    }

    private void addPageButton(JPanel parentPanel, ButtonGroup buttonGroup, int pageNumber) {
        JToggleButton toggleButton = new JToggleButton(Integer.toString(pageNumber));
        toggleButton.setMargin(new Insets(1, 3, 1, 3));
        buttonGroup.add(toggleButton);
        parentPanel.add(toggleButton);
        if (pageNumber == currentPage) {
            toggleButton.setSelected(true);
        }
        toggleButton.addActionListener(ae -> {
            currentPage = Integer.parseInt(ae.getActionCommand());
            paginate();
        });
    }

    private void paginate() {
        int startIndex = (currentPage - 1) * currentPageSize;
        int endIndex = startIndex + currentPageSize;
        if (endIndex > dataProvider.getTotalRowCount()) {
            endIndex = dataProvider.getTotalRowCount();
        }
        rows = dataProvider.getRows(startIndex, endIndex);
        objectTableModel.setObjectRows(rows);
        objectTableModel.fireTableDataChanged();
    }

    private void turnPage() {
        ArrayList<JToggleButton> buttons = new ArrayList<>();
        Arrays.stream(pageLinkPanel.getComponents())
                .filter(component -> component.getClass().equals(JToggleButton.class))
                .forEach(button -> buttons.add((JToggleButton) button));
        buttons.get(buttons.size() - 1).doClick();
    }

    public void refresh(TrasactionType trasactionType, @Nullable Integer selectedRow) {
        paginate();
        int totalRowCount = dataProvider.getTotalRowCount();
        if (totalRowCount > 0) {
            switch (trasactionType) {
                case INSERTING -> {
                    if (totalRowCount > currentPageSize || totalRowCount % currentPageSize == 0) turnPage();
                    if (totalRowCount > currentPageSize) {
                        table.setRowSelectionInterval((totalRowCount - 1) - currentPageSize, (totalRowCount - 1) - currentPageSize);
                    } else {
                        table.setRowSelectionInterval(totalRowCount - 1, totalRowCount - 1);
                    }
                }
                case DELETING -> {
                    if (totalRowCount > currentPageSize || totalRowCount % currentPageSize == 0) turnPage();
                    table.clearSelection();
                }
                case UPDATING -> table.setRowSelectionInterval(selectedRow, selectedRow);
            }
        }
    }

    public T getItemAt(int index) {
        return rows.get(index);
    }
}