package fr.iut.projet_mobile_s4_01_powerhome.app.creneau;

import android.content.Intent;
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
import java.util.List;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;

//Fragment pour voir les créneaux
public class TimeSlotFragment extends Fragment {

    private Integer id;
    private DatabaseManager databaseManager;
    private List<TimeSlot> timeSlots;
    private ListView listView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TimeSlotFragment() {
        // Required empty public constructor
    }

    public static TimeSlotFragment newInstance(String param1, String param2) {
        TimeSlotFragment fragment = new TimeSlotFragment();
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
        View view =  inflater.inflate(R.layout.fragment_time_slot, container, false);

        listView = view.findViewById(R.id.creneauListView);

        timeSlots = new ArrayList<>();
        databaseManager = new DatabaseManager(requireContext());

        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        getTimeSlots();

        return view;
    }

    public void onApiResponseEquipement(JSONObject response) {
        try {
            boolean success = response.getBoolean("success");
            if (success) {
                JSONArray timeSlotsArray = response.getJSONArray("timeSlots");
                List<TimeSlot> timeSlots = new ArrayList<>();

                for (int i = 0; i < timeSlotsArray.length(); i++) {
                    JSONObject timeSlotObject = timeSlotsArray.getJSONObject(i);
                    int id = timeSlotObject.getInt("id");
                    String begin = timeSlotObject.getString("begin");
                    String end = timeSlotObject.getString("end");
                    int maxWattage = timeSlotObject.getInt("max_wattage");
                    int wattageUsed = timeSlotObject.getInt("wattage_used");

                    TimeSlot timeSlot = new TimeSlot(id, begin, end, maxWattage, wattageUsed);
                    timeSlots.add(timeSlot);
                }

                TimeSlotAdapter adapter = new TimeSlotAdapter(requireContext(), timeSlots, R.layout.item_creneau);
                listView.setAdapter(adapter);

            } else {
                String error = response.getString("error");
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erreur lors de l'analyse des données", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTimeSlots() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getTimeSlots.php";
        JSONObject parameters = new JSONObject();
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