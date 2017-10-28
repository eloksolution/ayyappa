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
import in.eloksolutions.ayyappaapp.helper.GroupMemberObject;

public class MyRecyclerViewSwamiContacts extends RecyclerView
        .Adapter<MyRecyclerViewSwamiContacts
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<GroupMemberObject> mDataset;
    private static MyClickListener myClickListener;
    Context context;
    ImageView imageView;
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
                    GroupMemberObject dataObject=mDataset.get(getAdapterPosition());
                    Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
                    Intent topicView=new Intent(view.getContext(), UserView.class);
                    topicView.putExtra("swamiUserId",dataObject.getUserId());
                    Log.i(LOG_TAG, "userId is imag eclick dataObject.getUserId() :"+dataObject.getUserId());
                    view.getContext().startActivity(topicView);
                }
            });
        }

        @Override
        public void onClick(View v) {
            GroupMemberObject dataObject=mDataset.get(getAdapterPosition());
            Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
            Intent topicView=new Intent(v.getContext(), UserView.class);
            topicView.putExtra("swamiUserId",dataObject.getUserId());
            Log.i(LOG_TAG, "userId is imag eclick dataObject.getUserId() :"+dataObject.getUserId());
            v.getContext().startActivity(topicView);

        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewSwamiContacts(ArrayList<GroupMemberObject> myDataset, Context context) {
        mDataset = myDataset;
        this.context=context;
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
        holder.label.setText(mDataset.get(position).getFirstName()+mDataset.get(position).getLastName());
      // holder.label2.setText(mDataset.get(position).getDescription());

        if(mDataset.get(position).getImgPath()!=null) {
            glide.with(context).load(Config.S3_URL + mDataset.get(position).getImgPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            System.out.println("past from eventfromJson.gettime()" + mDataset.get(position).getImgPath());
        }else{
            glide.with(context).load(R.drawable.defaulta).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
    }
 
    public void addItem(GroupMemberObject dataObj, int index) {
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