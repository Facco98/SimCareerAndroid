package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Notification;

import java.util.List;

public class NotificationListAdapter extends BaseAdapter {

    private List<Notification> notifications;
    private Context context;

    public NotificationListAdapter(List<Notification> notifications, Context context) {

        this.notifications = notifications;
        this.context = context;

    }


    @Override
    public int getCount() {
        return this.notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return this.notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_notification, null);

        Notification element = (Notification) this.getItem(position);
        TextView lblTitle = convertView.findViewById(R.id.notification_lbl_title);
        TextView lblDescription = convertView.findViewById(R.id.notification_lbl_content);

        lblTitle.setText(element.getTitle());
        lblDescription.setText(element.getDescription());

        return convertView;

    }
}
