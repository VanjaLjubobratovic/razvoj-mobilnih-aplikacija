package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private RadioGroup sensorGroup, delayGroup;
    private Button startReading;
    public SensorManager sensorManager;
    public Sensor accelerometer, lightSensor, magnetometer;
    private EditText customDelay;
    private int readings = 0;
    long tStart, elapsedTime;
    TextView results;
    String sensor = "", sampling = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        results = findViewById(R.id.resultsText);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        customDelay = findViewById(R.id.customDelayInput);


        sensorGroup = findViewById(R.id.sensorGroup);
        delayGroup = findViewById(R.id.delayGroup);

        sensorGroup.check(R.id.accelerometerBtn);
        delayGroup.check(R.id.delNormal);

        startReading = findViewById(R.id.startButton);
        startReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int delay = 1000;
                readings = 0;
                elapsedTime = 1;
                sensorManager.unregisterListener(MainActivity.this);

                switch(delayGroup.getCheckedRadioButtonId()) {
                    case R.id.delNormal:
                        delay = SensorManager.SENSOR_DELAY_NORMAL;
                        sampling = "NORMAL";
                        break;
                    case R.id.delFASTEST:
                        delay = SensorManager.SENSOR_DELAY_FASTEST;
                        sampling = "FASTEST";
                        break;
                    case R.id.delGAME:
                        delay = SensorManager.SENSOR_DELAY_GAME;
                        sampling = "GAME";
                        break;
                    case R.id.delUI:
                        delay = SensorManager.SENSOR_DELAY_UI;
                        sampling = "UI";
                        break;
                    case R.id.delCustom:
                        delay = Integer.parseInt(customDelay.getText().toString());
                        sampling = "CUSTOM";
                        break;
                }
                switch (sensorGroup.getCheckedRadioButtonId()) {
                    case R.id.accelerometerBtn:
                        sensorManager.registerListener(MainActivity.this, accelerometer, delay);
                        sensor = "ACCELEROMETER";
                        break;
                    case R.id.magnetometerBtn:
                        sensorManager.registerListener(MainActivity.this, magnetometer, delay);
                        sensor = "MAGNETOMETER";
                        break;
                    case R.id.lightSensorBtn:
                        sensorManager.registerListener(MainActivity.this, lightSensor, delay);
                        sensor = "LIGHT SENSOR";
                        break;
                }
                tStart = System.currentTimeMillis();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        readings++;
        elapsedTime = System.currentTimeMillis() - tStart;
        float freq = (float)readings / (elapsedTime / 1000);
        if(elapsedTime / 1000 == 0)
            freq = 0;
        results.setText(sensor + ", sampling: " + sampling + "\n" + readings + " events in " + elapsedTime + " ms."
                    + "\nReal sampling rate: " + String.format("%.2f", freq) +  " Hz");
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}