package ru.GUI.Listeners;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class Table_3_Listener implements TableModelListener {

    JTable table_1;
    JTable table_3;

    public Table_3_Listener(JTable table_1, JTable table_3) {
        this.table_1 = table_1;
        this.table_3 = table_3;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getColumn() == 1) {
            updateBD(); }
    }

    public void updateBD() {
        table_3.setValueAt((Double) table_1.getValueAt(table_1.getRowCount()-1, 7)*(100 - (Double) table_3.getValueAt(0, 1))/100, 0, 2);
        table_3.setValueAt((Double) table_1.getValueAt(table_1.getRowCount()-1, 7)*(100 - (Double) table_3.getValueAt(1, 1))/100, 1, 2);
    }


}
