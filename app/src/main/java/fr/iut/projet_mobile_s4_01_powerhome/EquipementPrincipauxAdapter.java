package fr.iut.projet_mobile_s4_01_powerhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipementPrincipauxAdapter extends ArrayAdapter<EquipementPrincipaux> {

    private final LayoutInflater inflater;
    private final Map<Integer, Integer> applianceImageMap;

    public EquipementPrincipauxAdapter(Context context, List<EquipementPrincipaux> equipementPrincipaux) {
        super(context, 0, equipementPrincipaux);
        inflater = LayoutInflater.from(context);
        applianceImageMap = createApplianceImageMap();
    }


    private Map<Integer, Integer> createApplianceImageMap() {
        Map<Integer, Integer> map = new HashMap<>();
        // Ajoutez vos images et leurs correspondances ici
        map.put(4, R.drawable.ic_machine_a_laver);
        return map;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.item_equipements_principaux, parent, false);
        }

        EquipementPrincipaux currentEquipementPrincipaux = getItem(position);

        TextView residentNameTV = itemView.findViewById(R.id.equipementTV);
        residentNameTV.setText(currentEquipementPrincipaux.getName());

        TextView floorTV = itemView.findViewById(R.id.puissanceTV);
        floorTV.setText(String.valueOf(currentEquipementPrincipaux.getWattage() + " W"));

        ImageView equipementImage = itemView.findViewById(R.id.imageEquipementIV);
        equipementImage.setVisibility(View.VISIBLE);

        Integer imageResource = applianceImageMap.get(currentEquipementPrincipaux.getImageId());
        if (imageResource != null) {
            equipementImage.setImageResource(imageResource);
        }

        return itemView;
    }
}
