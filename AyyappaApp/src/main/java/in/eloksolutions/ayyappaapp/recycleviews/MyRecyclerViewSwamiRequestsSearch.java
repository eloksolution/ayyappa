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
import in.eloksolutions.ayyappaapp.activities.SearchSwamiRequest;
import in.eloksolutions.ayyappaapp.activities.UserView;
import in.eloksolutions.ayyappaapp.beans.GroupMembers;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.SendAcceptTaskSearch;
import in.eloksolutions.ayyappaapp.util.DataObjectRequests;

public class MyRecyclerViewSwamiRequestsSearch extends RecyclerView
        .Adapter<MyRecyclerViewSwamiRequestsSearch
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewTopicGroup";
    private ArrayList<DataObjectRequests> mDataset;
    private SearchSwamiRequest context;
    private static MyClickListener myClickListener;
    TextView keyName;
    String groupId, userId, firstName, lastName;
   static String joinStaus="Y";
    Glide glide;
    ImageView userImage;

    public MyRecyclerViewSwamiRequestsSearch(ArrayList<DataObjectRequests> myDataset, SearchSwamiRequest context) {
        mDataset = myDataset;
        this.context = context;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView label;
        Button sendRequest;

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



        ImageView accept,reject;
        Button joinBtn;

        public DataObjectHolder(final View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.title_1);
            sendRequest=(Button) itemView.findViewById(R.id.send_request);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
            userImage = (ImageView) itemView.findViewById(R.id.activity_image);
            SharedPreferences preferences=context.getSharedPreferences(Config.APP_PREFERENCES,Context.MODE_PRIVATE);
            userId=preferences.getString("userId",null);
            firstName=preferences.getString("firstName",null);
            lastName=preferences.getString("lastName",null);
            sendRequest.setOnClickListener(new View.OnClickListener() {
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
            Intent groupView = new Intent(v.getContext(), UserView.class);
            groupView.putExtra("swamiUserId", dataObject.getUserId());
            v.getContext().startActivity(groupView);

        }
    }


    private String userSendTag(DataObjectRequests dataObject) {

        RegisterDTO userDTo = buildDTOObject(dataObject);


        if (CheckInternet.checkInternetConenction(context)) {
            String gurl = Config.SERVER_URL +"user/connect";
            try {
                new SendAcceptTaskSearch(userDTo, gurl,context).execute();
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
    public MyRecyclerViewSwamiRequestsSearch(ArrayList<DataObjectRequests> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_search, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getFirstName() + " " + mDataset.get(position).getLastName());
        if(mDataset.get(position).getImgPath()!=null) {
            glide.with(context).load(Config.S3_URL + mDataset.get(position).getImgPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(userImage);
            System.out.println("past from eventfromJson.gettime()" + mDataset.get(position).getImgPath());
        }else{
            glide.with(context).load(R.drawable.defaulta).diskCacheStrategy(DiskCacheStrategy.ALL).into(userImage);
        }

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