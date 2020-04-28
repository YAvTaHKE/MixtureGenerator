package ru.GUI;

import ru.GUI.Utilities.HtmlStyler;
import ru.MixtureGenerator;
import ru.Oxide;
import ru.RawMaterial;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Map;

public class TableModel_1 extends AbstractTableModel {
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
            "MgO",
            "Al\u2082O\u2083",
            "SiO\u2082",
            "CaO",
            "Fe\u2082O\u2083",
            "TiO\u2082",
            "C",
            "LOI"};

    private ArrayList<Object[]> data;

    public TableModel_1(ArrayList<RawMaterial> data) {
        columnNames = htmlFormatter(columnNames);
        this.data = new ArrayList<>();
        initDefaultData(data);
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

    private void initDefaultData(ArrayList<RawMaterial> rmList){

        for (int i = 0; i < rmList.size(); i++){
            Object[] rowObj = new Object[getColumnCount()];
            RawMaterial rm = rmList.get(i);
            int j = 0;
            Double proportion = new Double(0);
            Double mass = 1000*proportion/100;
            Double volume = mass/rm.getBD();

            rowObj[j++] = rm; //Сырьевой материал
            rowObj[j++] = proportion; //Доля от массы, %
            rowObj[j++] = rm.getPriceBK5(); //Цена BK5, RMB
            rowObj[j++] = rm.getPriceBK6(); //Цена BK6, RMB
            rowObj[j++] = new Double(0); //Доля от объема, %
            rowObj[j++] = volume; //Объем, м3
            rowObj[j++] = mass; //Масса, кг
            rowObj[j++] = rm.getBD(); //Плотность, кг/м
            //Физхимия
            Map<Oxide, Double> map = rm.getChemicalAnalysis();
            for (Oxide oxide : Oxide.values()) {
                rowObj[j++] = map.get(oxide)*proportion/100;
            }
            data.add(rowObj);
        }
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

    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
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
