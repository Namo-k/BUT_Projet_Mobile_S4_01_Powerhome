package fr.iut.projet_mobile_s4_01_powerhome.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class EditProfileActivity extends AppCompatActivity {
    private Integer id;
    private String nom;
    private String prenom;
    private String naissance;
    private String mail;
    private String question;
    private String reponse;
    private EditText nom_;
    private EditText prenom_;
    private EditText naissance_;
    private EditText mail_;
    private EditText reponse_;
    private DatabaseManager databaseManager;
    private Notification notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        databaseManager = new DatabaseManager(getApplicationContext());
        CardView btnEnregistrer = (CardView) findViewById(R.id.btnEnregistrer);
        CardView btnAnnuler = (CardView) findViewById(R.id.btnAnnuler);

        Spinner questionSpinner = findViewById(R.id.question);
        List<String> maListe = new ArrayList<>();
        maListe.add("Selectionnez une question");
        maListe.add("Quel est le nom de ma peluche ?");
        maListe.add("Quel est le premier livre lu ?");
        maListe.add("Quel serait mon rêve ?");
        questionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maListe));

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        nom_ = (EditText) findViewById(R.id.nomET);
        prenom_ = (EditText) findViewById(R.id.prenomET);
        mail_ = (EditText) findViewById(R.id.mailET);
        naissance_ = (EditText) findViewById(R.id.naissanceET);
        reponse_ = (EditText) findViewById(R.id.reponseET);

        getInformations();

        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = nom_.getText().toString();
                prenom = prenom_.getText().toString();
                naissance = naissance_.getText().toString();
                mail = mail_.getText().toString();
                question = questionSpinner.getSelectedItem().toString();
                reponse = reponse_.getText().toString().trim();

                if (nom.isEmpty() || prenom.isEmpty() || naissance.isEmpty() || reponse.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else if (!isValidEmail(mail)){
                    Toast.makeText(getApplicationContext(), "Adresse email non valide", Toast.LENGTH_SHORT).show();
                }
                else if(question.equals("Selectionnez une question")){
                    Toast.makeText(getApplicationContext(), "Veuillez selectionner une question", Toast.LENGTH_SHORT).show();
                }
                else if (reponse.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Veuillez écrire la réponse à la question", Toast.LENGTH_SHORT).show();
                }
                else {
                    notif = new Notification("Configuration", "Vous avez modifié votre profil.", "configuration");
                    updateInformations();
                }
            }
        });

        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        try {
            success = response.getBoolean("success");
            if (success == true) {
                nom = response.getString("nom");
                prenom = response.getString("prenom");
                mail = response.getString("mail");
                naissance = response.getString("naissance");
                question = response.getString("question");
                reponse = response.getString("reponse");

                nom_.setText(nom);
                prenom_.setText(prenom);
                mail_.setText(mail);
                naissance_.setText(naissance);
                reponse_.setText(reponse);

                Spinner questionSpinner = findViewById(R.id.question);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) questionSpinner.getAdapter();
                int position = adapter.getPosition(question);
                questionSpinner.setSelection(position);
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

    public void onApiResponseUpdate(JSONObject response) {
        Boolean success = null;

        try {
            success = response.getBoolean("success");
            if (success == true) {
                Toast.makeText(getApplicationContext(), "Vos informations ont bien été enregistré !", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", response.getInt("id"));
                startActivity(intent);
                finish();

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateInformations() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/updateInformations.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("nom", nom);
        params.put("prenom", prenom);
        params.put("naissance", naissance);
        params.put("mail", mail);
        params.put("question", question);
        params.put("reponse", reponse);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ajouterNotification(notif);
                onApiResponseUpdate(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
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