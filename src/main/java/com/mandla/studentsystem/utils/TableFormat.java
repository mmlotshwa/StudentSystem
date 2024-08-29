/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.utils;

import java.awt.Component;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatter;

/**
 *
 * @author mlots
 */
public class TableFormat {
    private static final int MIN = 10, MAX = 300;
    
    public static void resizeColumnWidth(JTable table) {
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn tbColumn = table.getColumnModel().getColumn(col);
            TableCellRenderer renderer = tbColumn.getHeaderRenderer();
            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            int width = Math.max(MIN, renderer.getTableCellRendererComponent(table,
                    tbColumn.getHeaderValue(), false, false, -1, col).getPreferredSize().width);
            for (int row = 0; row < table.getRowCount() && width < MAX; row++) {
                Component comp = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
                width = Math.max(width, comp.getPreferredSize().width);
            }
            tbColumn.setPreferredWidth(Math.min(width, MAX));
            System.out.println("Column: " + table.getColumnName(col) + "\tSize: " + width);
        }
    }
    
    public static void resizeColumnWidthStatics(JTable table, Map<String,Integer> colNames) {
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn tbColumn = table.getColumnModel().getColumn(col);
            TableCellRenderer renderer = tbColumn.getHeaderRenderer();
            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            int width = Math.max(MIN, renderer.getTableCellRendererComponent(table,
                    tbColumn.getHeaderValue(), false, false, -1, col).getPreferredSize().width);
            for (int row = 0; row < table.getRowCount() && width < MAX; row++) {
                Component comp = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
                width = Math.max(width, comp.getPreferredSize().width);
            }
            if(colNames.containsKey(table.getColumnName(col)))
                tbColumn.setPreferredWidth(colNames.get(table.getColumnName(col)));
            else
                tbColumn.setPreferredWidth(Math.min(width, MAX));
            System.out.println("Column: " + table.getColumnName(col) + "\tSize: " + width);
        }
    }
    
    public static void addComboBoxToCell(JTable table, String colName, JComboBox combo){
        for(int col=0; col < table.getColumnCount(); col++){
            TableColumn tbColumn = table.getColumnModel().getColumn(col);
            if(table.getColumnName(col).equalsIgnoreCase(colName)){
                tbColumn.setCellEditor(new DefaultCellEditor(combo));
            }
        }
    }
}

