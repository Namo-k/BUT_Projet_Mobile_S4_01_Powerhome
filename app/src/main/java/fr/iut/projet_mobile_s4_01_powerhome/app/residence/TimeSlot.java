package fr.iut.projet_mobile_s4_01_powerhome.app.residence;

public class TimeSlot {

    private Integer id;
    private String begin;
    private String end;
    private Integer maxWattage;

    private Integer wattageUsed;


    public TimeSlot(Integer id, String begin, String end, Integer maxWattage, Integer wattageUsed) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.maxWattage = maxWattage;
        this.wattageUsed = wattageUsed;
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

    public void setWattageUsed(Integer wattageUsed) {
        this.wattageUsed = wattageUsed;
    }

}
