package com.jugaado.jugaado.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.MessagingActivity;
import com.jugaado.jugaado.models.Message;
import com.jugaado.jugaado.models.MessageThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ravivooda on 9/9/15.
 */
public class MessagingAdapter extends ArrayAdapter {

    JSONObject obj;
    String buttonLink,buttonText;
    String iconValue1,fieldValue1,iconValue2,fieldValue2,iconValue3,fieldValue3;
    private MessageThread messageThread;
    ArrayList<CharSequence> keys = new ArrayList<CharSequence>();
    ArrayList<CharSequence> values = new ArrayList<CharSequence>();
    private ArrayList<Message> messageArrayList = new ArrayList<>();
    Context mContext;


    public MessagingAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.mContext=context;
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

        LinearLayout rightContainer = (LinearLayout) row.findViewById(R.id.message_right_container);
        TextView rightTextView = (TextView) row.findViewById(R.id.message_right_text_view);
        TextView datentimeR=(TextView)row.findViewById(R.id.datetimeRtextView);

        LinearLayout leftContainer = (LinearLayout) row.findViewById(R.id.message_left_container);
        TextView leftTextView = (TextView) row.findViewById(R.id.message_left_text_view);
        TextView datentimeL=(TextView)row.findViewById(R.id.datetimeLtextView);
        Button leftButton=(Button)row.findViewById(R.id.message_left_button);

        final LinearLayout formLayout = (LinearLayout) row.findViewById(R.id.form_linearLayout);
        final EditText field1 =(EditText)row.findViewById(R.id.form_field1);
        final EditText field2 =(EditText)row.findViewById(R.id.form_field2);
        final EditText field3 =(EditText)row.findViewById(R.id.form_field3);
        Button sendForm = (Button)row.findViewById(R.id.form_send_button);
        switch (chatMessageObj.getMessage_way()){
            case MESSAGE_WAY_IN:
                rightContainer.setVisibility(View.GONE);
                rightTextView.setText("");
                leftContainer.setVisibility(View.VISIBLE);
                if(isJSONValid(chatMessageObj.getMessage())){
                    try {
                        obj = new JSONObject(chatMessageObj.getMessage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Iterator itrOuter = obj.keys();


                    int i = 0;
                    while(itrOuter.hasNext()) {
                        String key = itrOuter.next().toString();

                        try {
                            keys.add(i,key);
                            values.add(i, (CharSequence) obj.getString(key));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } //for example
                        i++;
                    }

                     if(keys.get(0).equals("type") && values.get(0).equals("button")){
                         buttonLink = values.get(1).toString();
                         buttonText = values.get(2).toString();
                         formLayout.setVisibility(View.INVISIBLE);
                         leftTextView.setVisibility(View.INVISIBLE);
                         leftButton.setVisibility(View.VISIBLE);
                         leftButton.setText(buttonText);
                         leftButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!buttonLink.startsWith("http://") && !buttonLink.startsWith("https://")) {
                                    buttonLink = "http://" + buttonLink;
                                }
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(buttonLink));
                                view.getContext().startActivity(browserIntent);
                            }

                        });
                     }
                    if(keys.get(0).equals("type") && values.get(0).equals("form")){
                        iconValue1= values.get(1).toString();
                        fieldValue1= values.get(2).toString();
                        iconValue2= values.get(3).toString();
                        fieldValue2= values.get(4).toString();
                        iconValue3= values.get(5).toString();
                        fieldValue3= values.get(6).toString();
                        leftContainer.setVisibility(View.INVISIBLE);
                        formLayout.setVisibility(View.VISIBLE);
                        field1.setHint(fieldValue1);
                        field2.setHint(fieldValue2);
                        field3.setHint(fieldValue3);
                        //final Message message = new Message(fieldValue1+": "+field1.getText()+"\n"+fieldValue2+": "+field2.getText()+"\n"+fieldValue3+": "+field3.getText());
                        sendForm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //((MessagingAdapter) view.getContext()).function();
                                if(mContext instanceof MessagingActivity){
                                    ((MessagingActivity)mContext).sendMessage(fieldValue1+": "+field1.getText()+"\n"+fieldValue2+": "+field2.getText()+"\n"+fieldValue3+": "+field3.getText());
                                    formLayout.setVisibility(View.GONE);
                                }

                            }

                        });                   }

                }
                else {
                    formLayout.setVisibility(View.GONE);
                    leftContainer.setVisibility(View.VISIBLE);
                    leftButton.setVisibility(View.GONE);
                    leftTextView.setVisibility(View.VISIBLE);
                    leftTextView.setText(chatMessageObj.getMessage());

                }
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

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


}
