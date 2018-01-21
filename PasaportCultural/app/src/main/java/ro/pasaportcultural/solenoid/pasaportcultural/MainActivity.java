package ro.pasaportcultural.solenoid.pasaportcultural;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 1;
    private Button mButton;
    private Button tButton;
    private ImageView mImage;
    private int share=0;
    private Bitmap shareable;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mButton.setVisibility(View.VISIBLE);
                    tButton.setVisibility(View.VISIBLE);
                    mImage.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    mButton.setVisibility(View.INVISIBLE);
                    tButton.setVisibility(View.INVISIBLE);
                    mImage.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    mButton.setVisibility(View.INVISIBLE);
                    tButton.setVisibility(View.INVISIBLE);
                    mImage.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mButton = (Button) findViewById(R.id.button_add);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if(share==1)
                {
                    String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), shareable,"title", null);
                    Uri bitmapUri = Uri.parse(bitmapPath);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    mButton.setText("Alege o poza");
                    share=0;
                }
                else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                    mButton.setText("Trimite poza");
                    share = 1;
                }
            }
        });

        tButton = (Button) findViewById(R.id.button_take);
        tButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (share == 1) {
                    mButton.setText("Alege o poza");
                    share = 0;
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 2);
                        mButton.setText("Alege alta poza");
                        share = 1;
                    }
                }
            }
        });

        mImage = (ImageView) findViewById(R.id.image_view);
    }

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == 1) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                final Bitmap overlay = BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_stampx);

                Bitmap bg = Bitmap.createBitmap(selectedImage.getWidth(), selectedImage.getHeight(), selectedImage.getConfig());
                Canvas canvas = new Canvas(bg);
                canvas.drawBitmap(selectedImage, new Matrix(), null);
                canvas.drawBitmap(overlay, null, new Rect(0,0,500,500), null);

                mImage.setImageBitmap(bg);
                saveImageToExternalStorage(bg);
                shareable=bg;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
            final Bitmap overlay = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.ic_stampx);

            Bitmap bg = Bitmap.createBitmap(selectedImage.getWidth(), selectedImage.getHeight(), selectedImage.getConfig());
            Canvas canvas = new Canvas(bg);
            canvas.drawBitmap(selectedImage, new Matrix(), null);
            canvas.drawBitmap(overlay, null, new Rect(0,0,100,100), null);

            mImage.setImageBitmap(bg);
            saveImageToExternalStorage(bg);
            shareable=bg;
        }
    }

    public boolean saveImageToExternalStorage(Bitmap image) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PasaportCultural";
        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(fullPath, "Image"+ DateFormat.getDateTimeInstance().format(new Date())+".png");
            file.createNewFile();
            fOut = new FileOutputStream(file);

// 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return true;

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }
}
