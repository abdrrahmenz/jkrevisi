package id.or.qodr.jadwalkajian.data;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import id.or.qodr.jadwalkajian.R;

/**
 * Created by adul on 04/08/17.
 */

public class GlideLoad {
    //DOWNLOAD AND CACHE IMG
    public static void loadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Glide.with(c).load(url).placeholder(R.drawable.load).into(img);
        }else {
            Glide.with(c).load(R.drawable.load).into(img);
        }
    }
}
