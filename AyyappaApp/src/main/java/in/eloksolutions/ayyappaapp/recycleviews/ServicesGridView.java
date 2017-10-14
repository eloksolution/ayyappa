package in.eloksolutions.ayyappaapp.recycleviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.eloksolutions.ayyappaapp.R;

public class ServicesGridView extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;


    public ServicesGridView(Context c, String[] web, int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.service_gridview, null);
        }
        else {
            grid = (View) convertView;
        }
            TextView textView = (TextView) grid.findViewById(R.id.services);
            ImageView imageView = (ImageView)grid.findViewById(R.id.Icon);
            textView.setText(web[position]);
            imageView.setImageResource(Imageid[position]);
        return grid;
    }
}