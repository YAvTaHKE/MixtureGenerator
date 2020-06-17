package ru.GUI;

import ru.GUI.Utilities.HtmlStyler;
import ru.Oxide;
import ru.RawMaterial;

import javax.swing.table.AbstractTableModel;
import java.util.*;


public class TableModel_2 extends AbstractTableModel {

        private String[] columnNames = {
                "Сырье",
                "Цена BK5, RMB",
                "Цена BK6, RMB",
                "Плотность, кг/м\u00B3",
                "MgO", "Al\u2082O\u2083",
                "SiO\u2082",
                "CaO",
                "Fe\u2082O\u2083",
                "TiO\u2082",
                "C",
                "LOI"};

        private ArrayList<Object[]> data;

        public TableModel_2(LinkedHashMap<RawMaterial, Double> rmMap) {

            columnNames = htmlFormatter(columnNames);
            this.data = new ArrayList<>();
            initDefaultData(rmMap);
        }

    private void initDefaultData(LinkedHashMap<RawMaterial, Double> rmMap){

        rmMap.forEach((rm, proportion) -> {
            Object[] rowObj = new Object[this.getColumnCount()];
            int j = 0;
            rowObj[j++] = rm;
            rowObj[j++] = rm.getPriceBK5();
            rowObj[j++] = rm.getPriceBK6();
            rowObj[j++] = rm.getBD();
            Map<Oxide, Double> map = rm.getChemicalAnalysis();
            Oxide[] var10 = Oxide.values();
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
                Oxide oxide = var10[var12];
                rowObj[j++] = map.get(oxide);
            }
            this.data.add(rowObj);
        });
        //пустая строка для подсчетов
        this.data.add(new Object[this.getColumnCount()]);
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
        if (getValueAt(0, col) == null) return Object.class;
            return getValueAt(0, col).getClass();
    }

        public boolean isCellEditable(int row, int col) {

                return false;
        }
}
