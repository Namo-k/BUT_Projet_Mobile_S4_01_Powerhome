package fr.iut.projet_mobile_s4_01_powerhome.connexion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.WelcomeActivity;
import fr.iut.projet_mobile_s4_01_powerhome.app.residence.MainActivity;


public class LoginGoogleActivity extends AppCompatActivity  {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private DatabaseManager databaseManager;
    private TextView email;
    private String personEmail;
    private CardView deconnexion;
    private CardView suite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        databaseManager = new DatabaseManager(getApplicationContext());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        email = findViewById(R.id.email);
        deconnexion = findViewById(R.id.btnDeconnexion);
        suite = findViewById(R.id.btnPasser);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null) {
            personEmail = acct.getEmail();
            email.setText(personEmail);
        }

        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        suite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirection();
            }
        });
    }

    public void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(LoginGoogleActivity.this, LoginActivity.class));
            }
        });
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;
        String error = "";

        try {
            success = response.getBoolean("success");

            if (success == true) {
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
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
    public void redirection() {
        // ce nombre correspond à localhost dans google chrome
        String url = "http://10.0.2.2:2000/powerhome_server/actions/connectUserGoogle.php";
        Map<String, String> params = new HashMap<>();
        params.put("email", personEmail);
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
