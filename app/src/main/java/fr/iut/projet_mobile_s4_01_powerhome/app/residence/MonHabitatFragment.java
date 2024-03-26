package fr.iut.projet_mobile_s4_01_powerhome.app.residence;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.creneau.CreneauFragment;
import fr.iut.projet_mobile_s4_01_powerhome.app.user.NotificationFragment;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementAddActivity;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementFragment;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementPrincipaux;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementPrincipauxAdapter;

public class MonHabitatFragment extends Fragment {
    private Integer id;
    private DatabaseManager databaseManager;
    private List<EquipementPrincipaux> equipementPrincipaux;
    private ListView listView;
    private Integer puissance;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MonHabitatFragment() {

    }
    public static MonHabitatFragment newInstance(String param1, String param2) {
        MonHabitatFragment fragment = new MonHabitatFragment();
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mon_habitat, container, false);

        listView = rootView.findViewById(R.id.equipementPrincipauxlistView);
        TextView btnVoirEquipementTV = rootView.findViewById(R.id.btnVoirEquipementTV);
        TextView btnAjouterEquipementTV = rootView.findViewById(R.id.btnAjouterEquipementTV);
        TextView btnModiferConsoTV = rootView.findViewById(R.id.btnModifierConsoTV);

        equipementPrincipaux = new ArrayList<>();
        databaseManager = new DatabaseManager(requireContext());

        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        getInformations(rootView);
        getEquipements(rootView);


        btnVoirEquipementTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EquipementFragment equipementFragment = new EquipementFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, equipementFragment)
                        .commit();
            }
        });

        btnModiferConsoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreneauFragment creneauFragment = new CreneauFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, creneauFragment)
                        .commit();
            }
        });

       btnAjouterEquipementTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), EquipementAddActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("puissance", puissance);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return rootView;
    }

    public void onApiResponse(JSONObject response, View rootView) {
        Boolean success = null;
        String prenom = "";
        Integer numeroHabitat = 0;
        Integer bonus = 0;
        Integer total = 0;
        Integer malus = 0;

        try {
            success = response.getBoolean("success");
            if (success) {

                TextView prenom_ = rootView.findViewById(R.id.prenom);
                TextView numeroHabitat_ = rootView.findViewById(R.id.habitatTV);
                TextView bonus_ = rootView.findViewById(R.id.bonusTV);
                TextView total_ = rootView.findViewById(R.id.totalTV);
                TextView malus_ = rootView.findViewById(R.id.malusTV);

                prenom = response.getString("prenom");
                numeroHabitat = response.getInt("habitat");
                bonus = response.getInt("bonus");
                malus = response.getInt("malus");
                total = bonus - malus;

                prenom_.setText(prenom);
                numeroHabitat_.setText("Appartement n°" + numeroHabitat);
                bonus_.setText(String.valueOf(bonus));
                total_.setText(String.valueOf(total));
                malus_.setText(String.valueOf(malus));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    public void getInformations(View rootView) {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getInformationsHabitat.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponse(response, rootView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseManager.queue.add(jsonObjectRequest);
    }

    public void onApiResponseEquipement(JSONObject response, View rootView) {
        Boolean success = null;
        String prenom = "";
        Integer puissanceCalculee = 0;

        try {
            success = response.getBoolean("success");
            if (success == true) {
                JSONArray appliancesArray = response.getJSONArray("appliances");

                for (int i = 0; i < appliancesArray.length(); i++) {
                    JSONObject applianceObject = appliancesArray.getJSONObject(i);
                    int id = applianceObject.getInt("id");
                    String name = applianceObject.getString("name");
                    String reference = applianceObject.getString("reference");
                    int wattage = applianceObject.getInt("wattage");
                    puissanceCalculee += wattage;

                    int imageResourceId = EquipementPrincipaux.getImageResourceIdByName(name);
                    equipementPrincipaux.add(new EquipementPrincipaux(id, name, reference, wattage));
                }
                EquipementPrincipauxAdapter adapter = new EquipementPrincipauxAdapter(requireContext(), equipementPrincipaux, R.layout.item_equipements_principaux);
                listView.setAdapter(adapter);

                TextView puissance_ = rootView.findViewById(R.id.puissanceTV);
                puissance_.setText(String.valueOf(puissanceCalculee) + "W");
                puissance = puissanceCalculee;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
}
