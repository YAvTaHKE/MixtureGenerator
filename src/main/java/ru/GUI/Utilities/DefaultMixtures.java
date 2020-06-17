package ru.GUI.Utilities;

import ru.RawMaterial;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DefaultMixtures {

    public static LinkedHashMap<RawMaterial, Double> MC (HashMap<String, RawMaterial> rmDataMap){
        LinkedHashMap<RawMaterial, Double> map = new LinkedHashMap<>();

        map.put(rmDataMap.getOrDefault("FM968LC", new RawMaterial()), 25.D);  //1
        map.put(rmDataMap.getOrDefault("FM968", new RawMaterial()), 49.5);     //2
        map.put(rmDataMap.getOrDefault("SCRAPMC", new RawMaterial()), 10.0D);   //3
        map.put(new RawMaterial(), 0.0D);                                     //4
        map.put(rmDataMap.getOrDefault("AL98", new RawMaterial()), 1.0D);       //5
        map.put(rmDataMap.getOrDefault("SI97", new RawMaterial()), 0.5D);       //6
        map.put(rmDataMap.getOrDefault("SIC95", new RawMaterial()), 0.0D);      //7
        map.put(rmDataMap.getOrDefault("G195", new RawMaterial()), 10.0D);      //8
        map.put(new RawMaterial(), 0.0D);                                     //9
        map.put(rmDataMap.getOrDefault("PITCH", new RawMaterial()), 1.0D);       //10
        map.put(rmDataMap.getOrDefault("RESIN5311", new RawMaterial()), 3.0D);  //11

        return map;
    }
}
