package fr.iut.projet_mobile_s4_01_powerhome;

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

public class LoginActivity extends AppCompatActivity {

    private String mail;
    private String password;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseManager = new DatabaseManager(getApplicationContext());

        CardView loginBtn = findViewById(R.id.btnConnexion);
        TextView registerBtn = findViewById(R.id.btnInscription);
        TextView connexionGoogle = findViewById(R.id.connexionGoogle);
        TextView forgotPasseword = findViewById(R.id.forgotPasseword);
        EditText mailET = findViewById(R.id.mailET);
        EditText mdpET = findViewById(R.id.mdpET);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity1.class);
                startActivity(intent);
            }
        });

        forgotPasseword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = mailET.getText().toString();
                password = mdpET.getText().toString();
                connectUser();
            }
        });
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String error = "";
        String firstTime = "";

        try {
            success = response.getBoolean("success");

            if (success == true) {
                firstTime = response.getString("firstTime");
                Intent intent;
                if (firstTime.equals("YES")) {
                    intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                }
                else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                intent.putExtra("id", response.getInt("id"));
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
    public void connectUser() {
        // ce nombre correspond à localhost dans google chrome
        String url = "http://10.0.2.2:2000/powerhome_server/actions/connectUser.php";
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
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }
}