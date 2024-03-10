package fr.iut.projet_mobile_s4_01_powerhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RegisterActivity2 extends AppCompatActivity {

    private String nom;
    private String prenom;
    private String naissance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        ImageView btnRetour = findViewById(R.id.btnRetour);
        TextView btnConnexion = (TextView) findViewById(R.id.btnConnexion);
        CardView btnSuite = (CardView) findViewById(R.id.btnSuite);

        Intent intent = getIntent();
        if (intent != null) {
            nom = intent.getStringExtra("nom");
            prenom = intent.getStringExtra("prenom");
            naissance = intent.getStringExtra("naissance");
        }

        btnSuite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = findViewById(R.id.mailET);
                EditText password = findViewById(R.id.mdpET);
                String password_ = password.getText().toString().trim();
                String email_ = email.getText().toString().trim();

                if (email_.isEmpty() || password_.isEmpty()){
                    Toast.makeText(RegisterActivity2.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else if (!isValidEmail(email_)){
                    Toast.makeText(RegisterActivity2.this, "Adresse email non valide", Toast.LENGTH_SHORT).show();
                }
                else if (!isPasswordValid(password_)) {
                    Toast.makeText(RegisterActivity2.this, "Le mot de passe doit avoir au moins 8 caractères, une majuscule, un chiffre et un symbole.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                    intent.putExtra("nom", nom);
                    intent.putExtra("prenom", prenom);
                    intent.putExtra("naissance", naissance);
                    intent.putExtra("mail", email_);
                    intent.putExtra("mdp", password_);
                    startActivity(intent);
                }
            }
        });

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, RegisterActivity1.class);
                startActivity(intent);
            }
        });

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, LoginActivity.class);
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
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*\\d.*");
    }
}