package cocad3v.com.challenge;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CocaCake on 24.11.2014.
 */
public class NewsAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;
    public ImageFileLoader imageFileLoader;

    public NewsAdapter(Activity a, ArrayList<HashMap<String, String>> d,int mode) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageFileLoader = new ImageFileLoader(activity.getApplicationContext(),mode);

    }

    public int getCount() {
        return data.size();

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        holder = null;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();

            holder.title = (TextView) vi.findViewById(R.id.tvTitle);
            holder.img = (ImageView) vi.findViewById(R.id.ivImg);

            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        HashMap<String, String> h = new HashMap<String, String>();
        h = data.get(position);

        holder.title.setText(h.get(MainActivity.TAG_TITLE));
      //  new ImageLoader().execute(holder.img, h.get(MainActivity.TAG_IMG_URL));
       imageFileLoader.DisplayImage(h.get(MainActivity.TAG_IMG_URL), holder.img);
      //  Bitmap bm = imageFileLoader.getBitmap(h.get(MainActivity.TAG_IMG_URL));
       // holder.img.setImageBitmap(bm);
        return vi;
    }

    private static class ViewHolder {
        TextView title;
        ImageView img;

    }
}
