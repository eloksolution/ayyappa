package in.eloksolutions.ayyappaapp.recycleviews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.GroupView;
import in.eloksolutions.ayyappaapp.beans.GroupMembers;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GroupJoinHelper;
import in.eloksolutions.ayyappaapp.util.DataObjectGroup;

public class MyRecyclerViewTopicGroup extends RecyclerView
        .Adapter<MyRecyclerViewTopicGroup
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewTopicGroup";
    private ArrayList<DataObjectGroup> mDataset;
    private Context context;
    private static MyClickListener myClickListener;
    TextView keyName;
    String groupId, userId, firstName, lastName;
   static String joinStaus="Y";
    Glide glide;
    Button joinBtn;
    public MyRecyclerViewTopicGroup(ArrayList<DataObjectGroup> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
        SharedPreferences preferences=context.getSharedPreferences(Config.APP_PREFERENCES,Context.MODE_PRIVATE);
        userId=preferences.getString("userId",null);
        firstName=preferences.getString("firstName",null);
        lastName=preferences.getString("lastName",null);
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
        public DataObjectHolder(final View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.title_1);
            label2 = (TextView) itemView.findViewById(R.id.title_area);
            label3=(TextView) itemView.findViewById(R.id.title_people);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);

            imageView = (ImageView) itemView.findViewById(R.id.activity_image);

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


            joinBtn = (Button) itemView.findViewById(R.id.send_request);
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataObjectGroup dataObject = mDataset.get(getAdapterPosition());
                    groupId = dataObject.getGroupId();
                    joinEvent(itemView);
                    joinBtn.setVisibility(View.GONE);

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


    private GroupMembers memBuildDTOObject() {
        GroupMembers groupMembers = new GroupMembers();
        groupMembers.setGroupId(groupId);
        groupMembers.setUserId(userId);
        groupMembers.setFirstname(firstName+lastName);
        Log.i(LOG_TAG, "Config.getUserId()" + groupId);
        groupMembers.setLastName(Config.getLastName());
        return groupMembers;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    public MyRecyclerViewTopicGroup(ArrayList<DataObjectGroup> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groups_lists_home, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        holder.label2.setText(mDataset.get(position).getmText2());
        try {
            Log.i(LOG_TAG," data set obj "+mDataset.get(position)+" position "+position+" userId "+userId+ " joinStaus "+joinStaus+"mDataset.get(position).getIsMember())"+mDataset.get(position).getIsMember());
            if(userId.equals((mDataset.get(position).getOwner())) || joinStaus.equals(mDataset.get(position).getIsMember())){
                System.out.println("mDataset.get(position)"+mDataset.get(position).getIsMember());
                joinBtn.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}