/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mlots
 * @param <T>
 */
public class DataTableModel<T> extends AbstractTableModel {
    private List<T> modelData;
    private ArrayList<Field> colFields;
    private ArrayList<Method> colMethods;
    private ArrayList<Method> colSetMethods;
    private Map<String, Integer> searchData;
    private Map<String, Integer> asearchData;

    public DataTableModel() {
    }

    public DataTableModel(List<T> modelData, ArrayList<Field> colFields, ArrayList<Method> colMethods, ArrayList<Method> colSetMethods) {
        this.modelData = new ArrayList<>();
        this.colFields = new ArrayList<>();
        this.colMethods = new ArrayList<>();
        this.colSetMethods = new ArrayList<>();
        this.searchData = new HashMap<>();
        this.asearchData = new HashMap<>();
        
        this.modelData = modelData;
        this.colFields = colFields;
        this.colMethods = colMethods;
        this.colSetMethods = colSetMethods;
    }
    
    public DataTableModel(List<T> modelData, ArrayList<Field> colFields, ArrayList<Method> colMethods, ArrayList<Method> colSetMethods, Map<String,Integer> searchData) {
        this.modelData = new ArrayList<>();
        this.colFields = new ArrayList<>();
        this.colMethods = new ArrayList<>();
        this.colSetMethods = new ArrayList<>();
        this.searchData = new HashMap<>();
        this.asearchData = new HashMap<>();
        
        this.modelData = modelData;
        this.colFields = colFields;
        this.colMethods = colMethods;
        this.colSetMethods = colSetMethods;
        this.searchData = searchData;
        fireTableStructureChanged();
    }

    public DataTableModel(List<T> modelData, ArrayList<Field> colFields, ArrayList<Method> colMethods, ArrayList<Method> colSetMethods, Map<String, Integer> searchData, Map<String, Integer> asearchData) {
        this.modelData = new ArrayList<>();
        this.colFields = new ArrayList<>();
        this.colMethods = new ArrayList<>();
        this.colSetMethods = new ArrayList<>();
        this.searchData = new HashMap<>();
        this.asearchData = new HashMap<>();
        
        this.modelData = modelData;
        this.colFields = colFields;
        this.colMethods = colMethods;
        this.colSetMethods = colSetMethods;
        this.searchData = searchData;
        this.asearchData = asearchData;
        fireTableStructureChanged();
    }
    
    public List<T> getModelData() {
        return modelData;
    }

    public void setModelData(List<T> modelData) {
        this.modelData = modelData;
        fireTableStructureChanged();
    }

    public ArrayList<Field> getColFields() {
        return colFields;
    }

    public void setColFields(ArrayList<Field> colFields) {
        this.colFields = colFields;
        fireTableStructureChanged();
    }

    public ArrayList<Method> getColMethods() {
        return colMethods;
    }

    public void setColMethods(ArrayList<Method> colMethods) {
        this.colMethods = colMethods;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return modelData.size();
    }

