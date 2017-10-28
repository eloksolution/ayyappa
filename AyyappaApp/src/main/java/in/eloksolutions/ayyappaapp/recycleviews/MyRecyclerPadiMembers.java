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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.UserView;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.PadiObject;

public class MyRecyclerPadiMembers extends RecyclerView
        .Adapter<MyRecyclerPadiMembers
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<PadiObject> mDataset;
    private static MyClickListener myClickListener;
    ImageView imageView;
    Context ctx;
Glide glide;
    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View

            .OnClickListener {
        TextView label;
       TextView label2;


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
                    PadiObject dataObject=mDataset.get(getAdapterPosition());
                    Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
                    Intent topicView=new Intent(view.getContext(), UserView.class);
                    topicView.putExtra("swamiUserId",dataObject.getUserId());
                    Log.i(LOG_TAG, "userid padipooja is imag eclick :"+dataObject.getUserId());
                    view.getContext().startActivity(topicView);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "Adding Topic Listener "+label.getText());
            PadiObject dataObject=mDataset.get(getAdapterPosition());
            Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
            Intent topicView=new Intent(v.getContext(), UserView.class);
            topicView.putExtra("swamiUserId",dataObject.getUserId());
            Log.i(LOG_TAG, "userid padipooja is imag eclick :"+dataObject.getUserId());
            v.getContext().startActivity(topicView);
            Log.i(LOG_TAG, "Adding Listener onClick");
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerPadiMembers(ArrayList<PadiObject> myDataset, Context ctx) {
        mDataset = myDataset;
        this.ctx=ctx;

    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getFirstName()+" "+mDataset.get(position).getLastName());
      // holder.label2.setText(mDataset.get(position).getDescription());
if(mDataset.get(position).getImgPath()!=null) {
    glide.with(ctx).load(Config.S3_URL + mDataset.get(position).getImgPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
}else{
            glide.with(ctx).load(R.drawable.defaulta).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
    }
 
    public void addItem(PadiObject dataObj, int index) {
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