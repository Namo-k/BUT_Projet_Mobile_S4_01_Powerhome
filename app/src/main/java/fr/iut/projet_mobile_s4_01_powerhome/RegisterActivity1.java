package fr.iut.projet_mobile_s4_01_powerhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RegisterActivity1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        ImageView btnRetour = (ImageView) findViewById(R.id.btnRetour);
        TextView btnConnexion = (TextView) findViewById(R.id.btnConnexion);
        CardView btnSuite = (CardView) findViewById(R.id.btnSuite);

        btnSuite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nom = findViewById(R.id.nomET);
                EditText prenom = findViewById(R.id.prenomET);
                EditText jour = findViewById(R.id.jourET);
                EditText mois = findViewById(R.id.moisET);
                EditText annee = findViewById(R.id.anneeET);
                String nom_ = nom.getText().toString();
                String prenom_ = prenom.getText().toString();
                String jour_ = jour.getText().toString();
                String mois_ = mois.getText().toString();
                String annee_ = annee.getText().toString();

                if (nom_.isEmpty() || prenom_.isEmpty() || jour_.isEmpty() || mois_.isEmpty() || annee_.isEmpty()) {
                    Toast.makeText(RegisterActivity1.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else if (!verifjour(jour_)) {
                    Toast.makeText(RegisterActivity1.this, "Le jour saisi n'est pas conforme", Toast.LENGTH_SHORT).show();
                }
                else if(!verifmois(mois_)){
                    Toast.makeText(RegisterActivity1.this, "Le mois saisi n'est pas conforme", Toast.LENGTH_SHORT).show();
                }
                else if(!verifanne(annee_)){
                    Toast.makeText(RegisterActivity1.this, "L'année saisie n'est pas conforme", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(RegisterActivity1.this, RegisterActivity2.class);
                    //startActivity(intent);
                    String datecomplete = jour_ + "/" + mois_ + "/" + annee_;
                    intent.putExtra("nom", nom_);
                    intent.putExtra("prenom", prenom_);
                    intent.putExtra("naissance", datecomplete);
                    startActivity(intent);
                }
            }
        });

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity1.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity1.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean verifanne(String annee){
        int year;
        if (annee.length() == 4) {
            year = Integer.parseInt(annee);
            if (year >= 1900 && year <= 2006) {
                return true;
            }
        }
        return false;
    }
    public boolean verifmois(String mois){
        int month;
        if (mois.length() == 2) {
            month = Integer.parseInt(mois);
            if (month >= 01 && month <= 12) {
                return true;
            }
        }
        return false;
    }

    public boolean verifjour(String jour){
        int day;
        if (jour.length() == 2) {
            day = Integer.parseInt(jour);
            if (day >= 1 && day <= 31) {
                return true;
            }
        }
        return false;
    }
}