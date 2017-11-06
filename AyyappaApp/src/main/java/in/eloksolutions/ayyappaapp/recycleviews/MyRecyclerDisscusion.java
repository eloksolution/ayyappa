package in.eloksolutions.ayyappaapp.recycleviews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.UserView;
import in.eloksolutions.ayyappaapp.util.DisObject;

public class MyRecyclerDisscusion extends RecyclerView
        .Adapter<MyRecyclerDisscusion
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DisObject> mDataset;
    private static MyClickListener myClickListener;
Context context;
    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View

            .OnClickListener {
        TextView label;
       TextView label2, label3;
        ImageView imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.person_name);
           label2 = (TextView) itemView.findViewById(R.id.person_date);
            label3=(TextView) itemView.findViewById(R.id.person_comment);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.person_photo);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "Adding Topic Listener "+label.getText());
                    DisObject dataObject=mDataset.get(getAdapterPosition());
                    Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
                    Intent topicView=new Intent(view.getContext(), UserView.class);
                    topicView.putExtra("userId",dataObject.getUserName());
                    Log.i(LOG_TAG, "topicId is imag eclick :"+dataObject.getUserId());
                    view.getContext().startActivity(topicView);
                }
            });
        }

        @Override
        public void onClick(View v) {

            Log.i(LOG_TAG, "Adding Topic Listener "+label.getText());
            DisObject dataObject=mDataset.get(getAdapterPosition());
            Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
            Intent topicView=new Intent(v.getContext(), UserView.class);
            topicView.putExtra("userId",dataObject.getUserName());
            Log.i(LOG_TAG, "topicId is imag eclick :"+dataObject.getUserId());
            v.getContext().startActivity(topicView);
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
    }

    public MyRecyclerDisscusion(ArrayList<DisObject> myDataset, Context context) {
        mDataset = myDataset;
        this.context=context;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discussion_recycler_list, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getUserId());
        holder.label3.setText(mDataset.get(position).getComment());
       holder.label2.setText(mDataset.get(position).getsPostDate());
        holder.imageView.setImageResource(mDataset.get(position).getImgPath());

    }
 
    public void addItem(DisObject dataObj, int index) {
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