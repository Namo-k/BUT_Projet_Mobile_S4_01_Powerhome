package fr.iut.projet_mobile_s4_01_powerhome.app.equipement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class EquipementModifActivity extends AppCompatActivity {
    private Integer id;
    private DatabaseManager databaseManager;
    private String wattage;
    private String name;
    private String reference;
    private Integer puissance;
    private Integer puissanceMAX = 10000;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipements_modif);

        CardView btnModif = (CardView) findViewById(R.id.btnModifier);
        CardView btnSupprimer = (CardView) findViewById(R.id.btnSupprimer);

        TextView puissance_ = findViewById(R.id.puissanceTV);
        errorTextView = (TextView) findViewById(R.id.errorTextView);

        Spinner equipementSpinner = findViewById(R.id.nomET);

        List<String> maListe = new ArrayList<>();
        maListe.add("Sélectionnez un équipement");
        maListe.add("Aspirateur");
        maListe.add("Climatiseur");
        maListe.add("Television");
        maListe.add("Fer à repasser");
        maListe.add("Machine à laver");
        maListe.add("Four électrique");
        maListe.add("Radiateur électrique");
        equipementSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maListe));

        databaseManager = new DatabaseManager(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            puissance = intent.getIntExtra("puissance", 0);
            puissance_.setText(String.valueOf(puissance) + "W");
        }

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
                EditText reference_ = findViewById(R.id.referenceET);
                EditText wattage_ = findViewById(R.id.wattageET);
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
                    modifEquipement();
                }
            }
        });
    }
    public void onApiResponseModif(JSONObject response) {
        Boolean success = null;
        String error = "";

        try {
            success = response.getBoolean("success");
            if (success == true) {
                Toast.makeText(getApplicationContext(), "Votre équipement a bien été ajouté !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EquipementActivity.class);
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
                Toast.makeText(getApplicationContext(), "Votre équipement a bien été ajouté !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EquipementActivity.class);
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
        params.put("name", name);
        params.put("reference", reference);
        params.put("wattage", wattage);
        params.put("id", String.valueOf(id));
        //Toast.makeText(getApplicationContext(), name + reference + wattage + String.valueOf(id), Toast.LENGTH_SHORT).show();
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
}