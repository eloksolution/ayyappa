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

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.util.DataObjectPadiPooja;


public class MyRecyclerViewAdapterHome extends RecyclerView
        .Adapter<MyRecyclerViewAdapterHome
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapterHome";
    private ArrayList<DataObjectPadiPooja> mDataset;
    private Context mcontext;
    private static MyClickListener myClickListener;
    Glide glide;

    TransferUtility transferUtility;

    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label, label2, count, time,null_value;
        ImageView imageView;
        Button joinBtn;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.group_title);
            label2 = (TextView) itemView.findViewById(R.id.group_desc);
            count  =(TextView) itemView.findViewById(R.id.badge_notification);
            time=(TextView) itemView.findViewById(R.id.dsate);
            null_value=(TextView) itemView.findViewById(R.id.null_value);

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

    public MyRecyclerViewAdapterHome(ArrayList<DataObjectPadiPooja> myDataset, Context mcontext) {
        mDataset = myDataset;
        this.mcontext=mcontext;
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
        Log.i(LOG_TAG, "Adding image :" +mDataset.get(position).getImgResource());
        if(mDataset.get(position).getImgResource()!=null)
            glide.with(mcontext).load(Config.S3_URL+mDataset.get(position).getImgResource()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        else
            holder.imageView.setImageResource(R.drawable.defaulta);

        Log.i(LOG_TAG, "Adding description :" + mDataset.get(position).getmText2());


            if (mDataset.get(position).getMemberSize() != 0) {
                holder.count.setText(mDataset.get(position).getMemberSize() + "are Joined");
            } else {
                holder.count.setText("0 Joined");
            }
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



