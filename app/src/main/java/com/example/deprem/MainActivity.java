package com.example.deprem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Proxy;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager yg_sensorManager;
    Sensor yg_ivme;
    float yg_ivmelenme;
    float yg_simdikiivmelenme;
    float yg_bironcekiivmelenme;
    TextView yg_txt;
    float yg_last_x,yg_last_y,yg_last_z;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yg_sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        yg_ivme=yg_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        yg_sensorManager.registerListener(this,yg_ivme,SensorManager.SENSOR_DELAY_NORMAL);
        yg_txt=findViewById(R.id.yg_textVİew);
        TextView yg_textView=findViewById(R.id.yg_textVİew);
        yg_textView.setTextSize(24);
        yg_textView.setTypeface(null, Typeface.BOLD);
        yg_textView.setTypeface(null, Typeface.BOLD_ITALIC);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        String yg_text = yg_textView.getText().toString();

        int index = yg_text.indexOf("!");

        if (index >= 0) {


            SpannableString spannableString = new SpannableString(yg_text);
            spannableString.setSpan(new RelativeSizeSpan(2.5f), index, index+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            yg_textView.setText(spannableString);
        }
        yg_ivmelenme=0.00f;
        yg_simdikiivmelenme=SensorManager.GRAVITY_EARTH;
        yg_bironcekiivmelenme=SensorManager.GRAVITY_EARTH;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x=sensorEvent.values[0];
        float y=sensorEvent.values[1];
        float z=sensorEvent.values[2];
        yg_bironcekiivmelenme=yg_simdikiivmelenme;
        yg_simdikiivmelenme=(float)Math.sqrt(x*x+y*y+z*z);

        float yg_değişim=yg_simdikiivmelenme-yg_bironcekiivmelenme;
        yg_ivmelenme=yg_ivmelenme*0.9f+yg_değişim;
        if (yg_ivmelenme>1) {
            yg_txt.setBackgroundColor(Color.RED);
            Vibrator yg_vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT>=26){
                yg_vibrator.vibrate(VibrationEffect.createOneShot(2000,VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else {
                yg_vibrator.vibrate(2000);
        }
            yg_txt.setTextColor(Color.GREEN);
            Toast.makeText(this, "ACİL DEPREM ÇAĞRISI", Toast.LENGTH_SHORT).show();
            Uri yg_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), yg_sound);
            r.play();
            MediaPlayer yg_mediaplayer = MediaPlayer.create(this, yg_sound);
            yg_mediaplayer.setLooping(true);
            yg_mediaplayer.start();
            CountDownTimer yg_timer = new CountDownTimer(Long.MAX_VALUE, 1000)
            {

                boolean yg_visible = true;

                @Override
             public void onTick(long millisUntilFinished) {
                   yg_txt.setVisibility(yg_visible ? View.VISIBLE : View.INVISIBLE);
                    yg_visible = !yg_visible;
                }

                @Override
                public void onFinish() {
                }
            };

           yg_timer.start();

        }

}
        @Override
        protected void onDestroy(){
        super.onDestroy();
        yg_sensorManager.unregisterListener(this);
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}