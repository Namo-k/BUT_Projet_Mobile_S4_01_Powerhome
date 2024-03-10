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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccueilActivity extends AppCompatActivity {

    private Integer id;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        ListView listView = findViewById(R.id.equipementPrincipauxlistView);
        ImageView modifProfilBtn = (ImageView) findViewById(R.id.modifProfilBtn);

        databaseManager = new DatabaseManager(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        getInformations();

        modifProfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        List<EquipementPrincipaux> equipementPrincipaux = new ArrayList<>();

        equipementPrincipaux.add(new EquipementPrincipaux(1, "Lave-Linge", 190, 4));
        equipementPrincipaux.add(new EquipementPrincipaux(1, "Lave-Linge", 190, 4));
        equipementPrincipaux.add(new EquipementPrincipaux(1, "Lave-Linge", 190, 4));
        equipementPrincipaux.add(new EquipementPrincipaux(1, "Lave-Linge", 190, 4));

        EquipementPrincipauxAdapter adapter = new EquipementPrincipauxAdapter(this, equipementPrincipaux);
        listView.setAdapter(adapter);
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String prenom = "";
        try {
            success = response.getBoolean("success");
            if (success == true) {
                prenom = response.getString("prenom");
                TextView prenom_ = findViewById(R.id.prenom);
                prenom_.setText(prenom);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void getInformations() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getInformations.php";
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
}