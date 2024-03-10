package fr.iut.projet_mobile_s4_01_powerhome;

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

public class WelcomeActivity extends AppCompatActivity {
    private Integer floor;
    private Integer area;
    private Integer id;
    private DatabaseManager databaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        CardView btnEnregistrer = (CardView) findViewById(R.id.btnEnregistrer);

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

        databaseManager = new DatabaseManager(getApplicationContext());
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floor = (Integer) floorSpinner.getSelectedItem();
                area = (Integer) areaSpinner.getSelectedItem();
                //Toast.makeText(getApplicationContext(), String.valueOf(floor) + String.valueOf(area), Toast.LENGTH_SHORT).show();
                updateHabitat();
            }
        });
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String error = "";

        try {
            success = response.getBoolean("success");

            if (success == true) {
                Intent intent = new Intent(getApplicationContext(), AccueilActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
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
}