package fr.iut.projet_mobile_s4_01_powerhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccueilActivity extends AppCompatActivity {
    private Integer id;
    private DatabaseManager databaseManager;
    private List<EquipementPrincipaux> equipementPrincipaux;
    private ListView listView;
    private Map<Integer, Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        Toast.makeText(this, "HEY", Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.equipementPrincipauxlistView);
        ImageView modifProfilBtn = (ImageView) findViewById(R.id.modifProfilBtn);
        TextView btnVoirEquipementTV = (TextView) findViewById(R.id.btnVoirEquipementTV);

        equipementPrincipaux = new ArrayList<>();
        databaseManager = new DatabaseManager(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        getInformations();
        getEquipements();

        btnVoirEquipementTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EquipementActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        modifProfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String prenom = "";
        Integer numeroHabitat = 0;
        Integer bonus = 0;
        Integer total = 0;
        Integer malus = 0;

        try {
            success = response.getBoolean("success");
            if (success == true) {
                TextView prenom_ = findViewById(R.id.prenom);
                TextView numeroHabitat_ = findViewById(R.id.habitatTV);
                TextView bonus_ = findViewById(R.id.bonusTV);
                TextView total_ = findViewById(R.id.totalTV);
                TextView malus_ = findViewById(R.id.malusTV);

                prenom = response.getString("prenom");
                numeroHabitat = response.getInt("habitat");
                bonus = response.getInt("bonus");
                malus = response.getInt("malus");
                total = bonus - malus;

                prenom_.setText(prenom);
                numeroHabitat_.setText("Appartement n°" + numeroHabitat);
                bonus_.setText(String.valueOf(bonus));
                total_.setText(String.valueOf(total));
                malus_.setText(String.valueOf(malus));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void getInformations() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getInformationsHabitat.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
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

    public void onApiResponseEquipement(JSONObject response) {
        Boolean success = null;
        String prenom = "";
        Integer puissance = 0;


        try {
            success = response.getBoolean("success");
            if (success == true) {
                JSONArray appliancesArray = response.getJSONArray("appliances");

                //Map<Integer, Integer> createApplianceImageMap()


                for (int i = 0; i < appliancesArray.length(); i++) {
                    JSONObject applianceObject = appliancesArray.getJSONObject(i);
                    int id = applianceObject.getInt("id");
                    String name = applianceObject.getString("name");
                    int wattage = applianceObject.getInt("wattage");
                    //Toast.makeText(getApplicationContext(), String.valueOf(id) + String.valueOf(wattage) + name, Toast.LENGTH_SHORT).show();
                    puissance += wattage;

                    equipementPrincipaux.add(new EquipementPrincipaux(id, name, wattage));
                }

                EquipementPrincipauxAdapter adapter = new EquipementPrincipauxAdapter(this, equipementPrincipaux, R.layout.item_equipements_principaux);
                listView.setAdapter(adapter);

                TextView puissance_ = findViewById(R.id.puissanceTV);
                puissance_.setText(String.valueOf(puissance) + "W");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getEquipements() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getEquipements.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponseEquipement(response);
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