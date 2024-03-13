package fr.iut.projet_mobile_s4_01_powerhome.app.residence;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementPrincipaux;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementPrincipauxAdapter;
import fr.iut.projet_mobile_s4_01_powerhome.app.user.PreferenceFragment;

public class CreneauFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Integer id;

    private DatabaseManager databaseManager;

    private Map<String, Integer> equipementConsoMap = new HashMap<>();
    private List<TimeSlot> timeSlots = new ArrayList<>();

    public CreneauFragment() {
        // Required empty public constructor
    }

    public static PreferenceFragment newInstance(String param1, String param2) {
        PreferenceFragment fragment = new PreferenceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creneau, container, false);

        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }


        List<String> maListe = new ArrayList<>();
        maListe.add("Sélectionnez l'heure de début");

        for (int i = 0; i < 24; i++) {
            String heure = String.format("%02dh", i);
            maListe.add(heure);
        }

        Spinner debutSpinner = view.findViewById(R.id.debutET);
        debutSpinner.setAdapter(new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, maListe));
        Spinner finSpinner = view.findViewById(R.id.finET);
        finSpinner.setAdapter(new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, maListe));

        databaseManager = new DatabaseManager(requireContext());

        Spinner equipementSpinner = view.findViewById(R.id.equipement1Spinner);
        TextView consoEquipement = view.findViewById(R.id.equipement1ConsoTV);
        TextView consoCreneau = view.findViewById(R.id.consoCreneauTV);
        CardView btnVerifierCreneau = view.findViewById(R.id.btnVerifier);
        CardView btnEnregistrerCreneau = view.findViewById(R.id.btnEnregistrerCreneau);
        CardView btnAnnulerCreneau = view.findViewById(R.id.btnAnnulerCreneau);

        getEquipements(view);
        //remplirTimeSlot();
        getTimeSlot(view);
        equipementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedEquipement = (String) parent.getItemAtPosition(position);
                    getConsoEquipement(selectedEquipement, consoEquipement);
                } else {
                    consoEquipement.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnVerifierCreneau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consoCreneau.setText("20W");
            }
        });

        btnAnnulerCreneau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MonHabitatFragment habitatFragment = new MonHabitatFragment();
                Bundle args = new Bundle();
                args.putInt("id", id);
                habitatFragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, habitatFragment)
                        .commit();
            }
        });


        return view;
    }

    public void getConsoEquipement(String equipementName, TextView consoEquipement) {
        if (equipementConsoMap.containsKey(equipementName)) {
            int consommation = equipementConsoMap.get(equipementName);
            consoEquipement.setText("Conso de votre " + equipementName + " : " + String.valueOf(consommation) + "W");
        } else {
            Toast.makeText(requireContext(), "La consommation de cet équipement n'est pas disponible", Toast.LENGTH_SHORT).show();
            consoEquipement.setText("");
        }
    }

    public void onApiResponseEquipement(JSONObject response, View rootView) {
        Boolean success;
        try {
            success = response.getBoolean("success");
            if (success) {
                JSONArray appliancesArray = response.getJSONArray("appliances");

                List<String> equipementNames = new ArrayList<>();
                equipementNames.add("Sélectionnez");

                equipementConsoMap.clear();

                for (int i = 0; i < appliancesArray.length(); i++) {
                    JSONObject applianceObject = appliancesArray.getJSONObject(i);
                    String name = applianceObject.getString("name");
                    int consommation = applianceObject.getInt("wattage");

                    equipementNames.add(name);

                    equipementConsoMap.put(name, consommation);
                }

                Spinner equipementSpinner = rootView.findViewById(R.id.equipement1Spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, equipementNames);
                equipementSpinner.setAdapter(adapter);
            } else {
                Toast.makeText(requireContext(), "La requête n'a pas réussi", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erreur lors du traitement de la réponse", Toast.LENGTH_SHORT).show();
        }
    }



    public void getEquipements(View rootView) {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getEquipements.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponseEquipement(response, rootView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }

    public void onApiResponseTimeSlot(JSONObject response, View rootView) {
        Boolean success;
        try {
            success = response.getBoolean("success");
            if (success) {
                JSONArray timeSlotArray = response.getJSONArray("time_slots");

                timeSlots.clear();

                for (int i = 0; i < timeSlotArray.length(); i++) {
                    JSONObject timeSlotObject = timeSlotArray.getJSONObject(i);
                    int id = timeSlotObject.getInt("id");
                    String begin = timeSlotObject.getString("begin");
                    String end = timeSlotObject.getString("end");
                    int maxWattage = timeSlotObject.getInt("max_wattage");
                    int wattageUsed = timeSlotObject.getInt("wattage_used");

                    TimeSlot timeSlot = new TimeSlot(id, begin, end, maxWattage, wattageUsed);
                    timeSlots.add(timeSlot);

                }

                /*Spinner debutSpinner = rootView.findViewById(R.id.debutET);
                Spinner finSpinner = rootView.findViewById(R.id.finET);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item);

                adapter.add("Selectionnez : ");
                for (TimeSlot timeSlot : timeSlots) {
                    adapter.add(timeSlot.getBegin());
                }
                debutSpinner.setAdapter(adapter);

                adapter.clear();
                adapter.add("Selectionnez : ");
                for (TimeSlot timeSlot : timeSlots) {
                    adapter.add(timeSlot.getEnd());
                }
                finSpinner.setAdapter(adapter);*/

            } else {
                Toast.makeText(requireContext(), "La requête n'a pas réussi", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erreur lors du traitement de la réponse", Toast.LENGTH_SHORT).show();
        }
    }
    public void getTimeSlot(View rootView) {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getTimeSlot.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponseTimeSlot(response, rootView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }

    /*public void remplirTimeSlot() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/remplirTimeSlot.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }*/



}

