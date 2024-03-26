package fr.iut.projet_mobile_s4_01_powerhome.app.residence;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



import fr.iut.projet_mobile_s4_01_powerhome.DatabaseManager;
import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementPrincipaux;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementPrincipauxAdapter;
import fr.iut.projet_mobile_s4_01_powerhome.app.user.Notification;
import fr.iut.projet_mobile_s4_01_powerhome.app.user.PreferenceFragment;

public class CreneauFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Integer id;

    private boolean verificationReussie = false;
    private DatabaseManager databaseManager;

    private final Map<String, Integer> equipementConsoMap = new HashMap<>();
    private final List<TimeSlot> timeSlots = new ArrayList<>();

    private Notification notif;

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

        TextView debutdateJourET = view.findViewById(R.id.debutdateJourET);
        TextView debutdateMoisET = view.findViewById(R.id.debutdateMoisET);
        TextView debutdateAnneeET = view.findViewById(R.id.debutdateAnneeET);

        Spinner debutSpinner = view.findViewById(R.id.debutET);
        debutSpinner.setAdapter(new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, maListe));
        Spinner finSpinner = view.findViewById(R.id.finET);
        finSpinner.setAdapter(new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, maListe));

        databaseManager = new DatabaseManager(requireContext());

        Spinner equipementSpinner = view.findViewById(R.id.equipement1Spinner);
        TextView consoEquipement = view.findViewById(R.id.equipement1ConsoTV);
        TextView consoCreneau = view.findViewById(R.id.consoCreneauTV);
        TextView msgTV = view.findViewById(R.id.msgTV);
        CardView btnEnregistrerCreneau = view.findViewById(R.id.btnEnregistrerCreneau);
        CardView btnVerifierCreneau = view.findViewById(R.id.btnVerifierCreneau);
        CardView btnAnnulerCreneau = view.findViewById(R.id.btnAnnulerCreneau);

        TextView btnCalendar = view.findViewById(R.id.btnCalendarTV);

        getEquipements(view);
        getTimeSlot(view);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }

            private void openDatePicker(){
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        debutdateJourET.setText(String.valueOf(day));
                        debutdateMoisET.setText(String.valueOf(month));
                        debutdateAnneeET.setText(String.valueOf(year));
                    }
                }, 2024, 03, 25);

                datePickerDialog.show();
            }
        });

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

                String debutTime = debutSpinner.getSelectedItem().toString();
                String finTime = finSpinner.getSelectedItem().toString();
                String jour = debutdateJourET.getText().toString();
                String mois = debutdateMoisET.getText().toString();
                String annee = debutdateAnneeET.getText().toString();


                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int selectedYear = Integer.parseInt(annee);
                int selectedMonth = Integer.parseInt(mois);
                int selectedDay = Integer.parseInt(jour);

                if (jour.isEmpty() || mois.isEmpty() || annee.isEmpty()) {
                    Toast.makeText(requireContext(), "Veuillez remplir tous les champs de la date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (debutTime.equals("Sélectionnez l'heure de début") || finTime.equals("Sélectionnez l'heure de fin")) {
                    Toast.makeText(requireContext(), "Veuillez sélectionner une heure de début et de fin", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int day = Integer.parseInt(jour);
                    int month = Integer.parseInt(mois);
                    int year = Integer.parseInt(annee);
                    if (day < 1 || day > 31 || month < 1 || month > 12 || year < 2023) {
                        Toast.makeText(requireContext(), "Format de date invalide", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Format de date invalide", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedYear < currentYear || (selectedYear == currentYear && selectedMonth < currentMonth) || (selectedYear == currentYear && selectedMonth == currentMonth && selectedDay < currentDay)) {
                    Toast.makeText(requireContext(), "La date doit être supérieure à aujourd'hui", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("HH'h'", Locale.FRANCE);
                try {
                    Date debutDate = sdf.parse(debutTime);
                    Date finDate = sdf.parse(finTime);
                    if (debutDate.compareTo(finDate) >= 0) {
                        Toast.makeText(requireContext(), "L'heure de début doit être strictement supérieure à l'heure de fin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String dateDebutComplete = annee + "-" + mois + "-" + jour + " " + debutTime.replace("h", ":00:00");
                    String dateFinComplete = annee + "-" + mois + "-" + jour + " " + finTime.replace("h", ":00:00");

                    if (equipementSpinner.getSelectedItemPosition() <= 0) {
                        Toast.makeText(requireContext(), "Veuillez sélectionner un équipement pour vérifier", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String equipementName = equipementSpinner.getSelectedItem().toString();
                    int equipementId = Integer.parseInt(equipementName.split(" ", 2)[0]);
                    Integer equipementConso = equipementConsoMap.getOrDefault(equipementName, 0);

                    boolean creneauFound = false;
                    for (TimeSlot slot : timeSlots) {
                        if (slot.getBegin().equals(dateDebutComplete) && slot.getEnd().equals(dateFinComplete)) {
                            creneauFound = true;
                            int totalConso = slot.getWattageUsed() + equipementConso;
                            if (totalConso > slot.getMaxWattage()) {
                                msgTV.setVisibility(View.VISIBLE);
                                msgTV.setText("Ce créneau est saturé, vous pourrez avoir un malus pour avoir utilisé 3x le même créneau");
                            } else {
                                msgTV.setVisibility(View.VISIBLE);
                                msgTV.setText("Vous pouvez réserver ce créneau sans soucis !");
                                msgTV.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                            }

                            consoCreneau.setText(slot.getWattageUsed() + "W");
                        }

                    }

                    if (!creneauFound) {
                        int maxWattage = 25000;
                        int totalConso = equipementConso;
                        if (totalConso > maxWattage) {
                            msgTV.setVisibility(View.VISIBLE);
                            msgTV.setText("Ce créneau est saturé, vous pourrez avoir un malus pour avoir utilisé 3x le même créneau");
                        } else {
                            msgTV.setVisibility(View.VISIBLE);
                            msgTV.setText("Vous pouvez réserver ce créneau sans soucis !");
                            msgTV.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                        }
                        consoCreneau.setText("0W");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnEnregistrerCreneau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String debutTime = debutSpinner.getSelectedItem().toString();
                String finTime = finSpinner.getSelectedItem().toString();
                String jour = debutdateJourET.getText().toString();
                String mois = debutdateMoisET.getText().toString();
                String annee = debutdateAnneeET.getText().toString();


                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int selectedYear = Integer.parseInt(annee);
                int selectedMonth = Integer.parseInt(mois);
                int selectedDay = Integer.parseInt(jour);

                if (jour.isEmpty() || mois.isEmpty() || annee.isEmpty()) {
                    Toast.makeText(requireContext(), "Veuillez remplir tous les champs de la date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (debutTime.equals("Sélectionnez l'heure de début") || finTime.equals("Sélectionnez l'heure de fin")) {
                    Toast.makeText(requireContext(), "Veuillez sélectionner une heure de début et de fin", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int day = Integer.parseInt(jour);
                    int month = Integer.parseInt(mois);
                    int year = Integer.parseInt(annee);
                    if (day < 1 || day > 31 || month < 1 || month > 12 || year < 2023) {
                        Toast.makeText(requireContext(), "Format de date invalide", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Format de date invalide", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedYear < currentYear || (selectedYear == currentYear && selectedMonth < currentMonth) || (selectedYear == currentYear && selectedMonth == currentMonth && selectedDay < currentDay)) {
                    Toast.makeText(requireContext(), "La date doit être supérieure à aujourd'hui", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("HH'h'", Locale.FRANCE);
                try {
                    Date debutDate = sdf.parse(debutTime);
                    Date finDate = sdf.parse(finTime);
                    if (debutDate.compareTo(finDate) >= 0) {
                        Toast.makeText(requireContext(), "L'heure de début doit être strictement supérieure à l'heure de fin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String dateDebutComplete = annee + "-" + mois + "-" + jour + " " + debutTime.replace("h", ":00:00");
                    String dateFinComplete = annee + "-" + mois + "-" + jour + " " + finTime.replace("h", ":00:00");

                    if (equipementSpinner.getSelectedItemPosition() <= 0) {
                        Toast.makeText(requireContext(), "Veuillez sélectionner un équipement pour vérifier", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String equipementName = equipementSpinner.getSelectedItem().toString();
                    int equipementId = Integer.parseInt(equipementName.split(" ", 2)[0]);
                    Integer equipementConso = equipementConsoMap.getOrDefault(equipementName, 0);

                    boolean creneauFound = false;
                    for (TimeSlot slot : timeSlots) {
                        if (slot.getBegin().equals(dateDebutComplete) && slot.getEnd().equals(dateFinComplete)) {
                            creneauFound = true;
                            int totalConso = slot.getWattageUsed() + equipementConso;
                            if (totalConso > slot.getMaxWattage()) {
                                msgTV.setVisibility(View.VISIBLE);
                                msgTV.setText("Ce créneau est saturé, vous pourrez avoir un malus pour avoir utilisé 3x le même créneau");
                            } else {
                                msgTV.setVisibility(View.VISIBLE);
                                msgTV.setText("Vous pouvez réserver ce créneau sans soucis !");
                                msgTV.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                            }

                            consoCreneau.setText(slot.getWattageUsed() + "W");

                            long duree = finDate.getTime() - debutDate.getTime();
                            long uneHeure = 3600000; // 1 heure en millisecondes

                            notif = new Notification("Sélection d'un créneau", "Vous avez réservé le créneau de "+ debutTime+" à " + finTime + " pour votre "+ equipementName +".", "creneau");
                            ajouterNotification(notif);

                            if (duree > uneHeure) {
                                for (long currentTime = debutDate.getTime(); currentTime < finDate.getTime(); currentTime += uneHeure) {
                                    Date currentBeginTime = new Date(currentTime);
                                    Date currentEndTime = new Date(currentTime + uneHeure);

                                    String currentBeginTimeString = sdf.format(currentBeginTime);
                                    String currentEndTimeString = sdf.format(currentEndTime);

                                   // Toast.makeText(requireContext(), currentBeginTimeString + " " + currentEndTimeString , Toast.LENGTH_SHORT).show();

                                    dateDebutComplete = annee + "-" + mois + "-" + jour + " " + currentBeginTimeString.replace("h", ":00:00");
                                    dateFinComplete = annee + "-" + mois + "-" + jour + " " + currentEndTimeString.replace("h", ":00:00");

                                    ajouterEquipementAuNouveauCreneau(dateDebutComplete, dateFinComplete, equipementConso, equipementId);

                                }
                            } else {
                                ajouterEquipementAuNouveauCreneau(dateDebutComplete, dateFinComplete, equipementConso, equipementId);
                            }
                        }

                    }

                    if (!creneauFound) {
                        int maxWattage = 25000;
                        int totalConso = equipementConso;
                        if (totalConso > maxWattage) {
                            msgTV.setVisibility(View.VISIBLE);
                            msgTV.setText("Ce créneau est saturé, vous pourrez avoir un malus pour avoir utilisé 3x le même créneau");
                        } else {
                            msgTV.setVisibility(View.VISIBLE);
                            msgTV.setText("Vous pouvez réserver ce créneau sans soucis !");
                            msgTV.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                        }
                        consoCreneau.setText("0W");
                        long duree = finDate.getTime() - debutDate.getTime();
                        long uneHeure = 3600000; // 1 heure en millisecondes

                        notif = new Notification("Sélection d'un créneau", "Vous avez réservé le créneau de "+ debutTime+" à " + finTime + " pour votre "+ equipementName +".", "creneau");
                        ajouterNotification(notif);
                        if (duree > uneHeure) {
                            for (long currentTime = debutDate.getTime(); currentTime < finDate.getTime(); currentTime += uneHeure) {
                                Date currentBeginTime = new Date(currentTime);
                                Date currentEndTime = new Date(currentTime + uneHeure);

                                String currentBeginTimeString = sdf.format(currentBeginTime);
                                String currentEndTimeString = sdf.format(currentEndTime);


                                dateDebutComplete = annee + "-" + mois + "-" + jour + " " + currentBeginTimeString.replace("h", ":00:00");
                                dateFinComplete = annee + "-" + mois + "-" + jour + " " + currentEndTimeString.replace("h", ":00:00");

                                ajouterEquipementAuNouveauCreneau(dateDebutComplete, dateFinComplete, equipementConso, equipementId);
                            }
                           // Toast.makeText(requireContext(), "Équipement ajouté avec succès" , Toast.LENGTH_SHORT).show();

                        } else {
                            ajouterEquipementAuNouveauCreneau(dateDebutComplete, dateFinComplete, equipementConso, equipementId);
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
    private String convertSpinnerTimeToDate(String spinnerTime) {
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        DateFormat currentDateFormatter = new SimpleDateFormat("yyyy-MM-dd ", Locale.FRANCE);
        String currentDate = currentDateFormatter.format(new Date());
        String time = spinnerTime.replace("h", "") + ":00:00";

        return currentDate + time;
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
                    int id = applianceObject.getInt("id");
                    int consommation = applianceObject.getInt("wattage");

                    equipementNames.add(id + " " + name);

                    equipementConsoMap.put(id + " " + name, consommation);
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

    public void ajouterEquipementAuNouveauCreneau(String dateDebut, String dateFin, int consommationEquipement, int equipementId) {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/ajoutEquipementAuNouveauCreneau_copy.php";
        Map<String, String> params = new HashMap<>();
        params.put("date_debut", dateDebut);
        params.put("date_fin", dateFin);
        params.put("consommation", String.valueOf(consommationEquipement));
        params.put("equipement_id", String.valueOf((equipementId)));
        params.put("id", String.valueOf(id));

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, response -> {
            try {
                boolean success = response.getBoolean("success");
                if (success) {
                    Toast.makeText(requireContext(), "Équipement ajouté au créneau avec succès!", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = response.has("error") ? response.getString("error") : "Une erreur s'est produite lors de l'ajout de l'équipement.";
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Erreur lors du traitement de la réponse du serveur.", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(requireContext(), "Erreur de communication avec le serveur: " + error.toString(), Toast.LENGTH_SHORT).show());

        databaseManager.queue.add(jsonObjectRequest);
    }
    public void ajouterNotification(Notification notif) {
        String url = "http://10.0.2.2:2000/powerhome_server/actions/ajoutNotification.php";
        Map<String, String> params = new HashMap<>();
        params.put("title", notif.getTitle());
        params.put("notification", notif.getNotification());
        params.put("categorie", notif.getCategorie());
        params.put("id", String.valueOf(id));
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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

