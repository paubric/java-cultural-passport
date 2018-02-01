package ro.pasaportcultural.solenoid.pasaportcultural;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryActivity extends AppCompatActivity {

    private ImageView homeImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(GalleryActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        homeImage = (ImageView) findViewById(R.id.image_home);
        homeImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(GalleryActivity.this, MainActivity.class);
                finish();
            }
        });
    }
}
