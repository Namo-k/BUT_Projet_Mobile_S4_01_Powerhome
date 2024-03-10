package fr.iut.projet_mobile_s4_01_powerhome;


import java.util.List;

public class Habitant {

    private int id;
    private String habitantName;

    private List<Appliance> appliances;

    private int ecoCoin;

    private int consoTotal;

    public Habitant(int id, String habitantName, List<Appliance> appliances, int ecoCoin) {
        this.id = id;
        this.habitantName = habitantName;
        this.appliances = appliances;
        this.ecoCoin = ecoCoin;
        this.consoTotal = consoTotal(appliances);
    }

    public int consoTotal(List<Appliance> appliances){
        int c = 0;
        for(int i = 0; i < appliances.size(); ++i){
            c = c + appliances.get(i).getWattage();
        }
        return c;
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

    public void setConsoTotal(int consoTotal) {
        this.consoTotal = consoTotal;
    }

}
