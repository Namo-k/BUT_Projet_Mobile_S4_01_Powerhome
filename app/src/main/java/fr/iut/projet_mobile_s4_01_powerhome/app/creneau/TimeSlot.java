package fr.iut.projet_mobile_s4_01_powerhome.app.creneau;

import java.util.HashMap;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.R;

public class TimeSlot {

    private Integer id;
    private String begin;
    private String end;
    private Integer maxWattage;

    private Integer wattageUsed;

    private String niveau;

    private static final Map<String, Integer> creneauImageMap = new HashMap<>();

    static {
        creneauImageMap.put("vert", R.drawable.rond_vert);
        creneauImageMap.put("orange", R.drawable.rond_orange);
        creneauImageMap.put("rouge", R.drawable.rond_rouge);
    }


    public TimeSlot(Integer id, String begin, String end, Integer maxWattage, Integer wattageUsed) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.maxWattage = maxWattage;
        this.wattageUsed = wattageUsed;
        calculateNiveau();
    }

    private void calculateNiveau() {
        double percentage = (double) wattageUsed / maxWattage * 100;

        if (percentage <= 30) {
            niveau = "vert";
        } else if (percentage > 30 && percentage <= 70) {
            niveau = "orange";
        } else {
            niveau = "rouge";
        }
    }

    public Integer getId() {
        return id;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public Integer getMaxWattage() {
        return maxWattage;
    }

    public Integer getWattageUsed() {
        return wattageUsed;
    }

    public String getNiveau() {
        return niveau;
    }

    public static int getImageResourceIdByName(String CreneauName) {
        Integer resourceId = creneauImageMap.get(CreneauName.toLowerCase());
        return resourceId != null ? resourceId : 0;
    }
}
