package ru;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.util.HashMap;
import java.util.Map;

public class RawMaterial {

    private String name; //краткая абривиатура сырьевого материала
    private double priceBK5; //цена за тонну в юанях
    private double priceBK6; //цена BK6
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

    public void setPriceBK5(double price) {
        this.priceBK5 = price;
    }
    public void setPriceBK6(double price) {
        this.priceBK6 = price;
    }

    public RawMaterial() {
        this.name = "-";
        this.priceBK5 =0.0D;
        this.priceBK6 =0.0D;
        this.chemicalAnalysis = new HashMap<>();
        for (Oxide oxide : Oxide.values()) {
            chemicalAnalysis.put(oxide, 0.0D);
        }
        this.bd = 0.0D;
    }

    public String getName() {
        return name;
    }

    public double getPriceBK5() {
        return priceBK5;
    }
    public double getPriceBK6() {
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
