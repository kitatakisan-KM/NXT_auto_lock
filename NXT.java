
import java.io.IOException;
import java.io.InputStream;
import lejos.nxt.*;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;


public class NXT{
public static InputStream input;
public static NXTConnection bluetoothConnection;

static UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S4); 
static int deg,sou,kagi,area,state;
static int key,sensor,auto,one,two,three,msg,joutai;
static String name;
static MyRunnable1 mm = new MyRunnable1();
static Thread nn = new Thread(mm);
static String joutai1;
static byte[] data;

public static void main(String[] args) throws IOException{
	
	 MyRunnable mr = new MyRunnable();
     Thread nt = new Thread(mr);
     nt.start();


sou	= 0;
auto = 1;
kagi = 0;
area = 0;
key=0;
deg = 0;
joutai = 0;
while(!Button.ESCAPE.isDown()){
	if(data != null && data[3] == 2){
	LCD.drawString("           ",0, 0) ;
	LCD.drawString("connect",0, 0) ;
	}
	else if(data != null && data[3] < 2){
		LCD.drawString("disconnect",0, 0) ;
		}
	
	sensor = 0;
	LCD.drawString("    ",5, 4) ;
	LCD.drawString("deg", 0, 4);
	LCD.drawInt(Motor.B.getTachoCount(), 5, 4) ;   //*

	//鍵が開いた
	if(deg >= 10){
		key = 1;
		LCD.drawString("unlock", 0, 1) ;
		try {
		    Thread.sleep(3000);               
		} catch (InterruptedException e) {
		}
		Motor.B.flt();
		if(sou!=1 && area == 1){
		sound();
		}
		if(auto == 1){
		sonic();
		}
		sou=0;
	}
	
	deg = Motor.B.getTachoCount();
	try {
	    Thread.sleep(1000);               
	} catch (InterruptedException e) {
	}
	
	//鍵が閉まった
	if(deg < 10){
			key = 0;
			LCD.drawString("      ", 0, 1) ;
			LCD.drawString("lock", 0, 1) ;
			try {
			    Thread.sleep(3000);               
			} catch (InterruptedException e) {
			}
		Motor.B.flt();
	}
	
	LCD.drawString("OK", 9, 4) ;  
	LCD.drawString("XX", 9, 5) ; 
	
	
	
	deg = Motor.B.getTachoCount();
	try {
	    Thread.sleep(1000);               
	} catch (InterruptedException e) {
	}
}

}





//超音波センサ
static void sonic(){

	one = ultrasonicSensor.getDistance();
	two = one;
	three = two;
	//オートロック機能10の位ON(1)かOFF(0)か
	while (key == 1 && deg >= 10) {
		LCD.drawString("   ", 1, 5) ;
		LCD.drawInt(one, 1, 5) ;
		LCD.drawString("   ", 1, 6) ;
		LCD.drawInt(two, 1, 6) ;
		LCD.drawString("   ", 1, 7) ;
		LCD.drawInt(three, 1, 7) ;
		LCD.drawString("XX", 9, 4) ;  
		LCD.drawString("OK", 9, 5) ;
		three = two;
		two = one;
		one = ultrasonicSensor.getDistance();
		deg = Motor.B.getTachoCount();
		if(three != two || two != one){
			sensor = 1;
		}
		else if(three == two && two == one && sensor == 1){
			if(one != 255){
				Motor.B.setSpeed(70); 
			Motor.B.rotate(-deg);
			LCD.drawString("              ", 0, 2);
			LCD.drawString("rotate", 0, 2);
			LCD.drawInt(-deg, 7, 2) ;
			three = 0;
			two = 0;
			one = 0;
			try {
			    Thread.sleep(3000);               
			} catch (InterruptedException e) {
			}
			return;
			}
		}
		
		//1秒停止
		try {
            Thread.sleep(1500);              
        } catch (InterruptedException e) {
        }
	}
	three = 0;
	two = 0;
	one = 0;
	return;
	
}
static void sound(){
		Sound.setVolume(100);
		while(!Button.ESCAPE.isDown()){
		Sound.playTone(1047, 1000);
		Sound.pause(2000); 
		}

}

//端末との変数受け渡し
 static class MyRunnable implements Runnable {
     public void run() {
    	 while(!Button.ESCAPE.isDown()){
    		 if(state == 0 ){
    			 Bluetooth.reset();
    			 con();
    		 }else if(state ==1 && data[3] == 2){
    			 
 		
 		
 		try {
 			
			if (input.available() != 0) {
				msg =input.read();
				LCD.drawString("      ", 2, 3) ;
				LCD.drawInt(msg, 2, 3) ;
					//鍵の操作
			if(msg>=100 && msg<=111){
				kagi=1;
				msg = msg - 100;
			}else if(msg >= 200 && msg < 300){
				kagi=2;
				msg = msg - 200;
			}else if(msg >= 44 && msg <= 55){
				kagi = 3;
				msg = msg - 44;
			}
			//オートロックの操作
			if(msg==11||msg==10){
				auto = 1;
				
				msg = msg-10;
			}else if(msg==1||msg==0){
				auto = 0;
				
			}
			//アラートの操作
			area = msg;
	switch(area){
			
			case 1:	
				
				break;
				
			case 0: 
				
			break;
		}
				
					switch(kagi){
					
					case 1:	//鍵を開ける100の位1
						if(key==0){
						Motor.B.setSpeed(70); 
						Motor.B.rotate(90);
						try {
				            Thread.sleep(3000);              
				        } catch (InterruptedException e) {
				        }
						LCD.drawString("              ", 0, 2);
						LCD.drawString("rotate", 0, 2);
						LCD.drawInt(deg, 7, 2) ;
						
						}
						
						sou=1;
						kagi=0;
						break;
						
					case 2: //鍵をしめる100の位2
						if(key==1){
							deg = Motor.B.getTachoCount();
						Motor.B.setSpeed(70); 
						Motor.B.rotate(-deg);
						try {
				            Thread.sleep(3000);              
				        } catch (InterruptedException e) {
				        }
						LCD.drawString("              ", 0, 2);
						LCD.drawString("rotate", 0, 2);
						LCD.drawInt(-deg, 7, 2) ;
						}
						
						kagi=0;
					break;
					case 3:
						if(key==1){
							Motor.B.setSpeed(70); 
							Motor.B.rotate(-90);
							try {
					            Thread.sleep(3000);              
					        } catch (InterruptedException e) {
					        }
						}else if(key==0){
								Motor.B.setSpeed(70); 
								Motor.B.rotate(90);
								try {
						            Thread.sleep(3000);              
						        } catch (InterruptedException e) {
						        }
							}
						
						kagi=0;
					break;
					//*
					default:
						try {
				            Thread.sleep(3000);              
				        } catch (InterruptedException e) {
				        }
						break;
				}
					
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
		}
 		
 		
    		 }
    		 
    	 }
     }
   }
 
 //接続状況確認
 static class MyRunnable1 implements Runnable {
     public void run() {
    	 while(!Button.ESCAPE.isDown()){
    		 if(state == 1){
    	 data=Bluetooth.getConnectionStatus();
         if(data[3] == 0 || data[3] == 1){
        	state = 0;
         }
         }
         } 
     }
     }
 
 //端末との接続
 public static void con(){
	 bluetoothConnection = Bluetooth.waitForConnection();
		bluetoothConnection.setIOMode(NXTConnection.RAW);
		input = bluetoothConnection.openInputStream();
		data=Bluetooth.getConnectionStatus();
		state=1;
		if(joutai == 0){
		nn.start();
		joutai = 1;
		}
		return ;
 }
}