package cocad3v.com.challenge;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;



public class MainActivity extends Activity {

    final String LOG_TAG = "dLogs";
    private static final String NEWS_URL = "http://api.innogest.ru/api/v3/amobile/news";

    public static final String TAG_NID = "nid";
    public static final String TAG_CREATE = "create";
    public static final String TAG_TITLE = "title";
    public static final String TAG_IMG_URL = "img_url";
    public static final String TAG_TYPE = "type";
    public static final String TAG_BODY = "body";
    public static final String TAG_URL = "url";
    public static final String TAG_POSITION = "position";
    public static final String TAG_GOODS_ID = "goods_id";
    public static final String TAG_WORDS2 = "words2";

    public static final int MODE_CACHE = 1;
    public static final int MODE_WEB = 0;


    private ServiceHandler sh = new ServiceHandler();

    private ListView lv;
    private ArrayList<HashMap<String, String>> NewsList;
    AsyncTask<String, String, String> task;
    private JSONArray json_news = null;
    NewsAdapter newsAdapter;
    DB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lvObj);
        db = new DB(this);
        db.open();

        Cursor c = db.getAllData();
        if (c.moveToFirst())
        {
            NewsList = new ArrayList<HashMap<String, String>>();
            Log.d(LOG_TAG, "Load from cache");
            int titleColIndex = c.getColumnIndex("title");
            int imgColIndex = c.getColumnIndex("img");
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_TITLE, c.getString(titleColIndex));
                map.put(TAG_IMG_URL, c.getString(imgColIndex));
                NewsList.add(map);
            } while (c.moveToNext());
            newsAdapter = new NewsAdapter(MainActivity.this, NewsList,MODE_CACHE);
            lv.setAdapter(newsAdapter);
        }
        else
        {
            Log.d(LOG_TAG, "Load from web");
            c.close();
            task = new LoadNews().execute();
        }

    }


    public void onClearCache(View v) {
        db.delCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
        }
    }


    class LoadNews extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            NewsList = new ArrayList<HashMap<String, String>>();

            String json = sh.makeServiceCall(NEWS_URL, ServiceHandler.GET);
            if (json != null) {
                try {
                    JSONArray jsonObj = new JSONArray(json);

                    for (int i = 0; i < jsonObj.length(); i++) {

                        JSONObject c = jsonObj.getJSONObject(i);

                        String nid = c.getString(TAG_NID);
                        String create = c.getString(TAG_CREATE);
                        String title = c.getString(TAG_TITLE);
                        String img_url = c.getString(TAG_IMG_URL);
                        String type = c.getString(TAG_TYPE);
                        String body = c.getString(TAG_BODY);
                        String url = c.getString(TAG_URL);
                        String position = c.getString(TAG_POSITION);
                        String goods_id = c.getString(TAG_GOODS_ID);
                        String words2 = c.getString(TAG_WORDS2);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_NID, nid);
                        map.put(TAG_CREATE, create);
                        map.put(TAG_TITLE, title);
                        map.put(TAG_IMG_URL, img_url);
                        map.put(TAG_TYPE, type);
                        map.put(TAG_BODY, body);
                        map.put(TAG_URL, url);
                        map.put(TAG_POSITION, position);
                        map.put(TAG_GOODS_ID, goods_id);
                        map.put(TAG_WORDS2, words2);

                        NewsList.add(map);
                        db.addRec(title, img_url);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "No Data exist");
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            // updating UI from Background Thread

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    newsAdapter = new NewsAdapter(MainActivity.this, NewsList,MODE_WEB);
                    lv.setAdapter(newsAdapter);

                }
            });

        }

    }


}
