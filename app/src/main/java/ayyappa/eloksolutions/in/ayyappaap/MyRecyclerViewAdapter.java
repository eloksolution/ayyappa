package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.util.DataObjectPadiPooja;


public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObjectPadiPooja> mDataset;
    private static MyClickListener myClickListener;
 
    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label, label2, count, time,location;
        ImageView imageView;
        Button joinBtn;
 
        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            label2 = (TextView) itemView.findViewById(R.id.textView2);
            count  =(TextView) itemView.findViewById(R.id.badge_notification);
            time=(TextView) itemView.findViewById(R.id.date_time);
            location=(TextView) itemView.findViewById(R.id.location);
            Log.i(LOG_TAG, "Adding Listener");

            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imgPlay);
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
         /*   joinBtn = (Button) itemView.findViewById(R.id.joinbtn);
            joinBtn.setOnClickListener(new View.OnClickListener() {
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
            }); */
        }
 
        @Override
        public void onClick(View v) {



        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<DataObjectPadiPooja> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        holder.label2.setText(mDataset.get(position).getmText2());
        holder.time.setText(mDataset.get(position).getDate());
        holder.location.setText(mDataset.get(position).getLocation());

        Log.i(LOG_TAG, "Adding description :"+mDataset.get(position).getmText2());
        holder.imageView.setImageResource(mDataset.get(position).getImgResource());
        if (mDataset.get(position).getPadiMembers()!=0) {
            holder.count.setText(mDataset.get(position).getPadiMembers() + "are Joined");
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