package fr.iut.projet_mobile_s4_01_powerhome.app.equipement;

import java.util.HashMap;
import java.util.Map;

public class Appliance {
    private int id;
    private String name;
    private int wattage;

    public Appliance(int id, String name, int wattage) {
        this.id = id;
        this.name = name;
        this.wattage = wattage;
    }

    public int getId() {
        return id;
    }

    public int getWattage() {
        return wattage;
    }
}