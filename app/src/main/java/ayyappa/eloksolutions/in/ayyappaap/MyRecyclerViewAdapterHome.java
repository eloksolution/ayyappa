package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.util.DataObjectPadiPooja;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;


public class MyRecyclerViewAdapterHome extends RecyclerView
        .Adapter<MyRecyclerViewAdapterHome
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObjectPadiPooja> mDataset;
    private Context mcontext;
    private static MyClickListener myClickListener;
    private AmazonS3 s3;

    TransferUtility transferUtility;

    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label, label2, count, time;
        ImageView imageView;
        Button joinBtn;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.group_title);
            label2 = (TextView) itemView.findViewById(R.id.group_desc);
            count  =(TextView) itemView.findViewById(R.id.badge_notification);
            time=(TextView) itemView.findViewById(R.id.dsate);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.padi_image_list);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "postion"+getAdapterPosition()+ "postion value"+mDataset.get(getAdapterPosition()));
                    Log.i(LOG_TAG, "Adding Listener "+label.getText()+"description"+label2.getText());
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
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapterHome(ArrayList<DataObjectPadiPooja> myDataset, Context mcontext, AmazonS3 s3, TransferUtility transferUtility) {
        mDataset = myDataset;
        this.mcontext=mcontext;
        this.s3 = s3;
        this.transferUtility = transferUtility;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_card, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        holder.label2.setText(mDataset.get(position).getmText2());
        holder.time.setText(mDataset.get(position).getDate());

        Log.i(LOG_TAG, "Adding description :"+mDataset.get(position).getmText2());
        getBitMap(mDataset.get(position).getImgResource(), holder.imageView);

        if (mDataset.get(position).getMemberSize()!=0) {
            holder.count.setText(mDataset.get(position).getMemberSize() + "are Joined");
        }
        else {
            holder.count.setText(  "0 Joined");
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

    public void setFileToDownload(String imageKey, File fileToDownload, ImageView imageView) {
        TransferObserver transferObserver=null;
        if (Util.isEmpty(imageKey))return;
        transferObserver = transferUtility.download(
                "elokayyappa",     // The bucket to download from *//*
                imageKey,    // The key for the object to download *//*
                fileToDownload        // The file to download the object to *//*
        );

        transferObserverListener(transferObserver,imageView,fileToDownload);

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


    public void transferObserverListener(TransferObserver transferObserver, final ImageView imageView,final File fileToDownload){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.i("File down load status", state+"");
                Log.i("File down load id", id+"");
                if("COMPLETED".equals(state.toString())){
                    try{
                        Bitmap bit= BitmapFactory.decodeFile(fileToDownload.getAbsolutePath());
                        imageView.setImageBitmap(bit);
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



}