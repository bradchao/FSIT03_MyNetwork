package tw.brad.app.helloworld.mynetwork;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private Bitmap bmp;
    private UIHandler handler;
    private File sdroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }else{
            init();
        }

    }

    private void init(){
        handler = new UIHandler();
        img = (ImageView)findViewById(R.id.img);

        sdroot = Environment.getExternalStorageDirectory();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                init();
            }else{
                finish();
            }
        }
    }

    public void test1(View view){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.iii.org.tw/");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ( (line = br.readLine()) !=null){
                        Log.i("brad", line);
                    }
                    br.close();
                } catch (Exception e) {
                    Log.i("brad", e.toString());
                }

            }
        }.start();
    }
    public void test2(View view){
        new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("http://www.iii.org.tw/assets/images/information-news/image004.jpg");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    bmp = BitmapFactory.decodeStream(conn.getInputStream());
                    handler.sendEmptyMessage(0);
                }catch(Exception e){
                    Log.i("brad", e.toString());
                }
            }
        }.start();
    }

    public void test3(View view){

    }
    public void test4(View view){

    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            img.setImageBitmap(bmp);
        }
    }

}
