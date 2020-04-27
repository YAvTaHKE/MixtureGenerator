package ru.GUI;

import ru.GUI.Utilities.HtmlStyler;
import ru.RawMaterial;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;


    public class TableModel_2 extends AbstractTableModel {
        private boolean DEBUG = false;

        private String[] columnNames = {
                "Сырье",
                "Доля от массы, %",
                "<html>Цена BK5, RMB</html>",
                "Цена BK6, RMB",
                "Доля от объема, %",
                "Объем, м3",
                "Масса, кг",
                "Плотность, кг/м\u00B3",
                "MgO", "Al\u2082O\u2083",
                "SiO\u2082",
                "CaO",
                "Fe\u2082O\u2083",
                "TiO\u2082",
                "C",
                "LOI"};
        private ArrayList<RawMaterial> data;

        public TableModel_2(ArrayList<RawMaterial> data) {
            columnNames = htmlFormatter(columnNames);
            this.data = data;
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


        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data.get(row).getName();
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col > 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
    /*public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col
                    + " to " + value
                    + " (an instance of "
                    + value.getClass() + ")");
        }

        data[row][col] = value;
        fireTableCellUpdated(row, col);

        if (DEBUG) {
            System.out.println("New value of data:");
            printDebugData();
        }
    }*/

    /*private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + data[i][j]);
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }*/
}
