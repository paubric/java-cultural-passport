package ro.pasaportcultural.solenoid.pasaportcultural;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pablo on 2/1/2018.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<File> gridItems = new ArrayList<File>();

    public ImageAdapter(Context c) {
        mContext = c;
        File[] files = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PasaportCultural").listFiles();
        for (File file : files)
            gridItems.add(file);
        Collections.reverse(gridItems);
    }

    public int getCount() {
        return gridItems.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, parent, false);

        } else {
            view = convertView;
        }
        ImageView imageView = view.findViewById(R.id.image);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(1000, 1000));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(8, 8, 8, 8);

        Bitmap image = BitmapFactory.decodeFile(gridItems.get(position).getAbsolutePath());
        imageView.setImageBitmap(image);

        TextView description = view.findViewById(R.id.text);


        ExifInterface exif = null;
        try {
            exif = new ExifInterface(gridItems.get(position).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        description.setText(exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION));

        return view;
    }

    /*
    // references to our images
    private Integer[] mThumbIds = {
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
            R.mipmap.ic_pc, R.mipmap.ic_stampx,
    };*/
}