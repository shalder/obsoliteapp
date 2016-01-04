package com.jugaado.jugaado.adapters;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.models.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ravivooda on 9/9/15.
 */
public class MessagingAdapter extends ArrayAdapter {

    private ArrayList<Message> messageArrayList = new ArrayList<>();

    public MessagingAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    public void setMessageArrayList(ArrayList<Message> messages){
        messageArrayList = messages;
    }

    @Override
    public void add(Object object) {
        messageArrayList.add((Message) object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.message_list_item, parent, false);
        }
        Message chatMessageObj = (Message) getItem(position);

        RelativeLayout rightContainer = (RelativeLayout) row.findViewById(R.id.message_right_container);
        TextView rightTextView = (TextView) row.findViewById(R.id.message_right_text_view);
        TextView datentimeR=(TextView)row.findViewById(R.id.datetimeRtextView);

        RelativeLayout leftContainer = (RelativeLayout) row.findViewById(R.id.message_left_container);
        TextView leftTextView = (TextView) row.findViewById(R.id.message_left_text_view);
        TextView datentimeL=(TextView)row.findViewById(R.id.datetimeLtextView);

        switch (chatMessageObj.getMessage_way()){
            case MESSAGE_WAY_IN:
                rightContainer.setVisibility(View.GONE);
                rightTextView.setText("");
                leftContainer.setVisibility(View.VISIBLE);
                leftTextView.setText(chatMessageObj.getMessage());
                //SimpleDateFormat sdfL = new SimpleDateFormat("dd.MM.yyyy   HH:mm:ss");
                //final String currentDateandTimeLeft = sdfL.format(new Date());
                datentimeL.setText(chatMessageObj.getDATEnTIME());
                break;
            case MESSAGE_WAY_OUT:
                leftContainer.setVisibility(View.GONE);
                leftTextView.setText("");
                rightContainer.setVisibility(View.VISIBLE);
                rightTextView.setText(chatMessageObj.getMessage());
                SimpleDateFormat sdfR = new SimpleDateFormat("dd.MM.yyyy   HH:mm:ss");
                final String currentDateandTimeRight = sdfR.format(new Date());
                datentimeR.setText(chatMessageObj.getDATEnTIME());
                break;
        }
        return row;
    }
}
