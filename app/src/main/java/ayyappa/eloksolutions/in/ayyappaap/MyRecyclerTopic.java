package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.util.TopicObject;

public class MyRecyclerTopic extends RecyclerView
        .Adapter<MyRecyclerTopic
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<TopicObject> mDataset;
    private static MyClickListener myClickListener;

    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View

            .OnClickListener {
        TextView label;
       TextView label2;
        ImageView imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
          // label2 = (TextView) itemView.findViewById(R.id.group_desc);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.topic_view_click);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "Adding Topic Listener "+label.getText());
                    TopicObject dataObject=mDataset.get(getAdapterPosition());
                    Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
                    Intent topicView=new Intent(view.getContext(), TopicView.class);
                    topicView.putExtra("topicId",dataObject.getTopicId());
                    Log.i(LOG_TAG, "topicId is imag eclick :"+dataObject.getTopicId());
                    view.getContext().startActivity(topicView);
                }
            });
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);

        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerTopic(ArrayList<TopicObject> myDataset) {
        mDataset = myDataset;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row_topic, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getTopic());
      // holder.label2.setText(mDataset.get(position).getDescription());
        holder.imageView.setImageResource(mDataset.get(position).getImgResource());
    }
 
    public void addItem(TopicObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }
 
    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }
 
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
 
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}