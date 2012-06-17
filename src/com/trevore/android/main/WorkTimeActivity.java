package com.trevore.android.main;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WorkTimeActivity extends Activity 
{
	//Member variables
	AudioManager audioManager;
	WifiManager wifi;
	BluetoothAdapter btAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button maxVolume = (Button) findViewById(R.id.maxVolume);
		Button muteVolume = (Button) findViewById(R.id.muteVolume);
		Button wifiOn = (Button) findViewById(R.id.wifiOn);
		Button wifiOff = (Button) findViewById(R.id.wifiOff);
		Button btOn = (Button) findViewById(R.id.btOn);
		Button btOff = (Button) findViewById(R.id.btOff);
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		btAdapter = BluetoothAdapter.getDefaultAdapter();

		maxVolume.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
				audioManager.setStreamVolume(AudioManager.STREAM_DTMF, audioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF), 0);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
				audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
				audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
				audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
				audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
				Toast.makeText(getApplicationContext(), "Volume Maxed!", Toast.LENGTH_SHORT).show();
			}
		});

		muteVolume.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
				audioManager.setStreamVolume(AudioManager.STREAM_DTMF, 0, 0);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
				audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
				audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
				audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
				audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0); //Still want call volume to be loud
				Toast.makeText(getApplicationContext(), "Volume Muted!", Toast.LENGTH_SHORT).show();
			}
		});

		wifiOn.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				wifi.setWifiEnabled(true);
				Toast.makeText(getApplicationContext(), "WiFi Enabled!", Toast.LENGTH_SHORT).show();
			}
		});

		wifiOff.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				wifi.setWifiEnabled(false);
				Toast.makeText(getApplicationContext(), "WiFi Disabled!", Toast.LENGTH_SHORT).show();
			}
		});

		btOn.setOnClickListener(new View.OnClickListener() 
		{

			public void onClick(View v) 
			{
				if(btAdapter != null)
				{
					btAdapter.enable();
					Toast.makeText(getApplicationContext(), "Bluetooth Enabled!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btOff.setOnClickListener(new View.OnClickListener() 
		{

			public void onClick(View v) 
			{
				if(btAdapter != null)
				{
					btAdapter.disable();
					Toast.makeText(getApplicationContext(), "Bluetooth Disabled!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}