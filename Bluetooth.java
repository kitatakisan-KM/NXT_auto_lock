package a.bluetooth;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.UUID;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class Bluetooth extends Activity {

	static TextView textView,textView2;
	static int bool;
	static int ii;
	static int key;
	static int auto;
	static int area;
	static int state;
	static int con,zz,xx = 0;

	//NXT1 のMAC アドレスを適宜変更
	public final static String address = "00:16:53:16:E7:6F";
	public static BluetoothAdapter mBtAdapter = null;
	public static final UUID
	SERIAL_PORT_SERVICE_CLASS_UUID=UUID.fromString
	("00001101-0000-1000-8000-00805F9B34FB");
	public static OutputStream nxtOutputStream = null;
	public static InputStream input;
	public static boolean connected = false;
	public static BluetoothSocket nxtSocket = null;
	public static BluetoothDevice nxtDevice = null;
	public static boolean btEnable = false;
	CheckBox check1,check2;
	static int z;
	static int y;
	int i;
	static int[] x = new int[700];
	static short[] bb = new short[700];
	private static short[] mByte;
	static AudioRecord recorder;
	private static Draw mVisualizerView2;
	static boolean recording = false;
	static Handler mHandler = new Handler();
	static int numberOfShort;
	static int bufferSize = AudioRecord.getMinBufferSize(
			8000,/*サンプリング周波数*/
			AudioFormat.CHANNEL_IN_MONO,
			AudioFormat.ENCODING_PCM_16BIT);

	//配列確保
	static short[] audioData = new short[bufferSize];
	static short[] mm = new short[bufferSize];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		mVisualizerView2 = (Draw) findViewById(R.id.view2);
		textView = (TextView)findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textview2);
		Button btclose = (Button)findViewById(R.id.close);
		Button btopen = (Button)findViewById(R.id.open);
		check1 = (CheckBox) findViewById(R.id.checkBox1);
		check1.setChecked(true);
		auto=10;
		check2 = (CheckBox) findViewById(R.id.checkBox2);
		check2.setChecked(false);
		area=0;
		recording = true;
		MainActivity mm = new MainActivity();
		Thread nn = new Thread(mm);
		nn.start();

		MyRunnable mr = new MyRunnable();
		Thread nt = new Thread(mr);
		nt.start();


		//オートロック，クリックされた時の動作を実装
		check1.setOnClickListener(new View.OnClickListener() {
			@Override
			// チェックされた時に呼び出されます
			public void onClick(View d) {
				if(check1.isChecked() == true){
					auto = 10;
				}else{
					auto = 0;
				}
				sendMessage();
			}
		});

		//アラーム，クリックされた時の動作を実装
		check2.setOnClickListener(new View.OnClickListener() {
			@Override
			// チェックされた時に呼び出されます
			public void onClick(View d) {
				if(check2.isChecked() == true){
					area = 1;

				}else{
					area = 0;
				}
				sendMessage();
			}
		});

		//鍵を開ける
		btopen.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				nxtBtopen();
			}
		});

		// 鍵をかける
		btclose.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				nxtBtclose();
			}
		});



	}


	static class MainActivity implements Runnable {
		public void run() {
			startRecord();

		}
		private void startRecord(){
			//バッファの設定

			//録音源
			recorder = new AudioRecord(
					MediaRecorder.AudioSource.MIC,
					8000,/*サンプリング周波数*/
					AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT,
					bufferSize);

			recorder.startRecording();

			while(recording){
				//numberOfShortは320


				numberOfShort = recorder.read(audioData, 0, bufferSize);

				mHandler.post(new Runnable() {
					@Override
					public void run() {
						for(z = 0; z < audioData.length; z++){
							for(y = 0; y < audioData.length; y++){
								if(z-y < 0){
									bb[y] = 0;
								}
								else{
									x[y] = z-y;
									bb[x[y]] = (short) Math.abs(audioData[x[y]]);
								}
							}
							mm[z] =      (short) (bb[x[0]] * 1.9954e-04 + bb[x[1]] * 2.2468e-04 + bb[x[2]] * 2.9890e-04 + bb[x[3]] * 4.1901e-04 + bb[x[4]] * 5.7978e-04 + bb[x[5]] * 7.7420e-04 + bb[x[6]] * 9.9380e-04 + bb[x[7]] * 1.2290e-03 + bb[x[8]] * 1.4695e-03 + bb[x[9]] * 1.7047e-03 + bb[x[10]] * 1.9245e-03
									+ bb[x[11]] * 2.1192e-03 + bb[x[12]] * 2.2802e-03 + bb[x[13]] * 2.4005e-03 + bb[x[14]] * 2.4748e-03 + bb[x[15]] * 2.5000e-03 + bb[x[16]] * 2.4748e-03 + bb[x[17]] * 2.4005e-03 + bb[x[18]] * 2.2802e-03 + bb[x[19]] * 2.1192e-03 + bb[x[20]] * 1.9245e-03 
									+ bb[x[21]] * 1.7047e-03 + bb[x[22]] * 1.4695e-03 + bb[x[23]] * 1.2290e-03 + bb[x[24]] * 9.9380e-04 + bb[x[25]] * 7.7420e-04 + bb[x[26]] * 5.7978e-04 + bb[x[27]] * 4.1901e-04 + bb[x[28]] * 2.9890e-04 + bb[x[29]] * 2.2468e-04 + bb[x[30]] * 1.9954e-04);

							audioData[z] = (short) Math.abs(audioData[z]);
						}

						for(int i = 0; i < numberOfShort; i++){
							if(i%100==0){
								if(mm[i]>800){
									textView2.setText("筋電  ON"); 
									key = 44;
									sendMessage();
									try {
										Thread.sleep(4000);              
									} catch (InterruptedException e) {
									}
								}else{
									textView2.setText("筋電 OFF");  
								}
							}
						}
						mByte = mm;



						mVisualizerView2.updateVisualizer(mByte);

						if(zz == 1){
							if(xx == 0 ){
								try {
									Thread.sleep(3000);              
								} catch (InterruptedException e) {
								}
								xx=1;
							}
							textView.setText("CONNECT");
							
						}else if(zz == 0){
							textView.setText("DISCONNECT"); 
							xx = 0;
						}
						return;
					}
				});
				
			}
			recorder.stop();
		};
	}




	//ダイレクトコマンドを送る
	public static void sendMessage(){
		if(nxtOutputStream != null || con==1){
			try{

				//全部足す
				state = key+auto+area;
				nxtOutputStream.write(state);
				nxtOutputStream.flush();
				key = 0;
				state = 0;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public void nxtBtopen(){
		if(con == 1){
			if(nxtSocket != null){
				//モーターを90度回転
				key = 100;
				sendMessage();
				//1秒停止
				try {
					Thread.sleep(1000);              
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void nxtBtclose(){
		if(con==1){
			if(nxtSocket != null){
				//モーターを-90度回転
				key = 200;
				sendMessage();

				//1秒停止
				try {
					Thread.sleep(1000);              
				} catch (InterruptedException e) {
				}
			}
		}
	}

	static class MyRunnable implements Runnable {
	    public void run() {
	    	while(true){
	    	if(con == 0){
	    	//Bluetooth接続可能デバイス検索
	    		
	    		nxtBtInit();
	    		nxtBtConnect(address);
	    		zz=0; 
	    	}
	    		
	    	
	    	if(nxtDevice.getBondState() != BluetoothDevice.BOND_BONDING){ 
	    		nxtBtInit();
	    		nxtBtConnect(address);
	    		zz=0; 
	    		
	    	}
	    	}
	    }

	    public static void nxtBtInit(){
	    	//Bluetoothに対応しているかどうか
	    BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
	    if(!Bt.equals(null)){
	    //Bluetooth 対応端末の場合の処理
	    	Log.v("BlueTooth","Bluetooth is supported.");
	    }else{
	    //Bluetooth 非対応端末の場合の処理
	    	Log.v("BlueTooth","Bluetooth is not supported.");
	    }

	    //Bluetoothが現在有効かつ使用可能な状態にされているかどうか
	    btEnable = Bt.isEnabled();
	    if(btEnable == true){
	    	Log.v("BlueTooth", "btEnable");
	    mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	    }else{
	    	Log.v("BlueTooth","Bluetooth should be turned ON.");
	    return;
	    }
	    }
	    @SuppressLint("NewApi")
	    public static void nxtBtConnect(String address){
	    	// BluetoothデバイスをMACアドレスから取得
	    nxtDevice = mBtAdapter.getRemoteDevice(address);
	    if(nxtDevice == null){
	    	Log.v("Bluetooth","Device is not found!");
	    }else{
	    try{
	    nxtSocket = nxtDevice.createInsecureRfcommSocketToServiceRecord(
	    SERIAL_PORT_SERVICE_CLASS_UUID);
	    nxtSocket.connect();
	    nxtOutputStream = nxtSocket.getOutputStream();
	    input = nxtSocket.getInputStream();
	    connected = true;
	    Log.v("Bluetooth","connect");
	    con=1;
	    zz=1;
	    try {
	    	bool = input.read();
	    	return;
	    } catch (IOException e1) {
	    	e1.printStackTrace();
	    } 
	    }catch(IOException e){
	    e.printStackTrace();
	    Log.v("Bluetooth","Socket error!");
	        return;
	    }
	    }
	    }
	    }


	    }
