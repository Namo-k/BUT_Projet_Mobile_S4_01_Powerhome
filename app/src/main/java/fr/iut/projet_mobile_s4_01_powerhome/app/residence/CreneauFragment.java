package fr.iut.projet_mobile_s4_01_powerhome.app.residence;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import fr.iut.projet_mobile_s4_01_powerhome.R;

public class CreneauFragment extends Fragment {


    public CreneauFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_creneau, container, false);
    }
}