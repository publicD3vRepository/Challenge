package cocad3v.com.challenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by CocaCake on 24.11.2014.
 */


class ImageLoader extends AsyncTask<Object, Void, Bitmap> {
    ImageView ivPreview = null;

    @Override
    protected Bitmap doInBackground(Object... params) {
        this.ivPreview = (ImageView) params[0];
        String url = (String) params[1];
        return loadBitmap(url);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        ivPreview.setImageBitmap(result);
    }


    public Bitmap loadBitmap(String url) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL(url);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return bitmap;
    }


    class FileCache {

        private File cacheDir;

        public FileCache(Context context) {
            // Find the dir to save cached images
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED))
                cacheDir = new File(
                        android.os.Environment.getExternalStorageDirectory(),
                        "innogest2");
            else
                cacheDir = context.getCacheDir();
            if (!cacheDir.exists())
                cacheDir.mkdirs();
        }

        public File getFile(String url) {
            // I identify images by hashcode. Not a perfect solution, good for the
            // demo.
            String filename = String.valueOf(url.hashCode());
            // Another possible solution (thanks to grantland)
            // String filename = URLEncoder.encode(url);
            File f = new File(cacheDir, filename);
            return f;

        }

        public void clear() {
            File[] files = cacheDir.listFiles();
            if (files == null)
                return;
            for (File f : files)
                f.delete();
        }

    }


}