package tw.tcnr16.happyfarming;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ajts.androidmads.fontutils.FontUtils;
public class Q0101 extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer startmusic;
    private Intent intent = new Intent();
    private Typeface typeface;
    private FontUtils fontUtils;
    private TextView t001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0101);
        setupViewComponent();
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    private void setupViewComponent() {
        t001 = (TextView)findViewById(R.id.q0101_t001);
        t001.setOnClickListener(this);
        startmusic = MediaPlayer.create(Q0101.this, R.raw.country);
        startmusic.start();
        TextView t001 = (TextView)findViewById(R.id.q0101_t001);
        typeface = Typeface.createFromAsset(getAssets(), "jf-openhuninn-1.1.ttf");
        fontUtils = new FontUtils();
        fontUtils.applyFontToView(t001, typeface);
        //動態調整layout
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        t001.getLayoutParams().height=displayMetrics.heightPixels /40*5;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.q0101_t001:
                if (startmusic.isPlaying()) { startmusic.stop(); }
                intent.putExtra("class_title", getString(R.string.q0100_b001));
                intent.setClass(Q0101.this, Q0100.class);
                startActivity(intent);
                break;
         }
    };
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
    }
}