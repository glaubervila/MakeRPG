package br.com.makerpg.vilaverde.makerpg;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class MyService extends Service {

    private static String TAG = MyService.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;

    public MyService() {
//        Exemplo Retirado
//        http://www.coderzheaven.com/2012/07/14/http-call-repeatedly-service-android/
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mythread  = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
    }

    public void readWebPage(){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://google.com");
        // Get the response
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response_str = null;
        try {
            response_str = client.execute(request, responseHandler);
            if(!response_str.equalsIgnoreCase("")){
                Log.d(TAG, "Got Response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyThread extends Thread{
        static final long DELAY = 3000;
        @Override
        public void run(){
            while(isRunning){
                Log.d(TAG,"Running");
                try {
                    readWebPage();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }
    }
}
