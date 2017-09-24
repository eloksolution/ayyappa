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
import java.util.List;

import ayyappa.eloksolutions.in.ayyappaap.beans.GroupMembers;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GroupJoinHelper;
import ayyappa.eloksolutions.in.ayyappaap.util.DataObjectGroup;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;

public class MyRecyclerViewGroup extends RecyclerView
        .Adapter<MyRecyclerViewGroup
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObjectGroup> mDataset;
    private Context context;
    private static MyClickListener myClickListener;
    TextView keyName;
    String groupId, userId, firstName, lastName;
    private AmazonS3 s3;
    private List<DataObjectGroup> itemList;
    Glide glide;
    TransferUtility transferUtility;

    public MyRecyclerViewGroup(ArrayList<DataObjectGroup> myDataset, Context context, AmazonS3 s3, TransferUtility transferUtility) {
        mDataset = myDataset;
        this.context = context;
        this.s3 = s3;
        this.transferUtility = transferUtility;
    }
    public MyRecyclerViewGroup(Context context, List<DataObjectGroup> itemList) {
        this.itemList = itemList;
        this.context = context;
    }


    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView label;
        TextView label2,label3;


        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        ImageView imageView;
        Button joinBtn;

        public DataObjectHolder(final View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.group_title);
            label2 = (TextView) itemView.findViewById(R.id.group_desc);
            label3=(TextView) itemView.findViewById(R.id.badge_notification);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);

            imageView = (ImageView) itemView.findViewById(R.id.group_image_list);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "Adding Listener " + label.getText());
                    DataObjectGroup dataObject = mDataset.get(getAdapterPosition());
                    Log.i(LOG_TAG, "data object is Listener" + dataObject);
                    Intent groupView = new Intent(view.getContext(), GroupView.class);
                    groupView.putExtra("groupId", dataObject.getGroupId());
                    view.getContext().startActivity(groupView);
                }
            });

            joinBtn = (Button) itemView.findViewById(R.id.joinbtn);
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataObjectGroup dataObject = mDataset.get(getAdapterPosition());
                    groupId = dataObject.getGroupId();
                    joinEvent(itemView);

                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }



    private void joinEvent(View itemView) {
        GroupJoinHelper groupJoinHelper = new GroupJoinHelper(itemView.getContext());
        GroupMembers groupJoins = memBuildDTOObject();

        String surl = Config.SERVER_URL + "group/join";
        try {
            String joinmem = groupJoinHelper.new JoinGroup(groupJoins, surl).execute().get();
            System.out.println("the output from JoinEvent" + joinmem);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private GroupMembers memBuildDTOObject() {
        GroupMembers groupMembers = new GroupMembers();
        groupMembers.setGroupId(groupId);
        groupMembers.setUserId(Config.getUserId());
        groupMembers.setFirstname(Config.getFirstName());
        Log.i(LOG_TAG, "Config.getUserId()" + groupId);
        groupMembers.setLastName(Config.getLastName());
        return groupMembers;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_list_view, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        holder.label2.setText(mDataset.get(position).getmText2());
        getBitMap(mDataset.get(position).getImgResource(), holder.imageView);
        if (mDataset.get(position).getMemberSize()!=0) {
            holder.label3.setText(mDataset.get(position).getMemberSize() + "  are Joined");
        }
        else {
            holder.label3.setText(  "0 Joined");
        }
    }

    private void getBitMap(String imgResource, ImageView imageView) {
        try {
            File outdirectory = context.getCacheDir();
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
                        // Bitmap bit= ImageUtils.getInstant().getCompressedBitmap(fileToDownload.getAbsolutePath());
                        //imageView.setImageBitmap(bit);
                        glide.with(context).load(fileToDownload.getAbsolutePath()).into(imageView);
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
    public void addItem(DataObjectGroup dataObj, int index) {
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