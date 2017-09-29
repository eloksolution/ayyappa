package ayyappa.eloksolutions.in.ayyappaap;

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

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.util.DataObjectPadiPooja;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;


public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObjectPadiPooja> mDataset;
    private static MyClickListener myClickListener;
    private AmazonS3 s3;
    TransferUtility transferUtility;
    Context mcontext;
    Glide glide;
    String Tag;
 
    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label, date, count, month,week;
        ImageView imageView;
        Button joinBtn;
 
        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.topic);
            date = (TextView) itemView.findViewById(R.id.date);
            count  =(TextView) itemView.findViewById(R.id.people_join);
            month=(TextView) itemView.findViewById(R.id.month);
            week=(TextView) itemView.findViewById(R.id.day);
            Log.i(LOG_TAG, "Adding Listener");

            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.event_image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "postion"+getAdapterPosition()+ "postion value"+mDataset.get(getAdapterPosition()));
                    Log.i(LOG_TAG, "Adding Listener "+label.getText()+"description"+date.getText());
                    DataObjectPadiPooja dataObject=mDataset.get(getAdapterPosition());
                    Log.i(LOG_TAG, "data object is Listener"+dataObject);
                    Intent padiPoojaView=new Intent(view.getContext(), PadiPoojaView.class);
                    padiPoojaView.putExtra("padiPoojaId",dataObject.getPadipoojaId());
                    view.getContext().startActivity(padiPoojaView);

                }
            });

        }
 
        @Override
        public void onClick(View v) {

        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
    }

    public MyRecyclerViewAdapter(ArrayList<DataObjectPadiPooja> myDataset, Context mcontext, AmazonS3 s3, TransferUtility transferUtility) {
        mDataset = myDataset;
        this.mcontext=mcontext;
        this.transferUtility=transferUtility;
        this.s3=s3;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.padi_recycler, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        holder.date.setText(mDataset.get(position).getDay());
        holder.month.setText(mDataset.get(position).getMonth());
       holder.week.setText(mDataset.get(position).getWeek());
        getBitMap(mDataset.get(position).getImgResource(), holder.imageView);
        Log.i(LOG_TAG, "Adding mDataset.get(position).getWeek() :"+mDataset.get(position).getWeek());
       // holder.imageView.setImageBitmap(mDataset.get(position).getImgResource());
        if (mDataset.get(position).getMemberSize()!=0) {
            holder.count.setText(mDataset.get(position).getMemberSize() + " are Joined");
        }
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
                      /* Bitmap bit= BitmapFactory.decodeFile(fileToDownload.getAbsolutePath());
                       Log.i(Tag, "beforebitmap"+bit.getWidth() + "-" + bit.getHeight());



                        Bitmap afterBitmap = ImageUtils.getInstant().getCompressedBitmap(fileToDownload.getAbsolutePath());
                        Log.i(Tag, "afterbitmap"+afterBitmap.getWidth() + "-" + afterBitmap.getHeight());
                        imageView.setImageBitmap(afterBitmap);*/
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

    public void addItem(DataObjectPadiPooja dataObj, int index) {
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