package fr.iut.projet_mobile_s4_01_powerhome.app.residence;


import java.util.List;

import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.Appliance;

public class Habitant {

    private int id;
    private String habitantName;

    private List<Appliance> appliances;

    private int ecoCoin;

    private int consoTotal;

    public Habitant(int id, String habitantName, List<Appliance> appliances, int ecoCoin, int consoTotal) {
        this.id = id;
        this.habitantName = habitantName;
        this.appliances = appliances;
        this.ecoCoin = ecoCoin;
        this.consoTotal = consoTotal;
    }

    public int getId() {
        return id;
    }
    public String getHabitantName() {
        return habitantName;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public int getEcoCoin() {
        return ecoCoin;
    }
    public void setEcoCoin(int ecoCoin) {
        this.ecoCoin = ecoCoin;
    }

    public int getConsoTotal() {
        return consoTotal;
    }

}