    @Override
    public int getColumnCount() {
        return colFields.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            return (Object) colMethods.get(columnIndex).invoke(modelData.get(rowIndex));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(DataTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return colFields.get(columnIndex).getType();
    }

    @Override
    public String getColumnName(int column) {
        return colFields.get(column).getName();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex != 2);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            switch (columnIndex) {
                case 3, 4, 5, 6, 7, 8, 9 -> {
                    if(asearchData.isEmpty() || !asearchData.containsKey(aValue.toString().trim())){
                        colSetMethods.get(columnIndex).invoke(modelData.get(rowIndex),aValue);
                    }else{
                        if(this.getColumnClass(columnIndex).getTypeName().equals(aValue.getClass().getName())){
                            colSetMethods.get(columnIndex).invoke(modelData.get(rowIndex),aValue);
                        }else{
                            aValue = asearchData.get(aValue.toString().trim());
                            colSetMethods.get(columnIndex).invoke(modelData.get(rowIndex),aValue);
                        }
                    }
                }
                case 1 -> {
                    if(searchData.isEmpty()){
                        colSetMethods.get(columnIndex).invoke(modelData.get(rowIndex),aValue);
                    }else{
                        aValue = searchData.get(aValue.toString().trim());
                        colSetMethods.get(columnIndex).invoke(modelData.get(rowIndex),aValue);
                    }
                }
                case 0 -> {
                    aValue = aValue.toString().trim();
                    colSetMethods.get(columnIndex).invoke(modelData.get(rowIndex),aValue);
                }
                default -> colSetMethods.get(columnIndex).invoke(modelData.get(rowIndex),aValue);
            }
            fireTableCellUpdated(rowIndex, columnIndex);
            if(this.getColumnClass(columnIndex).equals(Boolean.class)){
                bindCheckBoxes(aValue, rowIndex, columnIndex);
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(DataTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void bindCheckBoxes(Object aValue, int rowIndex, int columnIndex){
        try {
            switch (columnIndex) {
                case 3 -> {
                    if(this.getColumnCount()>5){
                        if(this.getColumnClass(columnIndex).equals(Boolean.class) &&
                                this.getColumnClass(columnIndex + 1).equals(Boolean.class) &&
                                this.getColumnClass(columnIndex + 2).equals(Boolean.class)
                                ){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex + 1).invoke(modelData.get(rowIndex),false);
                                colSetMethods.get(columnIndex + 2).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex + 1);
                                fireTableCellUpdated(rowIndex, columnIndex + 2);   
                            }
                            
                        }else if(this.getColumnClass(columnIndex).equals(Boolean.class) 
                                && this.getColumnClass(columnIndex + 1).equals(Boolean.class)){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex + 1).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex + 1);
                            }
                        }
                    }else{
                        if(this.getColumnClass(columnIndex).equals(Boolean.class) 
                                && this.getColumnClass(columnIndex + 1).equals(Boolean.class)){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex + 1).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex + 1);
                            }
                        }
                    }
                }
                case 4 -> {
                    if(this.getColumnCount()>5){
                        if(this.getColumnClass(columnIndex).equals(Boolean.class) &&
                                this.getColumnClass(columnIndex - 1).equals(Boolean.class) &&
                                this.getColumnClass(columnIndex + 1).equals(Boolean.class)
                                ){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex - 1).invoke(modelData.get(rowIndex),false);
                                colSetMethods.get(columnIndex + 1).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex - 1);
                                fireTableCellUpdated(rowIndex, columnIndex + 1);   
                            }
                            
                        }else if(this.getColumnClass(columnIndex).equals(Boolean.class) 
                                && this.getColumnClass(columnIndex - 1).equals(Boolean.class)){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex - 1).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex - 1);
                            }
                        }
                    }else{
                        if(this.getColumnClass(columnIndex).equals(Boolean.class) 
                                && this.getColumnClass(columnIndex - 1).equals(Boolean.class)){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex - 1).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex - 1);
                            }
                        }
                    } 
                }
                case 5 -> {
                    if(this.getColumnCount()>5){
                        if(this.getColumnClass(columnIndex).equals(Boolean.class) &&
                                this.getColumnClass(columnIndex - 1).equals(Boolean.class) &&
                                this.getColumnClass(columnIndex - 2).equals(Boolean.class)
                                ){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex - 1).invoke(modelData.get(rowIndex),false);
                                colSetMethods.get(columnIndex - 2).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex - 1);
                                fireTableCellUpdated(rowIndex, columnIndex - 2);   
                            }
                            
                        }else if(this.getColumnClass(columnIndex).equals(Boolean.class) 
                                && this.getColumnClass(columnIndex - 1).equals(Boolean.class)){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex - 1).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex - 1);
                            }
                        }
                    }else{
                        if(this.getColumnClass(columnIndex).equals(Boolean.class) 
                                && this.getColumnClass(columnIndex - 1).equals(Boolean.class)){
                            if(aValue.equals(true)){
                                colSetMethods.get(columnIndex - 1).invoke(modelData.get(rowIndex),false);
                                fireTableCellUpdated(rowIndex, columnIndex - 1);
                            }
                        }
                    }
                }
            }
        }catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(DataTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
