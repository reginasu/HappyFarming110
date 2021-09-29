package tw.tcnr16.happyfarming;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
public class Q0512 extends AppCompatActivity {
    private LinearLayout li01;
    private TextView mTxtResult;
    private TextView mDesc;
    private RecyclerView recyclerView;
    private TextView t_count;
    private TextView u_loading;
    private SwipeRefreshLayout laySwipe;
    private String ul = "https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAttractions.aspx";
    private ArrayList<Map<String, Object>> mList;
    private int total;
    private int t_total;
    private int nowposition;
    private Button b001,b002;
    private Intent intent = new Intent();
    private Uri uri;
    private Intent it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0512);
        setupViewComponent();
    }
    private void setupViewComponent() {
        li01 = (LinearLayout) findViewById(R.id.li01);
        li01.setVisibility(View.GONE);
        mTxtResult = (TextView) findViewById(R.id.q0512_name);
        mDesc = (TextView) findViewById(R.id.q0512_descr);
        mDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        mDesc.scrollTo(0, 0);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        t_count = (TextView) findViewById(R.id.count);
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        b001=(Button)findViewById(R.id.q0512_b001);
        b002=(Button)findViewById(R.id.q0512_b002);
        b001.setOnClickListener(b001On);
        b002.setOnClickListener(b001On);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                li01.setVisibility(View.GONE);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        u_loading = (TextView) findViewById(R.id.u_loading);
        u_loading.setVisibility(View.GONE);
        laySwipe = (SwipeRefreshLayout) findViewById(R.id.laySwipe);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setSize(SwipeRefreshLayout.LARGE);
        laySwipe.setDistanceToTriggerSync(10000);
        laySwipe.setProgressBackgroundColorSchemeColor(getColor(android.R.color.background_light));
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark);
        laySwipe.setProgressViewOffset(true, 0, 50);
        onSwipeToRefresh.onRefresh();
    }
    private final SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mTxtResult.setText("");
            MyAlertDialog myAltDlg = new MyAlertDialog(Q0512.this);
            myAltDlg.setTitle(getString(R.string.q0512_dialog_title));
            myAltDlg.setMessage(getString(R.string.q0512_dialog_t001) + getString(R.string.q0512_dialog_b001));
            myAltDlg.setCancelable(false);
            myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.q0512_dialog_positive), altDlgOnClkPosiBtnLis);
            myAltDlg.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.q0512_dialog_neutral), altDlgOnClkNeutBtnLis);
            myAltDlg.show();
        }
    };
    private DialogInterface.OnClickListener altDlgOnClkPosiBtnLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            laySwipe.setRefreshing(true);
            u_loading.setVisibility(View.VISIBLE);
            mTxtResult.setText("");
            mDesc.setText("");
            mDesc.scrollTo(0, 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDatatolist();
                    u_loading.setVisibility(View.GONE);
                    laySwipe.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.q0512_loadover), Toast.LENGTH_SHORT).show();
                }
            }, 1000);
        }
    };

    private DialogInterface.OnClickListener altDlgOnClkNeutBtnLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            u_loading.setVisibility(View.GONE);
            laySwipe.setRefreshing(false);
        }
    };
    private void setDatatolist() {
        u_importopendata();
        final ArrayList<Q0512_Post> mData = new ArrayList<>();
        for (Map<String, Object> m : mList) {
            if (m != null) {
                String Name = m.get("Name").toString().trim();
                String Coordinate = m.get("Coordinate").toString().trim();
                String Tel = m.get("Tel").toString().trim();
                String Address = m.get("Address").toString().trim();
                String Photo = m.get("Photo").toString().trim();
                if (Photo.isEmpty() || Photo.length() < 1) {
                    Photo = "https://tcnr2021a13.000webhostapp.com/post_img/nopic1.jpg";
                }
                String Introduction = m.get("Introduction").toString().trim();
                mData.add(new Q0512_Post(Name, Coordinate, Tel, Photo, Address, Introduction));
            } else {
                return;
            }
        }
        Q0512_RecyclerAdapter adapter = new Q0512_RecyclerAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new Q0512_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                li01.setVisibility(View.VISIBLE);

                mTxtResult.setText(mData.get(position).Name);
                mDesc.setText(mData.get(position).Introduction);
                mDesc.scrollTo(0, 0); //textview 回頂端
                nowposition = position;
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void u_importopendata() {
        try {
            String Task_opendata = new TransTask().execute(ul).get();
            mList = new ArrayList<Map<String, Object>>();
            JSONArray m_JSONArry = new JSONArray(Task_opendata);
            total = 0;
            t_total = m_JSONArry.length(); //總筆數
            m_JSONArry = sortJsonArray(m_JSONArry);
            total = m_JSONArry.length(); //有效筆數
            t_count.setText(getString(R.string.q0512_ncount) + total + "/" + t_total);
            total = m_JSONArry.length();
            t_count.setText(getString(R.string.q0512_ncount) + total);
            for (int i = 0; i < m_JSONArry.length(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                JSONObject jsonData = m_JSONArry.getJSONObject(i);
                String Name = m_JSONArry.getJSONObject(i).getString("Name");
                String Introduction = m_JSONArry.getJSONObject(i).getString("Introduction");
                String Address = m_JSONArry.getJSONObject(i).getString("Address");
                String Coordinate = m_JSONArry.getJSONObject(i).getString("Coordinate");
                String Tel = m_JSONArry.getJSONObject(i).getString("Tel");
                String Photo = m_JSONArry.getJSONObject(i).getString("Photo");
                item.put("Name", Name);
                item.put("Coordinate", Coordinate);
                item.put("Tel", Tel);
                item.put("Introduction", Introduction);
                item.put("Address", Address);
                item.put("Photo", Photo);
                mList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
 public static JSONArray sortJsonArray(JSONArray array) {
        ArrayList<JSONObject> jsons = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject t1, JSONObject t2) {
                String lid = "";
                String rid = "";
                try {
                    lid = t1.getString("Address");
                    rid = t2.getString("Address");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }
    @Override
    public void onBackPressed() { }
    private class TransTask extends AsyncTask<String, Void, String> {
        String ans;
        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while (line != null) {
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ans = sb.toString();
            //------------
            return ans;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseJson(s);
        }
        private void parseJson(String s) {
        }
    }
    private View.OnClickListener b001On=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            u_importopendata();
            final ArrayList<Q0512_Post> mData = new ArrayList<>();
            for (Map<String, Object> m : mList) {
                if (m != null) {
                    String Name = m.get("Name").toString().trim();
                    String Coordinate = m.get("Coordinate").toString().trim();
                    String Tel = m.get("Tel").toString().trim();
                    String Address = m.get("Address").toString().trim();
                    String Photo = m.get("Photo").toString().trim();
                    if (Photo.isEmpty() || Photo.length() < 1) {
                        Photo = "https://tcnr2021a13.000webhostapp.com/post_img/nopic1.jpg";
                    }
                    String Introduction = m.get("Introduction").toString().trim();
                    mData.add(new Q0512_Post(Name, Coordinate, Tel, Photo, Address, Introduction));
                } else {
                    return;
                }
            }
            switch (v.getId()){
                case R.id.q0512_b001:
                    String btn_C=mData.get(nowposition).Address;
                    it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                    break;

                case R.id.q0512_b002:
                    String btntel=mData.get(nowposition).Tel;
                    uri = Uri.parse("tel:"+btntel);
                    startActivity(it);
                    break;
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0512_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        li01.setVisibility(View.GONE);
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                break;
            case R.id.menu_top:
                nowposition = 0;
                recyclerView.scrollToPosition(nowposition);
                break;
            case R.id.menu_next:
                nowposition = nowposition + 100;
                if (nowposition > total - 1) {
                    nowposition = total - 1;
                }
                recyclerView.scrollToPosition(nowposition);
                break;
            case R.id.menu_back:
                nowposition = nowposition - 100;
                if (nowposition < 0) {
                    nowposition = 0;
                }
                recyclerView.scrollToPosition(nowposition);
                break;
            case R.id.menu_end:
                nowposition = total - 1;
                recyclerView.scrollToPosition(nowposition);
                break;
            case R.id.menu_load:
                onSwipeToRefresh.onRefresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}