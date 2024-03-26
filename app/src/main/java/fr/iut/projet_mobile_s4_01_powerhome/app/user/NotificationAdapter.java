package fr.iut.projet_mobile_s4_01_powerhome.app.user;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import fr.iut.projet_mobile_s4_01_powerhome.R;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private final LayoutInflater inflater;
    private final int layoutResourceId;
    public NotificationAdapter(Context context, List<Notification> notifications, int layoutResourceId) {
        super(context, 0, notifications);
        inflater = LayoutInflater.from(context);
        this.layoutResourceId = layoutResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(layoutResourceId, parent, false);
        }

        Notification currentNotitification = getItem(position);

        TextView titleNotifTV = itemView.findViewById(R.id.titleNotifTV);
        titleNotifTV.setText(currentNotitification.getTitle());

        TextView notifTextTV = itemView.findViewById(R.id.notifTextTV);
        notifTextTV.setText(String.valueOf(currentNotitification.getNotification()));

        ImageView notificationImage = itemView.findViewById(R.id.imageNotification);
        notificationImage.setVisibility(View.VISIBLE);

        int imageResourceId = Notification.getImageResourceIdByName(currentNotitification.getCategorie());
        if (imageResourceId != 0) {
            notificationImage.setImageResource(imageResourceId);
        }

        return itemView;
    }
}
