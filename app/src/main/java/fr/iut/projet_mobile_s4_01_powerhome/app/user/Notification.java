package fr.iut.projet_mobile_s4_01_powerhome.app.user;

import java.util.HashMap;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.R;

public class Notification {

    private int id;
    private String title;
    private String notification;
    private String categorie;
    private static final Map<String, Integer> notificationImageMap = new HashMap<>();

    static {
        notificationImageMap.put("creneau", R.drawable.creneau);
        notificationImageMap.put("equipement", R.drawable.equipement_logo);
        notificationImageMap.put("residence", R.drawable.accueil);
        notificationImageMap.put("configuration", R.drawable.utilisateur);
        notificationImageMap.put("notification", R.drawable.cloche);
    }

    public Notification(int id, String title, String notification, String categorie) {
        this.id = id;
        this.title = title;
        this.notification = notification;
        this.categorie = categorie;
    }
    public Notification(String title, String notification, String categorie) {
        this.id = id;
        this.title = title;
        this.notification = notification;
        this.categorie = categorie;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNotification() {
        return notification;
    }

    public String getCategorie() {
        return categorie;
    }

    public static int getImageResourceIdByName(String equipementName) {
        Integer resourceId = notificationImageMap.get(equipementName.toLowerCase());
        return resourceId != null ? resourceId : 0;
    }
}
