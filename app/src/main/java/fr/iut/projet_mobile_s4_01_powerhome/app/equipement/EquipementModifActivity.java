package fr.iut.projet_mobile_s4_01_powerhome.app.equipement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.residence.MainActivity;
import fr.iut.projet_mobile_s4_01_powerhome.app.user.Notification;

public class EquipementModifActivity extends AppCompatActivity {
    private Integer id;
    private DatabaseManager databaseManager;
    private List<EquipementPrincipaux> equipementPrincipaux;
    private String name;
    private String wattage;
    private String idEquipement;
    private String reference;
    private Integer puissance;
    private Integer puissanceMAX = 10000;
    private TextView errorTextView;
    private Notification notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipements_modif);

        CardView btnModif = (CardView) findViewById(R.id.btnModifier);
        CardView btnSupprimer = (CardView) findViewById(R.id.btnSupprimer);

        TextView puissance_ = findViewById(R.id.puissanceTV);
        errorTextView = (TextView) findViewById(R.id.errorTextView);

        equipementPrincipaux = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            puissance = intent.getIntExtra("puissance", 0);
            puissance_.setText(String.valueOf(puissance) + "W");
        }
        databaseManager = new DatabaseManager(getApplicationContext());

        getEquipement();

        Spinner equipementSpinner = findViewById(R.id.nomET);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        equipementSpinner.setAdapter(adapter);

        EditText reference_ = findViewById(R.id.referenceET);
        EditText wattage_ = findViewById(R.id.wattageET);
        TextView idEquipement_ = findViewById(R.id.idEquipement);

        equipementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = (String) parentView.getItemAtPosition(position);

                if (!selectedItem.equals("Sélectionnez un équipement")) {
                    String[] parts = selectedItem.split("\\.");
                    String selectedId = parts[1]; // Récupère l'ID

                    for (EquipementPrincipaux equipement : equipementPrincipaux) {
                        if (String.valueOf(equipement.getId()).equals(selectedId)) {
                            reference_.setText(equipement.getReference());
                            wattage_.setText(String.valueOf(equipement.getWattage()));
                            idEquipement_.setText(String.valueOf(equipement.getId()));
                            break;
                        }
                    }
                } else {
                    reference_.setText("");
                    wattage_.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        btnSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supprimerEquipement();
                Intent intent = new Intent(getApplicationContext(), EquipementFragment.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        btnModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = reference_.getText().toString().trim();
                wattage = wattage_.getText().toString().trim();
                name = equipementSpinner.getSelectedItem().toString();

                if(name.equals("Sélectionnez un équipement")){
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Veuillez selectionner un équipement");
                }
                if (reference.isEmpty() || wattage.isEmpty()){
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Veuillez remplir tous les champs");
                }
                else if (puissanceMAX < (puissance + Integer.parseInt(wattage))) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Vous ne pouvez pas dépasser la puissance maximale totale de votre habitat");
                }
                else {
                    idEquipement = idEquipement_.getText().toString();
                    notif = new Notification("Modification d'un équipement", "Vous avez modifié : " + name + " Ref. " + reference +  " de "+ wattage+"W de votre logement.", "equipement");
                    modifEquipement();
                }
            }
        });

        btnSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = equipementSpinner.getSelectedItem().toString();

                if(name.equals("Sélectionnez un équipement")){
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Veuillez selectionner un équipement");
                }
                else {
                    idEquipement = idEquipement_.getText().toString();
                    notif = new Notification("Supression d'un équipement", "Vous avez supprimé : " + name + " de votre logement.", "equipement");
                    supprimerEquipement();
                }
            }
        });
    }

    public void onApiResponseGet(JSONObject response) {
        Boolean success = null;

        try {
            success = response.getBoolean("success");
            if (success == true) {
                JSONArray appliancesArray = response.getJSONArray("appliances");

                List<String> maListe = new ArrayList<>();
                maListe.add("Sélectionnez un équipement");

                for (int i = 0; i < appliancesArray.length(); i++) {
                    JSONObject applianceObject = appliancesArray.getJSONObject(i);
                    int id = applianceObject.getInt("id");
                    String name = applianceObject.getString("name");
                    String reference = applianceObject.getString("reference");
                    int wattage = applianceObject.getInt("wattage");
                    equipementPrincipaux.add(new EquipementPrincipaux(id, name, reference, wattage));
                    maListe.add(name + "." + String.valueOf(id));
                }
                Spinner equipementSpinner = findViewById(R.id.nomET);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maListe);
                equipementSpinner.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getEquipement() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getEquipements.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        //Toast.makeText(getApplicationContext(), name + reference + wattage + String.valueOf(id), Toast.LENGTH_SHORT).show();
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponseGet(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }
    public void onApiResponseModif(JSONObject response) {
        Boolean success = null;
        String error = "";

        try {
            success = response.getBoolean("success");
            if (success == true) {
                Toast.makeText(getApplicationContext(), "Votre équipement a bien été modifié !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
            else {
                error = response.getString("error");
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(error);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void modifEquipement() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/modifEquipement.php";
        Map<String, String> params = new HashMap<>();
        params.put("idEquipement", idEquipement);
        params.put("id", String.valueOf(id));
        params.put("reference", reference);
        params.put("wattage", wattage);
        //Toast.makeText(getApplicationContext(), name + reference + wattage + String.valueOf(id), Toast.LENGTH_SHORT).show();
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ajouterNotification(notif);
                onApiResponseModif(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }

    public void onApiResponseSupp(JSONObject response) {
        Boolean success = null;
        String error = "";

        try {
            success = response.getBoolean("success");
            if (success == true) {
                Toast.makeText(getApplicationContext(), "Votre équipement a bien été supprimé !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
            else {
                error = response.getString("error");
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(error);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void supprimerEquipement() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/suppEquipement.php";
        Map<String, String> params = new HashMap<>();
        params.put("idEquipement", idEquipement);
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ajouterNotification(notif);
                onApiResponseSupp(response);
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