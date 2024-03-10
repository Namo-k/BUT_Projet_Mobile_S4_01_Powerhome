package fr.iut.projet_mobile_s4_01_powerhome;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Spinner residenceSpinner = findViewById(R.id.residence);

        List<String> maListe = new ArrayList<>();
        maListe.add("Selectionnez votre résidence");
        maListe.add("Residence Churchill");

        residenceSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maListe));


        Spinner habitatSpinner = findViewById(R.id.habitat);

        List<String> maListe2 = new ArrayList<>();
        maListe2.add("Selectionnez votre habitat");
        maListe2.add("Habitat 1");
        maListe2.add("Habitat 2");
        maListe2.add("Habitat 3");
        maListe2.add("Habitat 4");
        maListe2.add("Habitat 5");

        habitatSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maListe2));
    }
}