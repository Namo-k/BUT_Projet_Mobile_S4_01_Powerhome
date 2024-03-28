package fr.iut.projet_mobile_s4_01_powerhome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import fr.iut.projet_mobile_s4_01_powerhome.connexion.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private final static int TEMPS = 4550;
    private String Quote1 = "Savez-vous que l'échelle de consommation peut vous aider à économiser de l'énergie et réduire votre empreinte carbone ?";
    private String Quote2 = "Savez-vous que les bonus qui s'obtiennent en réservant à des heures creuses, vous permettent de gagner de l'argent ?";
    private String Quote3 = "Savez-vous qu'il existe des prises programmables pour éteindre automatiquement vos appareils électroniques ?";
    private String Quote4 = "Savez-vous que les  prises programmables peuvent réduire votre consommation d'énergie sans effort supplémentaire ?";
    private String[] tabQuote = {Quote1, Quote2, Quote3, Quote4};
    private TextView citation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        citation = findViewById(R.id.citationText);
        Random random = new Random();
        int nombreAleatoire = random.nextInt(4);
        citation.setText(tabQuote[nombreAleatoire]);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable,TEMPS);
    }

}