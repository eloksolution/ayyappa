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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.util.DataObjectPadiPooja;


public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObjectPadiPooja> mDataset;
    private static MyClickListener myClickListener;
    Context mcontext;
    Glide glide;
    String Tag= "MyRecyclerViewAdapter";
 
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
            Log.i(LOG_TAG, " Listener onClick");
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
    }

    public MyRecyclerViewAdapter(ArrayList<DataObjectPadiPooja> myDataset, Context mcontext) {
        mDataset = myDataset;
        this.mcontext= mcontext;
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
        if(mDataset.get(position).getImgResource()!=null){
        glide.with(mcontext).load(Config.S3_URL+mDataset.get(position).getImgResource()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
    }else{
        glide.with(mcontext).load(R.drawable.dt).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

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