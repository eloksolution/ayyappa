package in.eloksolutions.ayyappaapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.util.Message;


class ChatArrayAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messagesItems;
    private String msgSenderId;
    public ChatArrayAdapter(Context context, List<Message> navDrawerItems,String msgSenderId) {
        this.context = context;
        this.messagesItems = navDrawerItems;
        this.msgSenderId=msgSenderId;
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message m = messagesItems.get(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(msgSenderId.equals(m.getMsgSenderId()))
            convertView = mInflater.inflate(R.layout.right, null);
        else
            convertView = mInflater.inflate(R.layout.left, null);

      TextView  chatText = (TextView) convertView.findViewById(R.id.msgr);
        TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        chatText.setText(m.getMessage());
       lblFrom.setText(m.getMsgSenderName());
        return convertView;
    }
}
