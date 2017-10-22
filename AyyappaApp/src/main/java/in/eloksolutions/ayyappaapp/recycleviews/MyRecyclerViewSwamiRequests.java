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

import java.util.ArrayList;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.GroupView;
import in.eloksolutions.ayyappaapp.activities.SwamiRequest;
import in.eloksolutions.ayyappaapp.beans.GroupMembers;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.SendAcceptTask;
import in.eloksolutions.ayyappaapp.util.DataObjectRequests;

public class MyRecyclerViewSwamiRequests extends RecyclerView
        .Adapter<MyRecyclerViewSwamiRequests
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewTopicGroup";
    private ArrayList<DataObjectRequests> mDataset;
    private SwamiRequest context;
    private static MyClickListener myClickListener;
    TextView keyName;
    String groupId, userId, firstName, lastName;
   static String joinStaus="Y";
    Glide glide;
    ImageView userImage;

    public MyRecyclerViewSwamiRequests(ArrayList<DataObjectRequests> myDataset, SwamiRequest context) {
        mDataset = myDataset;
        this.context = context;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView label;

        public ImageView getAccept() {
            return accept;
        }

        public void setAccept(ImageView accept) {
            this.accept = accept;
        }

        public ImageView getReject() {
            return reject;
        }

        public void setReject(ImageView reject) {
            this.reject = reject;
        }

        public ImageView getUserImage() {
            return userImage;
        }

        public void setUserImage(ImageView userImage) {
            this.userImage = userImage;
        }

        ImageView accept,reject,userImage;
        Button joinBtn;

        public DataObjectHolder(final View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.user_name);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);

            accept = (ImageView) itemView.findViewById(R.id.accept);
            reject = (ImageView) itemView.findViewById(R.id.reject);
            userImage = (ImageView) itemView.findViewById(R.id.user_profile);
            SharedPreferences preferences=context.getSharedPreferences(Config.APP_PREFERENCES,Context.MODE_PRIVATE);
            userId=preferences.getString("userId",null);
            firstName=preferences.getString("firstName",null);
            lastName=preferences.getString("lastName",null);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "Adding Listener " + label.getText());
                    DataObjectRequests dataObject = mDataset.get(getAdapterPosition());
                    userSendTag(dataObject);

                    Log.i(LOG_TAG, "data object is Listener" + dataObject);

                }
            });
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "Adding Listener " + label.getText());
            DataObjectRequests dataObject = mDataset.get(getAdapterPosition());
            Log.i(LOG_TAG, "data object is Listener" + dataObject);
            Intent groupView = new Intent(v.getContext(), GroupView.class);
            groupView.putExtra("groupId", dataObject.getUserId());
            v.getContext().startActivity(groupView);

        }
    }


    private String userSendTag(DataObjectRequests dataObject) {

        RegisterDTO userDTo = buildDTOObject(dataObject);


            if (CheckInternet.checkInternetConenction(context)) {
                String gurl = Config.SERVER_URL +"user/connect";
                try {
                   new SendAcceptTask(userDTo, gurl,context).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(context, "No Internet Connection",
                        "You don't have internet connection.");
            }

        return null;
    }
    private RegisterDTO buildDTOObject(DataObjectRequests dataObject) {
        RegisterDTO newRegisterDTO= new RegisterDTO();
        newRegisterDTO.setToUserId(dataObject.getUserId());
        newRegisterDTO.setToFirstName(dataObject.getFirstName());
        newRegisterDTO.setToLastName(dataObject.getLastName());
        newRegisterDTO.setUserId(userId);
        newRegisterDTO.setFirstName(firstName);
        newRegisterDTO.setLastName(lastName);
        return newRegisterDTO;
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
    public MyRecyclerViewSwamiRequests(ArrayList<DataObjectRequests> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_request, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getFirstName() + " " + mDataset.get(position).getLastName());
        holder.accept.setImageResource(mDataset.get(position).getYes());
        holder.reject.setImageResource(mDataset.get(position).getNo());

    }

        public void addItem(DataObjectRequests dataObj, int index) {
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