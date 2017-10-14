package in.eloksolutions.ayyappaapp.recycleviews;

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

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.GroupView;
import in.eloksolutions.ayyappaapp.beans.GroupMembers;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GroupJoinHelper;
import in.eloksolutions.ayyappaapp.util.DataObjectGroup;

public class MyRecyclerViewGroupSdk extends RecyclerView
        .Adapter<MyRecyclerViewGroupSdk
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObjectGroup> mDataset;
    private static MyClickListener myClickListener;
    TextView keyName;

    public  class DataObjectHolder extends RecyclerView.ViewHolder
            implements View

            .OnClickListener {
        TextView label;
       TextView label2;
        ImageView imageView;
        Button joinBtn;

        public DataObjectHolder(final View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.group_title);
           label2 = (TextView) itemView.findViewById(R.id.group_desc);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);

            imageView = (ImageView) itemView.findViewById(R.id.group_image_list);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "Adding Listener "+label.getText());
                    DataObjectGroup dataObject=mDataset.get(getAdapterPosition());
                    Log.i(LOG_TAG, "data object is Listener"+dataObject);
                    Intent groupView=new Intent(view.getContext(), GroupView.class);
                    groupView.putExtra("groupId",dataObject.getGroupId());
                    view.getContext().startActivity(groupView);
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
           String joinmem=groupJoinHelper.new JoinGroup(groupJoins,surl).execute().get();
            System.out.println("the output from JoinEvent"+joinmem);

        }catch (Exception e){}
    }
    private GroupMembers memBuildDTOObject() {
        GroupMembers groupMembers = new GroupMembers();
        groupMembers.setGroupId("");
        groupMembers.setUserId(Config.getUserId());
        groupMembers.setFirstname(Config.getFirstName());
        groupMembers.setLastName(Config.getLastName());
        return groupMembers;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewGroupSdk(ArrayList<DataObjectGroup> myDataset) {
        mDataset = myDataset;
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