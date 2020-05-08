package ru.GUI;

import ru.GUI.Utilities.HtmlStyler;
import ru.MixtureGenerator;
import ru.Oxide;
import ru.RawMaterial;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Map;

public class TableModel_3 extends AbstractTableModel {

        private final int DEFAULT_ROW_COUNT = 2;

        private String[] columnNames = {
                "Наименование",
                "MgO",
                "Al\u2082O\u2083",
                "SiO\u2082",
                "CaO",
                "Fe\u2082O\u2083",
                "TiO\u2082",
                "C",
                "LOI"};

        private ArrayList<Object[]> data;

        public TableModel_3() {
            columnNames = htmlFormatter(columnNames);
            data = new ArrayList<>();
            initDefaultData();
        }
        private String[] htmlFormatter(String[] strings){
            String[] str = new String[strings.length];

            for (int i = 0; i < strings.length; i++) {
                HtmlStyler style = new HtmlStyler(strings[i]);
                style.center();
                style.bold();
                str[i] = style.getHtmlString();
            }
            return str;
        }

        private void initDefaultData(){
            for (int i = 0; i < DEFAULT_ROW_COUNT; i++) {
                Object[] row = new Object[this.getColumnCount()];
                data.add(row);
            }
            data.get(0)[0] = new String("На сухое вещество");
            data.get(1)[0] = new String("На прокаленное вещество");
        }


        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        @Override
        public int getRowCount() {
            return data.size();
        }
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }
        @Override
        public Object getValueAt(int row, int col) {
            return data.get(row)[col];
        }

        @Override
        public Class getColumnClass(int col) {

            if (getValueAt(0, col) != null) return getValueAt(0, col).getClass();
            else return Object.class;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            data.get(row)[col] = value;
            fireTableCellUpdated(row, col);
        }


}
