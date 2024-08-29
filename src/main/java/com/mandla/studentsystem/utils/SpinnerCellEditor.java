/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mandla.studentsystem.utils;

import com.mandla.studentsystem.utils.EventCellDataChange;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

/**
 *
 * @author mlots
 */
public class SpinnerCellEditor extends DefaultCellEditor{
        private JSpinner data;
        private EventCellDataChange event;

        public SpinnerCellEditor(EventCellDataChange event) {
            super(new JCheckBox());
            this.event = event;
            data = new JSpinner(new SpinnerNumberModel(0,0,100,1));
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) data.getEditor();
            DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
            formatter.setCommitsOnValidEdit(true);
            editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
            
            data.addChangeListener(new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e) {
                    event.dataChanged();
                } 
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
            super.getTableCellEditorComponent(table, value, isSelected, row, column);
            int info = Integer.parseInt(value.toString());
            data.setValue(info);
            data.setEnabled(false);
            enable();
            return data;
        }
        
        private void enable(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        data.setEnabled(true);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SpinnerCellEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }){
                
            }.start();
        }
        
        @Override
        public Object getCellEditorValue(){
            return data.getValue();
        }
    }
