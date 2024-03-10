package fr.iut.projet_mobile_s4_01_powerhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResidenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);


        ListView listView = findViewById(R.id.habitantlistView);

        //Création des appliances
        List<Appliance> a1 = new ArrayList<>();
        a1.add(new Appliance(1, "Aspirateur",  600));
        a1.add(new Appliance(4, "Machine à laver", 50));
        a1.add(new Appliance(2, "Climatiseur", 450));

        List<Appliance> a2 = new ArrayList<>();
        a2.add(new Appliance(3, "Fer a repasser", 145));
        a2.add(new Appliance(1, "Aspirateur", 600));

        List<Appliance> a3 = new ArrayList<>();
        a3.add(new Appliance(4, "Machine à laver", 50));

        //Création des habitans
        List<Habitant> Habitants = new ArrayList<>();

        Habitants.add(new Habitant(1, "Namo Kalia", a1, 15));
        Habitants.add(new Habitant(2, "Alex Carou", a2, 10));
        Habitants.add(new Habitant(3, "Martin Francais", a3, 0));
        Habitants.add(new Habitant(4, "Robert Richard", a2, 0));
        Habitants.add(new Habitant(5, "Jean Francois", a1, 80));
        Habitants.add(new Habitant(5, "Elisabeth Premier", a1, 10));

//        Collections.sort(Habitants, new Comparator<Habitant>() {
//            @Override
//            public int compare(Habitant Habitant1, Habitant Habitant2) {
//                return Integer.compare(Habitant1.getEcoCoin(), Habitant2.getEcoCoin());
//            }
//        });

        HabitantAdapter adapter = new HabitantAdapter(this, Habitants);
        listView.setAdapter(adapter);

    }
}