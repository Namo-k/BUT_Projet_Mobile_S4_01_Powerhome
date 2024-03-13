package fr.iut.projet_mobile_s4_01_powerhome.app.equipement;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.iut.projet_mobile_s4_01_powerhome.R;

public class EquipementPrincipauxAdapter extends ArrayAdapter<EquipementPrincipaux> {

    private final LayoutInflater inflater;
    private final int layoutResourceId;
    public EquipementPrincipauxAdapter(Context context, List<EquipementPrincipaux> equipementPrincipaux, int layoutResourceId) {
        super(context, 0, equipementPrincipaux);
        inflater = LayoutInflater.from(context);
        this.layoutResourceId = layoutResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(layoutResourceId, parent, false);
        }

        EquipementPrincipaux currentEquipementPrincipaux = getItem(position);

        TextView residentNameTV = itemView.findViewById(R.id.equipementTV);
        residentNameTV.setText(currentEquipementPrincipaux.getName());

        TextView floorTV = itemView.findViewById(R.id.puissanceTV);
        floorTV.setText(String.valueOf(currentEquipementPrincipaux.getWattage() + " W"));

        ImageView equipementImage = itemView.findViewById(R.id.imageEquipementIV);
        equipementImage.setVisibility(View.VISIBLE);

        int imageResourceId = EquipementPrincipaux.getImageResourceIdByName(currentEquipementPrincipaux.getName());
        if (imageResourceId != 0) {
            equipementImage.setImageResource(imageResourceId);
        }

        return itemView;
    }
}
