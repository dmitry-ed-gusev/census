package org.census.gui.components.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import static org.census.gui.AppDefaults.NO_DATA_LABEL_TEXT;

/**
 * DataGrid component, inherited from JPanel and implementing rich JTable component.
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 27.12.2014)
 */

public class DataGrid<T extends AbstractDataGridModel> extends JPanel implements ComponentListener {

    @SuppressWarnings("ConstantNamingConvention")
    private static final Log log = LogFactory.getLog(DataGrid.class);

    private JTable      jtable;     // internal JTable component
    private JScrollPane scrollPane; // internal scroll pane component

    /** Creating DataGrid component with DataGridModel. */
    public DataGrid(T dataGridModel) {
        super(new BorderLayout());
        log.debug("DataGrid constructor() working.");
        this.addComponentListener(this);
        // table initialization
        if (dataGridModel != null) { // underlying model isn't empty
            this.jtable = new JTable(dataGridModel);
            //table.setPreferredScrollableViewportSize(new Dimension(600, 100));
            //this.jtable.setFillsViewportHeight(true);

            this.scrollPane = new JScrollPane(this.jtable);
            this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            //this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.add(this.scrollPane);

            this.jtable.addComponentListener(this);

            this.initColumnSizes();

        } else { // underlying model is empty - put label instead of jtable
            this.jtable = null;
            JLabel noDataLabel = new JLabel(NO_DATA_LABEL_TEXT);
            noDataLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(noDataLabel, BorderLayout.CENTER);
        }
    }

    /***/
    public DataGrid() {
        this(null);
    }

    /** Init colums sizes for current  */
    private void initColumnSizes() {
        log.debug("AbstractDataGridModel.initColumnSizes() working [PRIVATE].");

        //this.jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        AbstractDataGridModel tableModel     = (AbstractDataGridModel) this.jtable.getModel();    // table model
        TableCellRenderer     headerRenderer = this.jtable.getTableHeader().getDefaultRenderer(); // table header renderer
        TableColumn           tableColumn; // currently processing table column

        int                   headerWidth; // column header width
        int                   cellWidth;   // column cell width

        for (int i = 0; i < this.jtable.getColumnCount(); i++) { // iterate over columns and set width for every column
            // get current table column
            tableColumn = this.jtable.getColumnModel().getColumn(i);
            // get header width for current column
            //headerWidth = headerRenderer.getTableCellRendererComponent(null, tableColumn.getHeaderValue(),
            //        false, false, 0, 0).getPreferredSize().width;
            // get cell width for current column
            //cellWidth   = this.jtable.getDefaultRenderer(tableModel.getColumnClass(i)).getTableCellRendererComponent(
            //        this.jtable, "ID", false, false, 0, i).getPreferredSize().width;

            //log.debug(String.format("Widths for column [%s] -> header width = [%s], cell width = [%s].",
            //        tableColumn.getHeaderValue(), headerWidth, cellWidth));

            // set width for column
            //tableColumn.setPreferredWidth(Math.max(headerWidth, cellWidth));

            if (i == 0) {
                tableColumn.setPreferredWidth(30);
                tableColumn.setMaxWidth(tableColumn.getPreferredWidth());
                tableColumn.setMinWidth(tableColumn.getPreferredWidth());
            }

            System.out.println(i + " -> " + tableColumn.getMaxWidth());
        } // end of FOR

        //System.out.println("---> " + this.scrollPane.getPreferredSize());
        //System.out.println("---> " + this.jtable.getPreferredSize());

        //this.jtable.revalidate();
        //this.jtable.repaint();

    }

    /**
     * Loads data from DB table, related to JTable model.
    */
    public void load() {
        if (this.jtable != null) {
            AbstractDataGridModel tableModel = (AbstractDataGridModel) this.jtable.getModel();
            tableModel.load();
            this.initColumnSizes();
            //tableModel.fireTableDataChanged();
        }
    }

    /**
     * Saves data to DB table, related to JTable model.
    */
    public void save() {
        if (this.jtable != null) {
            ((AbstractDataGridModel) this.jtable.getModel()).save();
        }
    }

    /**
     * Add empty row to JTable model.
    */
    public void addEmptyRow() {
        if (this.jtable != null) {
            ((AbstractDataGridModel) this.jtable.getModel()).addEmptyRow();
        }
    }

    /**
     * Remove selected row from JTable model.
    */
    public void removeSelectedRow() {
        if (this.jtable != null) {
            AbstractDataGridModel tableModel = (AbstractDataGridModel) this.jtable.getModel();
            tableModel.deleteRow(this.jtable.getSelectedRow());
            //tableModel.fireTableDataChanged();
            //this.jtable.revalidate(); // revalidates jtable vertical scroll bar -> jtable contents changed
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        log.debug("DataGrid.componentResized() working.");
        System.out.println("---> " + this.jtable.getWidth());
        this.initColumnSizes();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        log.debug("DataGrid.componentMoved() working.");
    }

    @Override
    public void componentShown(ComponentEvent e) {
        log.debug("DataGrid.componentShown() working.");
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        log.debug("DataGrid.componentHidden() working.");
    }
}