package org.census.gui.stuff;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class ResizeColumnWidth {
  public static void main(String args[]) {

    final Object rowData[][] = {
        { "1", "one",  "I" },
        { "2", "two",  "II" },
        { "3", "three", "III" }};
    final String columnNames[] = { "#", "English", "Roman" };

    final JTable table = new JTable(rowData, columnNames);
    JScrollPane scrollPane = new JScrollPane(table);


    JFrame frame = new JFrame("Resizing Table");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.add(scrollPane, BorderLayout.CENTER);


    TableColumn column = null;
    for (int i = 0; i < 3; i++) {
        column = table.getColumnModel().getColumn(i);
        if (i == 2) {
            column.setPreferredWidth(350); //sport column is bigger
        } else {
            column.setPreferredWidth(100);
        }
    }

    frame.setSize(300, 150);
    frame.setVisible(true);

  }
}