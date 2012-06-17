package com.trevore.android.main;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WorkTimeActivity extends Activity 
{
	//Member variables
	private AudioManager mAudioManager;
	private WifiManager mWifi;
	private BluetoothAdapter mBtAdapter;
	private TextView mWifiConnPercent, mSignalPercent;
	private TelephonyManager mTelManager;
	
	//Receiver for information on the battery level
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			int batValue = intent.getIntExtra("level", 0);
			TextView batLevel = (TextView)findViewById(R.id.batLevel);
			batLevel.setText("Battery Level: " + Integer.toString(batValue) + "%");
		}
	};
	
	//Receiver for information on the network info
	private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			List<ScanResult> scanResult = mWifi.getScanResults();
			for(ScanResult scan : scanResult)
			{
				mWifiConnPercent.setText("Network strength: " + String.valueOf(scan.level) + " dBm");
			}
		}	
	};
	
	//Listener for the signal strength
	private PhoneStateListener mPhoneState = new PhoneStateListener()
	{
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength)
		{
			DecimalFormat df = new DecimalFormat("#.##");
			double value = signalStrength.getGsmSignalStrength() / 31.0;
			if(value >= 0 && value <= 31) //Only valid values
			{
				mSignalPercent.setText("Signal strength: " + String.valueOf(df.format(value)));				
			}
			else //Value of 99 means that the value is unknown or not detectable
			{
				mSignalPercent.setText("Signal strength: UNKNOWN");		
			}
		}
	};

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
		mWifiConnPercent = (TextView) findViewById(R.id.wifiInfo);
		mSignalPercent = (TextView) findViewById(R.id.signalInfo);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mTelManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		//Max all Android volumes
		maxVolume.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF), 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_RING, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
				Toast.makeText(getApplicationContext(), "Volume Maxed!", Toast.LENGTH_SHORT).show();
			}
		});

		//Mute all Android volumes except for call volume
		muteVolume.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, 0, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0); //Still want call volume to be loud
				Toast.makeText(getApplicationContext(), "Volume Muted!", Toast.LENGTH_SHORT).show();
			}
		});

		//Turn WiFi on
		wifiOn.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				mWifi.setWifiEnabled(true);
				Toast.makeText(getApplicationContext(), "WiFi Enabled!", Toast.LENGTH_SHORT).show();
			}
		});

		//Turn WiFi off
		wifiOff.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				mWifi.setWifiEnabled(false);
				Toast.makeText(getApplicationContext(), "WiFi Disabled!", Toast.LENGTH_SHORT).show();
			}
		});

		//Turn Bluetooth on
		btOn.setOnClickListener(new View.OnClickListener() 
		{

			public void onClick(View v) 
			{
				if(mBtAdapter != null)
				{
					mBtAdapter.enable();
					Toast.makeText(getApplicationContext(), "Bluetooth Enabled!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		//Turn Bluetooth off
		btOff.setOnClickListener(new View.OnClickListener() 
		{

			public void onClick(View v) 
			{
				if(mBtAdapter != null)
				{
					mBtAdapter.disable();
					Toast.makeText(getApplicationContext(), "Bluetooth Disabled!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		mWifi.startScan(); //Start the scan for the network info
		
		//Register the receivers
		registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)); //battery level
		registerReceiver(mNetworkReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); //network info
		mTelManager.listen(mPhoneState, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		//Unregister the services when the app closes
		unregisterReceiver(mBatInfoReceiver);
		unregisterReceiver(mNetworkReceiver);
		mTelManager.listen(mPhoneState, PhoneStateListener.LISTEN_NONE);
	}
}