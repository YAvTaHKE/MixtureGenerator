package ru;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.util.HashMap;
import java.util.Map;

public class RawMaterial {

    private String name; //краткая абривиатура сырьевого материала
    private int priceBK5; //цена за тонну в юанях
    private int priceBK6; //цена BK6
    private double bd; //истинная плотность


    public double getBD() {
        return bd;
    }

    public void setBD(double bd) {
        this.bd = bd;
    }

    private Map<Oxide, Double> chemicalAnalysis; //Химический состав в пересчете на оксиды

    public void setName(String name) {
        this.name = name;
    }

    public void setPriceBK5(int price) {
        this.priceBK5 = price;
    }
    public void setPriceBK6(int price) {
        this.priceBK6 = price;
    }

    public RawMaterial() {
        this.name = "default";
        this.priceBK5 =0;
        this.priceBK6 =0;
        this.chemicalAnalysis = new HashMap<>();
        this.bd = 0;
    }

    public String getName() {
        return name;
    }

    public int getPriceBK5() {
        return priceBK5;
    }
    public int getPriceBK6() {
        return priceBK6;
    }

    public Map<Oxide, Double> getChemicalAnalysis(){
        return this.chemicalAnalysis;
    }

    public void setChemicalAnalysis (HSSFRow row, short minCol) {
        DataFormatter formatter = new DataFormatter();
        for (Oxide oxide : Oxide.values()) {
            Double value;
            try {
                value = Double.valueOf(formatter.formatCellValue(row.getCell(minCol++)).replace(",", "."));
            } catch (NumberFormatException e) {
                value = 0d;
            }
            chemicalAnalysis.put(oxide, value);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
