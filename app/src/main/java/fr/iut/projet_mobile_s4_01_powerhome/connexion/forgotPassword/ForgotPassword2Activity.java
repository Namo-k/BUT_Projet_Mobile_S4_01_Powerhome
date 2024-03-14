package fr.iut.projet_mobile_s4_01_powerhome.connexion.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.connexion.LoginActivity;
import fr.iut.projet_mobile_s4_01_powerhome.R;

public class ForgotPassword2Activity extends AppCompatActivity {

    private String mail;
    private String password;
    private DatabaseManager databaseManager;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword2);

        databaseManager = new DatabaseManager(getApplicationContext());
        errorTextView = findViewById(R.id.errorTextView);

        Intent intent = getIntent();
        if (intent != null) {
            mail = intent.getStringExtra("mail");
        }

        ImageView btnRetour = (ImageView) findViewById(R.id.btnRetour);
        CardView btnEnregistrer = (CardView) findViewById(R.id.btnEnregistrer);

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword2Activity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Afficher cacher mdp oeil
        ImageView oeilouvert = (ImageView) findViewById(R.id.oeilouvert);
        ImageView oeilferme = (ImageView) findViewById(R.id.oeilferme);
        EditText passEdit = findViewById(R.id.mdpET);

        oeilferme.setVisibility(View.VISIBLE);
        passEdit.setTransformationMethod(new PasswordTransformationMethod());
        oeilouvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oeilouvert.setVisibility(View.INVISIBLE);
                oeilferme.setVisibility(View.VISIBLE);
                passEdit.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
        oeilferme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oeilouvert.setVisibility(View.VISIBLE);
                oeilferme.setVisibility(View.INVISIBLE);
                passEdit.setTransformationMethod(null);
            }
        });

        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText password_ = (EditText) findViewById(R.id.mdpET);
                password = password_.getText().toString();

                if (password.isEmpty()){
                    Toast.makeText(ForgotPassword2Activity.this, "Veuillez remplir un mot de passe", Toast.LENGTH_SHORT).show();
                }
                else if (!isPasswordValid(password)) {
                    Toast.makeText(ForgotPassword2Activity.this, "Le mot de passe doit avoir au moins 8 caractères, une majuscule, un chiffre et un symbole.", Toast.LENGTH_SHORT).show();
                }
                else {
                    changePassword();
                }
            }
        });
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String error = "";
        String password = "";

        try {
            success = response.getBoolean("success");
            if (success == true) {
                Intent intent = new Intent(ForgotPassword2Activity.this, LoginActivity.class);
                Toast.makeText(ForgotPassword2Activity.this, "Votre mot de passe a bien été modifié !", Toast.LENGTH_SHORT).show();
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
    public void changePassword() {
        // ce nombre correspond à localhost dans google chrome
        String url = "http://10.0.2.2:2000/powerhome_server/actions/changePassword.php";
        Map<String, String> params = new HashMap<>();
        params.put("mail", mail);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params); // on envoie sous format JSON pcq php récup format JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorTextView.setText(error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }
    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*\\d.*");
    }

}