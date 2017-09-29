package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.util.TopicObject;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;

public class MyRecyclerTopic extends RecyclerView
        .Adapter<MyRecyclerTopic
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<TopicObject> mDataset;
    private static MyClickListener myClickListener;
    private Context mcontext;
    private AmazonS3 s3;
    Glide glide;
    TransferUtility transferUtility;

    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View

            .OnClickListener {
        TextView label;
       TextView label2,label3;
        ImageView imageView;


        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.user_name);
           label2 = (TextView) itemView.findViewById(R.id.date);
            label3=(TextView)  itemView.findViewById(R.id.forum_title);
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
        }

        @Override
        public void onClick(View v) {

        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerTopic(ArrayList<TopicObject> myDataset, Context mcontext, AmazonS3 s3, TransferUtility transferUtility) {
        mDataset = myDataset;
        this.mcontext=mcontext;
        this.s3=s3;
        this.transferUtility=transferUtility;
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

        getBitMap(mDataset.get(position).getImgResource(), holder.imageView);
    }

    private void getBitMap(String imgResource, ImageView imageView) {
        try {
            File outdirectory = mcontext.getCacheDir();
            File fileToDownload = File.createTempFile("GRO", "jpg", outdirectory);
            setFileToDownload(imgResource, fileToDownload, imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFileToDownload(String imageKey, File fileToDownload, ImageView imageView){
        TransferObserver transferObserver=null;
        imageKey= "groups/G_302_1505918747142";
        if (Util.isEmpty(imageKey))return;

        transferObserver = transferUtility.download(
                "elokayyappa",     // The bucket to download from *//*
               imageKey,    // The key for the object to download *//*
                fileToDownload        // The file to download the object to *//*

        );

        transferObserverListener(transferObserver,imageView,fileToDownload);

    }
    public void transferObserverListener(TransferObserver transferObserver, final ImageView imageView,final File fileToDownload){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.i("File down load status", state+"");
                Log.i("File down load id", id+"");
                if("COMPLETED".equals(state.toString())){
                    try{
                        // Bitmap bit= ImageUtils.getInstant().getCompressedBitmap(fileToDownload.getAbsolutePath());
                        //imageView.setImageBitmap(bit);
                        glide.with(mcontext).load(fileToDownload.getAbsolutePath()).into(imageView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.e("percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error",ex);
            }


        });
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