package fr.iut.projet_mobile_s4_01_powerhome.app.residence;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.Appliance;

public class ResidenceFragment extends Fragment {

    private Integer id;
    private DatabaseManager databaseManager;
    private List<Habitant> Habitants;
    private ListView listView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResidenceFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_residence, container, false);

        databaseManager = new DatabaseManager(requireContext());

        Habitants = new ArrayList<>();
        listView = view.findViewById(R.id.habitantlistView);
        getListeHabitants();

        return view;
    }

    public void onApiResponse(JSONObject response) {
        Boolean success = null;

        try {
            success = response.getBoolean("success");
            if (success == true) {
                JSONArray habitantsArray = response.getJSONArray("users");

                for (int i = 0; i < habitantsArray.length(); i++) {
                    JSONObject habitantObject = habitantsArray.getJSONObject(i);
                    Integer habitat_id = habitantObject.getInt("habitat_id");
                    String nomprenom = habitantObject.getString("prenom") + " " + habitantObject.getString("nom");
                    Integer ecocoin = habitantObject.getInt("bonus") - habitantObject.getInt("malus");
                    Integer consoTotal = habitantObject.getInt("consommation");
                    List<Appliance> equipementsPrincipaux = new ArrayList<>();

                    Habitants.add(new Habitant(habitat_id, nomprenom, equipementsPrincipaux, ecocoin, consoTotal));
                }

                HabitantAdapter adapter = new HabitantAdapter(requireContext(), Habitants);
                listView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getListeHabitants() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getListeHabitants.php";
        Map<String, String> params = new HashMap<>();
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponse(response);
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