package fr.iut.projet_mobile_s4_01_powerhome;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipementFragment extends Fragment {
    private Integer id;
    private DatabaseManager databaseManager;
    private List<EquipementPrincipaux> equipementPrincipaux;
    private ListView listView;
    private Integer puissance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Utilisez l'inflater pour créer la vue du fragment
        View view = inflater.inflate(R.layout.fragment_equipement, container, false);

        listView = view.findViewById(R.id.equipementPrincipauxlistView);
        TextView btnAjouterEquipement = view.findViewById(R.id.btnAjouterEquipement);

        equipementPrincipaux = new ArrayList<>();
        databaseManager = new DatabaseManager(requireContext());

        // Obtenez l'id de l'intent parent (si disponible)
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("id", 0);
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

        return view;
    }

    public void onApiResponseEquipement(JSONObject response) {
        Boolean success = null;
        Integer puissanceCalculee = 0;

        try {
            success = response.getBoolean("success");
            if (success != null && success) {
                JSONArray appliancesArray = response.getJSONArray("appliances");

                for (int i = 0; i < appliancesArray.length(); i++) {
                    JSONObject applianceObject = appliancesArray.getJSONObject(i);
                    int id = applianceObject.getInt("id");
                    String name = applianceObject.getString("name");
                    int wattage = applianceObject.getInt("wattage");
                    //Toast.makeText(requireContext(), String.valueOf(id) + String.valueOf(wattage) + name, Toast.LENGTH_SHORT).show();
                    puissanceCalculee += wattage;
                    equipementPrincipaux.add(new EquipementPrincipaux(id, name, wattage, 4));
                }
                EquipementPrincipauxAdapter adapter = new EquipementPrincipauxAdapter(requireContext(), equipementPrincipaux);
                listView.setAdapter(adapter);

                TextView puissance_ = requireView().findViewById(R.id.puissanceTV);
                puissance_.setText(String.valueOf(puissanceCalculee) + "W");
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
