package fr.iut.projet_mobile_s4_01_powerhome.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementAddActivity;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementModifActivity;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementPrincipaux;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementPrincipauxAdapter;

public class NotificationFragment extends Fragment {

    private Integer id;
    private DatabaseManager databaseManager;
    private List<Notification> notifications;
    private ListView listView;
    private TextView nbNotification_;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        listView = view.findViewById(R.id.notificationListView);

        notifications = new ArrayList<>();
        databaseManager = new DatabaseManager(requireContext());

        nbNotification_ = view.findViewById(R.id.nbNotification);

        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        getNotifications();

        return view;
    }

    public void onApiResponseEquipement(JSONObject response) {
        Boolean success = null;
        Integer nbNotification = 0;

        try {
            success = response.getBoolean("success");
            if (success == true) {
                JSONArray notificationsArray = response.getJSONArray("notifications");

                for (int i = 0; i < notificationsArray.length(); i++) {
                    JSONObject applianceObject = notificationsArray.getJSONObject(i);
                    int id = applianceObject.getInt("id");
                    String title = applianceObject.getString("notification_title");
                    String notification = applianceObject.getString("notification");
                    String categorie = applianceObject.getString("notification_categorie");
                    ++nbNotification;
                    notifications.add(new Notification(id, title, notification, categorie));
                }
                NotificationAdapter adapter = new NotificationAdapter(requireContext(), notifications, R.layout.item_notification);
                listView.setAdapter(adapter);

                nbNotification_.setText("Vous avez " + nbNotification + (nbNotification > 1 ? " notifications !" : " notification !"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getNotifications() {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/getNotifications.php";
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