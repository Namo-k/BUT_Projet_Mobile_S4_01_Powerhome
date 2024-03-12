package fr.iut.projet_mobile_s4_01_powerhome;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResidenceActivity extends AppCompatActivity {

    private Integer id;
    private DatabaseManager databaseManager;
    private List<Habitant> Habitants;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);

        databaseManager = new DatabaseManager(getApplicationContext());

        Habitants = new ArrayList<>();
        listView = findViewById(R.id.habitantlistView);
        getListeHabitants();
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;

        try {
            success = response.getBoolean("success");
            if (success == true) {
                JSONArray habitantsArray = response.getJSONArray("users");

                List<Appliance> a1 = new ArrayList<>();
                a1.add(new Appliance(1, "Aspirateur",  600));
                a1.add(new Appliance(4, "Machine à laver", 50));
                a1.add(new Appliance(2, "Climatiseur", 450));

                for (int i = 0; i < habitantsArray.length(); i++) {
                    JSONObject applianceObject = habitantsArray.getJSONObject(i);
                    Integer habitat_id = applianceObject.getInt("habitat_id");
                    String nomprenom = applianceObject.getString("prenom") + " " + applianceObject.getString("nom");;
                    Integer ecocoin = applianceObject.getInt("bonus") - applianceObject.getInt("malus");
                    Integer consoTotal = applianceObject.getInt("consommation");

                    Habitants.add(new Habitant(habitat_id, nomprenom, a1, ecocoin, consoTotal));
                }
                HabitantAdapter adapter = new HabitantAdapter(this, Habitants);
                listView.setAdapter(adapter);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getListeHabitants() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getListeHabitants.php";
        Map<String, String> params = new HashMap<>();
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

}


