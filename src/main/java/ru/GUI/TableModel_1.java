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

public class TableModel_1 extends AbstractTableModel {

    private final int DEFAULT_ROW_COUNT = 11;

    private String[] columnNames = {
            "Сырье",
            "Доля от массы, %",
            "Цена BK5, RMB",
            "Цена BK6, RMB",
            "Доля от объема, %",
            "Объем, м3",
            "Масса, кг",
            "BD, кг/м\u00B3",
            "MgO",
            "Al\u2082O\u2083",
            "SiO\u2082",
            "CaO",
            "Fe\u2082O\u2083",
            "TiO\u2082",
            "C",
            "LOI"};

    private ArrayList<Object[]> data;

    public TableModel_1(ArrayList<RawMaterial> rmList) {
        columnNames = htmlFormatter(columnNames);
        this.data = new ArrayList<>();
        initDefaultData(rmList);
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
        for(int i = 0; i < DEFAULT_ROW_COUNT; ++i) {
            Object[] rowObj = new Object[this.getColumnCount()];
            RawMaterial rm = rmList.get(i);
            int j = 0;
            Double proportion = new Double(0.0D);
            Double mass = 1000.0D * proportion / 100.0D;
            Double volume = mass / rm.getBD();
            int var14 = j + 1;
            rowObj[j] = rm;
            rowObj[var14++] = proportion;
            rowObj[var14++] = rm.getPriceBK5();
            rowObj[var14++] = rm.getPriceBK6();
            rowObj[var14++] = new Double(0.0D);
            rowObj[var14++] = volume;
            rowObj[var14++] = mass;
            rowObj[var14++] = rm.getBD();
            Map<Oxide, Double> map = rm.getChemicalAnalysis();
            Oxide[] var10 = Oxide.values();
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
                Oxide oxide = var10[var12];
                rowObj[var14++] = map.get(oxide) * proportion / 100.0D;
            }



            this.data.add(rowObj);
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
        if (col > 1) {
            return false;
        } else {
            return true;
        }
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
