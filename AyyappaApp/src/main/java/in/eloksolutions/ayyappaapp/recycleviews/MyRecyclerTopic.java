package in.eloksolutions.ayyappaapp.recycleviews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.PlayYoutubeActivity;
import in.eloksolutions.ayyappaapp.activities.TopicView;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.util.TopicObject;

public class MyRecyclerTopic extends RecyclerView
        .Adapter<MyRecyclerTopic
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<TopicObject> mDataset;
    private static MyClickListener myClickListener;
    private Context mcontext;
    Glide glide;

    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View

            .OnClickListener {
        TextView label;
       TextView label2,label3;
        ImageView imageView;
        Button btnPalay;


        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.user_name);
           label2 = (TextView) itemView.findViewById(R.id.date);
            label3=(TextView)  itemView.findViewById(R.id.forum_title);
            btnPalay=(Button) itemView.findViewById(R.id.play);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.image);
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
            btnPalay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "Adding Topic Listener "+label.getText());
                    TopicObject dataObject=mDataset.get(getAdapterPosition());
                    Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
                    String video_url=dataObject.getDescription();
                    String video_code = (video_url.substring(video_url.lastIndexOf("=") + 1));
                    Intent topicView=new Intent(view.getContext(), PlayYoutubeActivity.class);
                    topicView.putExtra("uri",video_code);
                    Log.i(LOG_TAG, "topicId is imag eclick video_code :"+video_code);
                    view.getContext().startActivity(topicView);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "Adding Topic Listener "+label.getText());
            TopicObject dataObject=mDataset.get(getAdapterPosition());
            Log.i(LOG_TAG, "data object is Topic Listener"+dataObject);
            Intent topicView=new Intent(v.getContext(), TopicView.class);
            topicView.putExtra("topicId",dataObject.getTopicId());
            Log.i(LOG_TAG, "topicId is imag eclick :"+dataObject.getTopicId());
            v.getContext().startActivity(topicView);
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerTopic(ArrayList<TopicObject> myDataset, Context mcontext) {
        mDataset = myDataset;
        this.mcontext=mcontext;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getName());
       holder.label2.setText(mDataset.get(position).getCreateDate());
        holder.label3.setText(mDataset.get(position).getTopic());
        if(mDataset.get(position).getDescription()!=null){
            if(mDataset.get(position).getDescription().contains("youtube")) {
                holder.btnPalay.setVisibility(View.VISIBLE);
            }
        }
        if(mDataset.get(position).getImgResource()!=null) {
            glide.with(mcontext).load(Config.S3_URL + mDataset.get(position).getImgResource()).into(holder.imageView);
        }else{
            glide.with(mcontext).load(R.drawable.defaulta).into(holder.imageView);

        }
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