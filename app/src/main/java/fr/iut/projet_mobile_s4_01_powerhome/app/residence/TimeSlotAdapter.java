package fr.iut.projet_mobile_s4_01_powerhome.app.residence;
import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.iut.projet_mobile_s4_01_powerhome.R;

public class TimeSlotAdapter extends ArrayAdapter<TimeSlot> {

    private final LayoutInflater inflater;
    private final int layoutResourceId;
    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlots, int layoutResourceId) {
        super(context, 0, timeSlots);
        inflater = LayoutInflater.from(context);
        this.layoutResourceId = layoutResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(layoutResourceId, parent, false);
        }

        TimeSlot currentTimeSlot = getItem(position);

        TextView dateTV = itemView.findViewById(R.id.dateTV);
        String dateOnly = currentTimeSlot.getBegin().split(" ")[0];
        dateTV.setText(dateOnly);

        TextView heureTV = itemView.findViewById(R.id.heureTV);
        String heureDeb = currentTimeSlot.getBegin().substring(11, 16);
        String heureFin = currentTimeSlot.getEnd().substring(11, 16);
        heureTV.setText(heureDeb + " - " + heureFin);

        TextView maxWattageTV = itemView.findViewById(R.id.maxWattageTV);
        maxWattageTV.setText("Max wattage : "+ currentTimeSlot.getMaxWattage() +" W");

        TextView usedWattageTV = itemView.findViewById(R.id.usedWattageTV);
        usedWattageTV.setText("Consommation utilisée : : "+ currentTimeSlot.getWattageUsed() +" W");

        ImageView timeSlotImage = itemView.findViewById(R.id.imageCreneau);
        timeSlotImage.setVisibility(View.VISIBLE);

        int imageResourceId = TimeSlot.getImageResourceIdByName(currentTimeSlot.getNiveau());
        if (imageResourceId != 0) {
            timeSlotImage.setImageResource(imageResourceId);
        }

        return itemView;
    }
}
