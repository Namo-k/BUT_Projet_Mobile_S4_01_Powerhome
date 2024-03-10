package fr.iut.projet_mobile_s4_01_powerhome;

public class EquipementPrincipaux {

    int id;
    String name;
    int wattage;

    int imageId;

    public EquipementPrincipaux(int id, String name, int wattage, int imageId) {
        this.id = id;
        this.name = name;
        this.wattage = wattage;
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWattage() {
        return wattage;
    }

    public int getImageId() {
        return imageId;
    }
}
