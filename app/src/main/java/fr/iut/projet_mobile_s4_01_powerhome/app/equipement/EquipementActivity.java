package fr.iut.projet_mobile_s4_01_powerhome.app.equipement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;

public class EquipementActivity extends AppCompatActivity {
    private Integer id;
    private DatabaseManager databaseManager;
    private List<EquipementPrincipaux> equipementPrincipaux;
    private ListView listView;
    private Integer puissance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipement);

        listView = findViewById(R.id.equipementPrincipauxlistView);
        TextView btnAjouterEquipement = (TextView) findViewById(R.id.btnAjouterEquipement);

        equipementPrincipaux = new ArrayList<>();
        databaseManager = new DatabaseManager(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        getEquipements();

        btnAjouterEquipement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EquipementAddActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("puissance", puissance);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onApiResponseEquipement(JSONObject response) {
        Boolean success = null;
        Integer puissanceCalculee = 0;
        Integer nbEquipements = 0;

        try {
            success = response.getBoolean("success");
            if (success == true) {
                JSONArray appliancesArray = response.getJSONArray("appliances");

                for (int i = 0; i < appliancesArray.length(); i++) {
                    JSONObject applianceObject = appliancesArray.getJSONObject(i);
                    int id = applianceObject.getInt("id");
                    String name = applianceObject.getString("name");
                    int wattage = applianceObject.getInt("wattage");
                    puissanceCalculee += wattage;
                    ++nbEquipements;
                    equipementPrincipaux.add(new EquipementPrincipaux(id, name, wattage));
                }
                EquipementPrincipauxAdapter adapter = new EquipementPrincipauxAdapter(this, equipementPrincipaux, R.layout.item_equipements_principaux);
                listView.setAdapter(adapter);

                TextView puissance_ = findViewById(R.id.puissanceTV);
                puissance_.setText(String.valueOf(puissanceCalculee) + "W");

                TextView nbEquipements_ = findViewById(R.id.nbEquipements);
                nbEquipements_.setText("Vous avez " + String.valueOf(nbEquipements) + " équipements !");

                puissance = puissanceCalculee;
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