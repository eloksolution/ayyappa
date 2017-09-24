package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class Movis extends AppCompatActivity {
    GridView grid;
    ImageView add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submenu);
        add = (ImageView) findViewById(R.id.add);

        final String[] services = new String[] {"Ayyappa janmarahasyam", "Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan", "Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith", "Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas", "Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji", "Ayyappa Swamy Janma Rahasyam Telugu Full Movie"};
        int [] Images={
                R.drawable.ayy1,
                R.drawable.ayy2,
                R.drawable.ayy3,
                R.drawable.ayy4,
                R.drawable.ayy5,
                R.drawable.ayy

        };
        ServicesGridView adapter = new ServicesGridView(Movis.this, services, Images);
        grid=(GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +array[+ position], Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        String uri = "https://www.youtube.com/watch?v=vxpEMuM1eBc";
                        Intent intent = new Intent(Movis.this,WebActivity.class);
                        intent.putExtra("uri",uri);
                        startActivity(intent);
                        break;
                    case 1:
                        String uri1 = "https://www.youtube.com/watch?v=hRtuGEQmm1E";
                        Intent intent1 = new Intent(Movis.this,WebActivity.class);
                        intent1.putExtra("uri",uri1);
                        startActivity(intent1);
                        break;
                    case 2:
                        String uri2 = "https://www.youtube.com/watch?v=4wjuDG7WXY8";
                        Intent intent2 = new Intent(Movis.this,WebActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        String uri3 = "https://www.youtube.com/watch?v=FTBLd2zz8IU";
                        Intent intent3 = new Intent(Movis.this,WebActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        String uri4 = "https://www.youtube.com/watch?v=o4vv3PN45Eo";
                        Intent intent4 = new Intent(Movis.this,WebActivity.class);
                        intent4.putExtra("uri",uri4);
                        startActivity(intent4);
                        break;
                    case 5:
                        String uri5 = "https://www.youtube.com/watch?v=TfT8w5v8KSY";
                        Intent intent5 = new Intent(Movis.this,WebActivity.class);
                        intent5.putExtra("uri",uri5);
                        startActivity(intent5);
                        break;
                    default:
                        break;

                }
            }

        });

    }


}
