package fr.iut.projet_mobile_s4_01_powerhome.app.equipement;

import java.util.HashMap;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.R;

public class EquipementPrincipaux {

    private int id;
    private String name;
    private String reference;
    private int wattage;
    private static final Map<String, Integer> equipementImageMap = new HashMap<>();

    static {
        equipementImageMap.put("aspirateur", R.drawable.ic_aspirateur);
        equipementImageMap.put("climatiseur", R.drawable.ic_climatiseur);
        equipementImageMap.put("fer à repasser", R.drawable.ic_fer_a_repasser);
        equipementImageMap.put("machine à laver", R.drawable.ic_machine_a_laver);
        equipementImageMap.put("television", R.drawable.ic_television);
        equipementImageMap.put("radiateur électrique", R.drawable.ic_radiateur);
        equipementImageMap.put("four électrique", R.drawable.ic_four);
    }

    public EquipementPrincipaux(int id, String name, String reference, int wattage) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.wattage = wattage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getReference() { return reference; }
    public int getWattage() {
        return wattage;
    }

    public static int getImageResourceIdByName(String equipementName) {
        Integer resourceId = equipementImageMap.get(equipementName.toLowerCase());
        return resourceId != null ? resourceId : 0;
    }
}
