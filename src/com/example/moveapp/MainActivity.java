package com.example.moveapp;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.media.AsyncPlayer;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener{
	private long last_update = 0, last_movement = 0;
	private float prevX = 0, prevY = 0, prevZ = 0,bef_movement=0;
	private float curX = 0, curY = 0, curZ = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);        
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            long current_time = event.timestamp;
             
            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];
             
            if (prevX == 0 && prevY == 0 && prevZ == 0) {
                last_update = current_time;
                last_movement = current_time;
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
            }
            
     
            long time_difference = current_time - last_update;
            //if (time_difference > 1000000000) {
                //float movement = Math.abs((curX + curY + curZ) - (prevX - prevY - prevZ)) / time_difference;
            if(time_difference>1000000){
            	float movement = (float) Math.abs(Math.sqrt((Math.pow(curX-prevX,2)) + (Math.pow(curY - prevY, 2)) +(Math.pow(curZ - prevZ, 2))));
            	if(movement>bef_movement){
            		bef_movement=movement;
            	}else {
            		movement=bef_movement;
            	}
            	//int limit = 1500;
               // float min_movement = 1E-6f;
               // if (movement > min_movement) {
                    //if (current_time - last_movement >= limit) {                     
                       // Toast.makeText(getApplicationContext(), "Hay movimiento de " + movement, Toast.LENGTH_SHORT).show();
            	if (time_difference > 1000000000) {
                    	((TextView) findViewById(R.id.textx)).setText(((Float)movement).toString().substring(0, 4));
                    	if(movement<=10){
                    		((TextView) findViewById(R.id.textx)).setTextColor(getResources().getColor(R.color.green));
                    	}
                    	if(movement>10 && movement<=20){
                    		((TextView) findViewById(R.id.textx)).setTextColor(getResources().getColor(R.color.yellow));
                    	}
                    	if(movement>20){
                    		((TextView) findViewById(R.id.textx)).setTextColor(getResources().getColor(R.color.red));
                    	}
                    //}
                    last_movement = current_time;
               // }
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
                last_update = current_time;
                bef_movement=0;
             }
            	if(movement >10){
            		//call();
            	}
            }
             
             
            
            //((TextView) findViewById(R.id.texty)).setText("Aceler—metro Y: " + curY);
            //((TextView) findViewById(R.id.textz)).setText("Aceler—metro Z: " + curZ);
        }   
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);        
        if (sensors.size() > 0) {
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }
    
    @Override
    protected void onStop() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);        
        sm.unregisterListener(this);
        super.onStop();
    }
    
    private void call(){
    	try{
    		Intent callIntent= new Intent(Intent.ACTION_CALL);
    		callIntent.setData(Uri.parse("tel:638764292"));
    		
    		startActivity(callIntent);
    	}catch(ActivityNotFoundException activityNotFound){
    		Log.e("...","fail");
    	}
    }

}
