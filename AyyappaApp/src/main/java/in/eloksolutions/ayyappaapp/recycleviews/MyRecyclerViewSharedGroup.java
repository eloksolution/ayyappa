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

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.CreateTopic;
import in.eloksolutions.ayyappaapp.activities.GroupView;
import in.eloksolutions.ayyappaapp.beans.GroupMembers;
import in.eloksolutions.ayyappaapp.beans.TopicDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GroupJoinHelper;
import in.eloksolutions.ayyappaapp.helper.TopicHelper;
import in.eloksolutions.ayyappaapp.util.DataObjectGroup;

public class MyRecyclerViewSharedGroup extends RecyclerView
        .Adapter<MyRecyclerViewSharedGroup
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObjectGroup> mDataset;
    private Context context;
    private static MyClickListener myClickListener;
    TextView keyName;
    String groupId, userId, firstName, lastName;
    private AmazonS3 s3;
    Glide glide;
    TransferUtility transferUtility;
    public CreateTopic createTopic;

    public MyRecyclerViewSharedGroup(ArrayList<DataObjectGroup> myDataset, Context context, AmazonS3 s3, TransferUtility transferUtility, CreateTopic createTopic) {
        mDataset = myDataset;
        this.context = context;
        this.s3 = s3;
        this.transferUtility = transferUtility;
        this.createTopic=createTopic;
    }
    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView label;
        TextView label2, label3;


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
            label3 = (TextView) itemView.findViewById(R.id.badge_notification);
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

                    saveEventToServer(itemView);

                }
            });
        }

        @Override
        public void onClick(View v) {

            Log.i(LOG_TAG, "Adding Listener " + label.getText());
            DataObjectGroup dataObject = mDataset.get(getAdapterPosition());
            Log.i(LOG_TAG, "data object is Listener" + dataObject);
            Intent groupView = new Intent(v.getContext(), GroupView.class);
            groupView.putExtra("groupId", dataObject.getGroupId());
            v.getContext().startActivity(groupView);
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
    private String saveEventToServer(View itemView) {

        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(itemView.getContext())) {
                TopicHelper createtopicpHelper = new TopicHelper(itemView.getContext());
                String gurl = Config.SERVER_URL +"topic/add";
                try {
                    TopicDTO topicDTO=createTopic.getTopicDTO();
                    topicDTO.setGroupId(groupId);
                    System.out.println("topicDTO "+topicDTO);
                    String gId= createtopicpHelper.new CreateTopic(topicDTO, gurl).execute().get();
                    return gId;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(itemView.getContext(), "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
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
                .inflate(R.layout.group_share_view, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        holder.label2.setText(mDataset.get(position).getmText2());
        if(mDataset.get(position).getImgResource()!=null) {
            glide.with(context).load(Config.S3_URL + mDataset.get(position).getImgResource()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        } else{
            holder.imageView.setImageResource(R.drawable.defaulta);}

        if (mDataset.get(position).getMemberSize()!=0) {
            holder.label3.setText(mDataset.get(position).getMemberSize() + " Joined");
        }
        else {
            holder.label3.setText(  "0 Joined");
        }
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
    private boolean checkValidation() {
        boolean ret = true;
        return ret;
    }
}