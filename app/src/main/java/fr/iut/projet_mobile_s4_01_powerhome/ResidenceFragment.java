package fr.iut.projet_mobile_s4_01_powerhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ResidenceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_residence, container, false);

        ListView listView = view.findViewById(R.id.habitantlistView);

        // Création des appliances
        List<Appliance> a1 = new ArrayList<>();
        a1.add(new Appliance(1, "Aspirateur", 600));
        a1.add(new Appliance(4, "Machine à laver", 50));
        a1.add(new Appliance(2, "Climatiseur", 450));

        List<Appliance> a2 = new ArrayList<>();
        a2.add(new Appliance(3, "Fer à repasser", 145));
        a2.add(new Appliance(1, "Aspirateur", 600));

        List<Appliance> a3 = new ArrayList<>();
        a3.add(new Appliance(4, "Machine à laver", 50));

        // Création des habitants
        List<Habitant> habitants = new ArrayList<>();

        habitants.add(new Habitant(1, "Namo Kalia", a1, 15));
        habitants.add(new Habitant(2, "Alex Carou", a2, 10));
        habitants.add(new Habitant(3, "Martin Francais", a3, 0));
        habitants.add(new Habitant(4, "Robert Richard", a2, 0));
        habitants.add(new Habitant(5, "Jean Francois", a1, 80));
        habitants.add(new Habitant(5, "Elisabeth Premier", a1, 10));

        HabitantAdapter adapter = new HabitantAdapter(requireContext(), habitants);
        listView.setAdapter(adapter);

        return view;
    }
}
