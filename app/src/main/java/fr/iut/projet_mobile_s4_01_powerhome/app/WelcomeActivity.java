package fr.iut.projet_mobile_s4_01_powerhome.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.residence.MainActivity;
import fr.iut.projet_mobile_s4_01_powerhome.app.user.Notification;

public class WelcomeActivity extends AppCompatActivity {
    private Integer floor;
    private Integer area;
    private Integer id;
    private String wattage;
    private String name;
    private String reference;
    private Integer puissanceMAX = 10000;
    private Boolean ajout;
    private DatabaseManager databaseManager;

    private Notification notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        CardView btnEnregistrer = (CardView) findViewById(R.id.btnEnregistrer);
        CardView btnPasser = (CardView) findViewById(R.id.btnPasser);

        Spinner floorSpinner = findViewById(R.id.floor);
        List<Integer> maListe = new ArrayList<>();
        maListe.add(1);
        maListe.add(2);
        maListe.add(3);
        maListe.add(4);
        maListe.add(5);
        floorSpinner.setAdapter(new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, maListe));

        Spinner areaSpinner = findViewById(R.id.area);
        areaSpinner.setAdapter(new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, maListe));

        Spinner equipementSpinner = findViewById(R.id.nomET);

        List<String> maListe3 = new ArrayList<>();
        maListe3.add("Sélectionnez un équipement");
        maListe3.add("Aspirateur");
        maListe3.add("Climatiseur");
        maListe3.add("Television");
        maListe3.add("Fer à repasser");
        maListe3.add("Machine à laver");
        maListe3.add("Four électrique");
        maListe3.add("Radiateur électrique");
        equipementSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maListe3));

        databaseManager = new DatabaseManager(getApplicationContext());

        btnPasser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floor = (Integer) floorSpinner.getSelectedItem();
                area = (Integer) areaSpinner.getSelectedItem();
                ajout = false;
                notif = new Notification("Bienvenue", "Nous sommes ravie de vous accueillir dans votre nouveau logement", "notification");
                ajouterNotification(notif);
                updateHabitat();
            }
        });
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floor = (Integer) floorSpinner.getSelectedItem();
                area = (Integer) areaSpinner.getSelectedItem();

                EditText reference_ = findViewById(R.id.referenceET);
                EditText wattage_ = findViewById(R.id.wattageET);
                reference = reference_.getText().toString().trim();
                wattage = wattage_.getText().toString().trim();
                name = equipementSpinner.getSelectedItem().toString();

                if(name.equals("Sélectionnez un équipement")){
                    Toast.makeText(WelcomeActivity.this, "Veuillez selectionner un équipement", Toast.LENGTH_SHORT).show();
                }
                if (reference.isEmpty() || wattage.isEmpty()){
                    Toast.makeText(WelcomeActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else if (puissanceMAX < Integer.parseInt(wattage)) {
                    Toast.makeText(WelcomeActivity.this, "Vous ne pouvez pas dépasser la puissance maximale totale de votre habitat", Toast.LENGTH_SHORT).show();
                }
                else {
                    ajout = true;
                    notif = new Notification("Bienvenue", "Nous sommes ravie de vous accueillir dans votre nouveau logement", "notification");
                    ajouterNotification(notif);
                    updateHabitat();
                }
            }
        });
    }

    public void updateHabitat() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/updateHabitat.php";
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        params.put("floor", floor);
        params.put("area", area);
        JSONObject parameters = new JSONObject(params); // on envoie sous format JSON pcq php récup format JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String error = "";

        try {
            success = response.getBoolean("success");

            if (success == true) {
                if (ajout == true) {
                    notif = new Notification("Ajout d'un équipement", "Vous avez ajouté un " + name + " Ref. " + reference +  " de "+ wattage+"W à votre logement.", "equipement");
                    ajouterNotification(notif);
                    ajouterEquipement();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onApiResponseAjout(JSONObject response) {
        Boolean success = null;
        String error = "";

        try {
            success = response.getBoolean("success");
            if (success == true) {
                Toast.makeText(getApplicationContext(), "Votre équipement a bien été ajouté !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
            else {
                error = response.getString("error");
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ajouterEquipement() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/ajoutEquipement.php";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("reference", reference);
        params.put("wattage", wattage);
        params.put("id", String.valueOf(id));
        //Toast.makeText(getApplicationContext(), name + reference + wattage + String.valueOf(id), Toast.LENGTH_SHORT).show();
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponseAjout(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }
    public void ajouterNotification(Notification notif) {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/ajoutNotification.php";
        Map<String, String> params = new HashMap<>();
        params.put("title", notif.getTitle());
        params.put("notification", notif.getNotification());
        params.put("categorie", notif.getCategorie());
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }
}