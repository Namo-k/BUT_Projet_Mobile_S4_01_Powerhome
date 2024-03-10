package fr.iut.projet_mobile_s4_01_powerhome;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    private String mail_;
    private String question_;
    private String reponse_;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        databaseManager = new DatabaseManager(getApplicationContext());

        Spinner questionSpinner = findViewById(R.id.question);
        List<String> maListe = new ArrayList<>();
        maListe.add("Selectionnez une question");
        maListe.add("Quel est le nom de ma peluche ?");
        maListe.add("Quel est le premier livre lu ?");
        maListe.add("Quel serait mon rêve ?");
        questionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maListe));

        ImageView btnRetour = (ImageView) findViewById(R.id.btnRetour);
        CardView btnEnregistrer = (CardView) findViewById(R.id.btnEnregistrer);

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mail = findViewById(R.id.mailET);
                mail_ = mail.getText().toString();
                EditText reponse = findViewById(R.id.reponseET);
                reponse_ = reponse.getText().toString();
                question_ = questionSpinner.getSelectedItem().toString();

                if(question_.equals("Selectionnez une question")){
                    Toast.makeText(ForgotPasswordActivity.this, "Veuillez selectionner une question", Toast.LENGTH_SHORT).show();
                }
                else if (reponse_.isEmpty() || mail_.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else {
                    getPassword();
                }
            }
        });
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String error = "";
        String id = "";

        try {
            success = response.getBoolean("success");
            if (success == true) {
                Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPassword2Activity.class);
                intent.putExtra("mail", response.getString("mail"));
                startActivity(intent);
                finish();
            }
            else {
                error = response.getString("error");
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void getPassword() {
        // ce nombre correspond à localhost dans google chrome
        String url = "http://10.0.2.2:2000/powerhome_server/actions/forgotPassword.php";
        Map<String, String> params = new HashMap<>();
        params.put("mail", mail_);
        params.put("question", question_);
        params.put("reponse", reponse_);
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