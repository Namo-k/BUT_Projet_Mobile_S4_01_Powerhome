package fr.iut.projet_mobile_s4_01_powerhome.connexion.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import fr.iut.projet_mobile_s4_01_powerhome.connexion.LoginActivity;
import fr.iut.projet_mobile_s4_01_powerhome.R;

public class RegisterActivity3 extends AppCompatActivity {

    private String nom;
    private String prenom;
    private String naissance;
    private String mail;
    private String mdp;
    private String question;
    private String reponse;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        Spinner questionSpinner = findViewById(R.id.question);

        databaseManager = new DatabaseManager(getApplicationContext());

        List<String> maListe = new ArrayList<>();
        maListe.add("Selectionnez une question");
        maListe.add("Quel est le nom de ma peluche ?");
        maListe.add("Quel est le premier livre lu ?");
        maListe.add("Quel serait mon rêve ?");
        questionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maListe));

        ImageView btnRetour = findViewById(R.id.btnRetour);
        TextView btnConnexion = (TextView) findViewById(R.id.btnConnexion);
        CardView btnSuite = (CardView) findViewById(R.id.btnSuite);

        Intent intent = getIntent();
        if (intent != null) {
            nom = intent.getStringExtra("nom");
            prenom = intent.getStringExtra("prenom");
            naissance = intent.getStringExtra("naissance");
            mail = intent.getStringExtra("mail");
            mdp = intent.getStringExtra("mdp");
        }

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity3.this, RegisterActivity2.class);
                startActivity(intent);
            }
        });

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity3.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSuite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reponse_ = findViewById(R.id.reponseET);
                question = questionSpinner.getSelectedItem().toString();
                reponse = reponse_.getText().toString().trim();

                if(question.equals("Selectionnez une question")){
                    Toast.makeText(RegisterActivity3.this, "Veuillez selectionner une question", Toast.LENGTH_SHORT).show();
                }
                else if (reponse.isEmpty()) {
                    Toast.makeText(RegisterActivity3.this, "Veuillez écrire la réponse à la question", Toast.LENGTH_SHORT).show();
                }
                else {
                    createAccount();
                }
            }
        });
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String error = "";

        try {
            success = response.getBoolean("success");

            if (success == true) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                Toast.makeText(getApplicationContext(), "Votre compte a bien été créé", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void createAccount() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/createAccount.php";
        Map<String, String> params = new HashMap<>();
        params.put("mail", mail);
        params.put("password", mdp);
        params.put("nom", nom);
        params.put("prenom", prenom);
        params.put("naissance", naissance);
        params.put("question", question);
        params.put("reponse", reponse);
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