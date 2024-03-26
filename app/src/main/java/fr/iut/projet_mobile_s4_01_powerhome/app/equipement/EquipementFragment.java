package fr.iut.projet_mobile_s4_01_powerhome.app.equipement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import fr.iut.projet_mobile_s4_01_powerhome.app.user.NotificationFragment;

public class EquipementFragment extends Fragment {
    private Integer id;
    private DatabaseManager databaseManager;
    private List<EquipementPrincipaux> equipementPrincipaux;
    private ListView listView;
    private Integer puissance;
    private TextView puissance_;
    private TextView nbEquipements_;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public EquipementFragment() {
        // Required empty public constructor
    }

    public static EquipementFragment newInstance(String param1, String param2) {
        EquipementFragment fragment = new EquipementFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipement, container, false);

        listView = view.findViewById(R.id.equipementPrincipauxlistView);
        CardView btnAjouterEquipement = view.findViewById(R.id.btnAjouterEquipement);
        CardView btnModif = view.findViewById(R.id.btnModifier);

        equipementPrincipaux = new ArrayList<>();
        databaseManager = new DatabaseManager(requireContext());

        puissance_ = view.findViewById(R.id.puissanceTV);
        nbEquipements_ = view.findViewById(R.id.nbEquipements);

        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        getEquipements();

        btnAjouterEquipement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), EquipementAddActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("puissance", puissance);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        btnModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), EquipementModifActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("puissance", puissance);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return view;
    }

    public void onApiResponseEquipement(JSONObject response) {
        Boolean success = null;
        Integer puissanceCalculee = 0;
        Integer nbEquipements = 0;

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
                    ++nbEquipements;
                    equipementPrincipaux.add(new EquipementPrincipaux(id, name, reference, wattage));
                }
                EquipementPrincipauxAdapter adapter = new EquipementPrincipauxAdapter(requireContext(), equipementPrincipaux, R.layout.item_equipements_principaux2);
                listView.setAdapter(adapter);

                puissance_.setText(String.valueOf(puissanceCalculee) + "W");
                nbEquipements_.setText("Vous avez " + nbEquipements + (nbEquipements > 1 ? " équipements !" : " équipement !"));

                puissance = puissanceCalculee;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getEquipements() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getEquipements.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponseEquipement(response);
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
