package com.tsr.android.tourmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tsult on 08-May-17.
 */

public class EventAdapter extends ArrayAdapter<EventList> {
    private ArrayList<EventList> eventList;
    private Context context;
    public EventAdapter(Context context, ArrayList<EventList> eventList) {
        super(context, R.layout.single_event_row);
        this.context=context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.single_event_row,parent,false);
        TextView eventName = (TextView) convertView.findViewById(R.id.event_name_tv);
        TextView destination = (TextView) convertView.findViewById(R.id.destination_tv);

        eventName.setText(eventList.get(position).getEventName());
        destination.setText(eventList.get(position).getDestination());
        return convertView;
    }
}
