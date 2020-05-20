package ru.GUI.Listeners;

import ru.Oxide;
import ru.RawMaterial;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Map;

public class Table_1_Listener implements TableModelListener {

    private JTable table_1;
    private JTable table_3;


    public Table_1_Listener(JTable table_1, JTable table_3) {
        this.table_1 = table_1;
        this.table_3 = table_3;
    }

    @Override
    public void tableChanged(TableModelEvent e) {

            if (e.getFirstRow() == 11) return;
            switch (e.getColumn()) {
                case 0: {
                    RawMaterial rm = (RawMaterial) table_1.getModel().getValueAt(e.getFirstRow(), 0);
                    if (rm.getName().equals("-")) {
                        table_1.getModel().setValueAt(0.0D, e.getFirstRow(), 1);//установить пропорцию = 0;
                    }
                    updateDataRow(e.getFirstRow());
                    updateSummary();
                    updateTable_3();
                    break;
                }
                case 1: {

                    updateDataRow(e.getFirstRow());
                    if ((Double)table_1.getValueAt(e.getFirstRow(), 1) == 0.0D){
                        table_1.setValueAt(0.0D, e.getFirstRow(), 2);
                        table_1.setValueAt(0.0D, e.getFirstRow(), 3);
                        table_1.setValueAt(0.0D, e.getFirstRow(), 7);
                    }
                    updateSummary();
                    updateTable_3();
                    break;
                }
                case 5: {
                    updateVolume();
                    break;
                }
            }
    }

    //Обновление данных в таблице
    public void updateDataRow(int row) {
        RawMaterial rm = (RawMaterial) table_1.getModel().getValueAt(row, 0);
        TableModel model = table_1.getModel();

        Double proportion = (Double) model.getValueAt(row, 1);
        double mass = 1000*proportion/100;
        Double BD = rm.getBD();
        Double volume = 0.0D;
        if (BD != 0) {
            volume = mass / rm.getBD();
        }

        model.setValueAt(rm.getPriceBK5(), row, 2); //Цена BK5, RMB
        model.setValueAt(rm.getPriceBK6(), row, 3); //Цена BK6, RMB
        model.setValueAt(volume, row, 5); //Объем, м3
        model.setValueAt(mass, row, 6); //Масса, кг
        model.setValueAt(BD, row, 7); //BD
        int j = 8;
        //Химия
        Map<Oxide, Double> map = rm.getChemicalAnalysis();

        for (Oxide oxide : Oxide.values()) {
            model.setValueAt(map.get(oxide)*proportion/100, row, j++);
        }
    }

    //Обновить строку суммы
    public void updateSummary(){
        TableModel model = table_1.getModel();
        Double sumProportion = 0.0D;
        Double sumVolume = 0.0D;
        Double sumMass = 0.0D;
        Double sumMg = 0.0D;
        Double sumAl = 0.0D;
        Double sumSi = 0.0D;
        Double sumCa = 0.0D;
        Double sumFe = 0.0D;
        Double sumTi = 0.0D;
        Double sumC = 0.0D;
        Double sumLOI = 0.0D;

        for (int i = 0; i < table_1.getRowCount() - 1; i++) {

            sumProportion += (Double) model.getValueAt(i, 1);
            Double vol = (Double) model.getValueAt(i, 5);
            sumVolume = sumVolume + vol;
            sumMass += (Double) model.getValueAt(i, 6);
            sumMg += (Double) model.getValueAt(i, 8);
            sumAl += (Double) model.getValueAt(i, 9);
            sumSi += (Double) model.getValueAt(i, 10);
            sumCa += (Double) model.getValueAt(i, 11);
            sumFe += (Double) model.getValueAt(i, 12);
            sumTi += (Double) model.getValueAt(i, 13);
            sumC += (Double) model.getValueAt(i, 14);
            sumLOI += (Double) model.getValueAt(i, 15);
        }

        model.setValueAt(sumProportion, table_1.getRowCount()-1, 1);
        model.setValueAt(sumVolume, table_1.getRowCount()-1, 5);
        model.setValueAt(sumMass, table_1.getRowCount()-1, 6);
        model.setValueAt(sumMass/sumVolume, table_1.getRowCount()-1, 7);
        model.setValueAt(sumMg, table_1.getRowCount()-1, 8);
        model.setValueAt(sumAl, table_1.getRowCount()-1, 9);
        model.setValueAt(sumSi, table_1.getRowCount()-1, 10);
        model.setValueAt(sumCa, table_1.getRowCount()-1, 11);
        model.setValueAt(sumFe, table_1.getRowCount()-1, 12);
        model.setValueAt(sumTi, table_1.getRowCount()-1, 13);
        model.setValueAt(sumC, table_1.getRowCount()-1, 14);
        model.setValueAt(sumLOI, table_1.getRowCount()-1, 15);
    }

    //Обновить долю от объема
    public void updateVolume(){
        for (int i = 0; i < table_1.getRowCount() - 1; i++) {
            Double volume = (Double) table_1.getValueAt(i, 5);
            table_1.setValueAt(volume * 100 /(Double) table_1.getValueAt(11, 5), i, 4); //Доля от объема, %
        }
    }

    public void updateTable_3(){
        Double total = 0.0D;
        //Получаем сумму элементов из табл_1
        for (int i = 8; i < table_1.getColumnCount()-1; i++) {
            total += (Double) table_1.getValueAt(11, i);
        }

        //Заполняем строку на сухое вещество
        int j = 8;
        Double sum = 0.0D;
        for (int i = 3; i < table_3.getColumnCount()-1; i++) {
            Double oxide = (Double) table_1.getValueAt(11, j);
            Double value = oxide/total*100;
            table_3.setValueAt(value, 0, i);
            sum += value;
            j++;
        }
        table_3.setValueAt(sum, 0, table_3.getColumnCount()-1);


        //Заполняем строку на прокаленное вещество
        Double LOI = (Double) table_1.getValueAt(table_1.getRowCount()-1, table_1.getColumnCount()-1);
        sum = 0.0D;
        for (int i = 3; i < table_3.getColumnCount()-2; i++) {
            Double oxide = (Double) table_3.getValueAt(0, i);
            Double value = oxide/(100 - LOI)*100;
            table_3.setValueAt(value, 1, i);
            sum += value;
        }

        //Провидим на прокаленное вещество к 100%
        Double sum2 = 0.0D;
        for (int i = 3; i < table_3.getColumnCount()-2; i++) {
            Double oxide = (Double) table_3.getValueAt(1, i);
            Double value = oxide/sum*100;
            table_3.setValueAt(value, 1, i);
            sum2 += value;
        }

        table_3.setValueAt(sum2, 1, table_3.getColumnCount()-1);
        updateBD();
    }

    public void updateBD() {
        table_3.setValueAt((Double) table_1.getValueAt(table_1.getRowCount()-1, 7)*(100 - (Double) table_3.getValueAt(0, 1))/100, 0, 2);
        table_3.setValueAt((Double) table_1.getValueAt(table_1.getRowCount()-1, 7)*(100 - (Double) table_3.getValueAt(1, 1))/100, 1, 2);
    }

}
