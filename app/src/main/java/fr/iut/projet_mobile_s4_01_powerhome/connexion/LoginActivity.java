package fr.iut.projet_mobile_s4_01_powerhome.connexion;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.WelcomeActivity;
import fr.iut.projet_mobile_s4_01_powerhome.app.residence.MainActivity;
import fr.iut.projet_mobile_s4_01_powerhome.connexion.Register.RegisterActivity1;
import fr.iut.projet_mobile_s4_01_powerhome.connexion.forgotPassword.ForgotPasswordActivity;

public class LoginActivity extends AppCompatActivity {

    private String mail;
    private String password;
    private DatabaseManager databaseManager;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

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

        mailET.setText("ac@gmail.com");
        mdpET.setText("Alexandre77!!");

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        connexionGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

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

    public void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this, LoginGoogleActivity.class);
        startActivity(intent);
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