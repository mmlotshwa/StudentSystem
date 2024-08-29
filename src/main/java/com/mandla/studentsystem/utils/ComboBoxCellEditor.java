/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.utils;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author mlots
 * @param <T>
 */
public class ComboBoxCellEditor<T> extends AbstractCellEditor implements TableCellEditor {
    private JTable dataSource;
    private List<T> comboSource;
    private List<T> currentData;
    private JComboBox dataCombo;
    private int searchCol;
    private ArrayList<Method> colMethods;
    

    public ComboBoxCellEditor(JTable dataSource, List<T> comboSource, ArrayList<Method> colMethods, int searchCol) {
        this.comboSource = new ArrayList<>();
        this.colMethods = new ArrayList<>();
        
        this.dataSource = dataSource;
        this.comboSource = comboSource;
        this.colMethods = colMethods;
        this.searchCol = searchCol;
    }

    public ComboBoxCellEditor() {
    }
    

    @Override
    public Object getCellEditorValue() {
        return this.dataCombo.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      
        Object searchData = dataSource.getModel().getValueAt(row, searchCol);
        this.currentData = new ArrayList<>();
        for(T item : comboSource){
            try {
                if(colMethods.get(searchCol).invoke(item).equals(searchData)){
                    currentData.add(item);
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(ComboBoxCellEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(!currentData.isEmpty()){
            DefaultComboBoxModel itemsModel = new DefaultComboBoxModel(currentData.toArray());
            this.dataCombo = new JComboBox(itemsModel);
        }else{
            DefaultComboBoxModel itemsModel = new DefaultComboBoxModel(comboSource.toArray());
            this.dataCombo = new JComboBox(itemsModel);
        }
        
        return dataCombo;
    }
    
}
