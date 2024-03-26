package fr.iut.projet_mobile_s4_01_powerhome.app.residence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.Appliance;

public class HabitantAdapter extends ArrayAdapter<Habitant> {
    private final LayoutInflater inflater;
    private final Map<Integer, Integer> applianceImageMap;

    public HabitantAdapter(Context context, List<Habitant> habitants) {
        super(context, 0, habitants);
        inflater = LayoutInflater.from(context);
        applianceImageMap = createApplianceImageMap();
    }

    private Map<Integer, Integer> createApplianceImageMap() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, R.drawable.ic_aspirateur);
        map.put(2, R.drawable.ic_climatiseur);
        map.put(3, R.drawable.ic_fer_a_repasser);
        map.put(4, R.drawable.ic_machine_a_laver);
        map.put(5, R.drawable.ic_television);
        map.put(6, R.drawable.ic_four);
        map.put(7, R.drawable.ic_radiateur);
        return map;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.item_habitant, parent, false);
        }

        Habitant currentHabitant = getItem(position);

        TextView habitantNameTV = itemView.findViewById(R.id.habitantNameTV);
        String habitantName = currentHabitant.getHabitantName();

        String[] parts = habitantName.split(" ");

        if(parts.length > 1) {
            parts[1] = parts[1].toUpperCase();
            habitantName = parts[0] + " " + parts[1];
        }

        habitantNameTV.setText(habitantName);

        TextView numberHabitantTV = itemView.findViewById(R.id.numberHabitantTV);
        numberHabitantTV.setText(String.valueOf(currentHabitant.getId()));

        TextView consoTV = itemView.findViewById(R.id.consoTV);
        consoTV.setText(String.valueOf(currentHabitant.getConsoTotal()));

        TextView ecoCoinTV = itemView.findViewById(R.id.ecoCoinTV);
        ecoCoinTV.setText(String.valueOf(String.valueOf(currentHabitant.getEcoCoin())));

        List<Appliance> appliances = currentHabitant.getAppliances();
        setApplianceImages(itemView, appliances);

        return itemView;
    }

    private void setApplianceImages(View itemView, List<Appliance> appliances) {
        int[] imageViewIds = {
                R.id.appliance1IV,
                R.id.appliance2IV,
                R.id.appliance3IV,
                R.id.appliance4IV
        };

        for (int i = 0; i < imageViewIds.length; i++) {
            ImageView imageView = itemView.findViewById(imageViewIds[i]);

            if (i < appliances.size()) {
                Appliance appliance = appliances.get(i);
                int applianceId = appliance.getId();

                imageView.setVisibility(View.VISIBLE);

                Integer imageResource = applianceImageMap.get(applianceId);
                if (imageResource != null) {
                    imageView.setImageResource(imageResource);
                }
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
