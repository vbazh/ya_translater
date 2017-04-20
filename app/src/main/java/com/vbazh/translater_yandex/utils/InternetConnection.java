package com.vbazh.translater_yandex.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Класс отслеживающий подключение к интернету
 */

public class InternetConnection extends BroadcastReceiver {

    public static boolean INTERNET_CONNECTION = false;

    public boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    //пытаемся установить соединение с хостом
//    public boolean isInternetAvailable() {
//        String host = "www.yandex.ru";
//        int port = 80;
//        Socket socket = new Socket();
//
//        try {
//            socket.connect(new InetSocketAddress(host, port), 2000);
//            socket.close();
//            return true;
//        } catch (IOException e) {
//            try {
//                socket.close();
//            } catch (IOException es) {}
//            return false;
//        }
//    }

    //пингуем днс сервер яндекса, чтобы знать что интернет доступен
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 77.88.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (getConnectivityStatus(context)) {
            new TaskIsInternetAvailable().execute();
        }
    }

    //Асинхронным запросом выясняем доступен ли интернет
    private class TaskIsInternetAvailable extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... args) {
//            return isInternetAvailable();
            return isOnline();
        }

        protected void onPostExecute(Boolean result) {
            INTERNET_CONNECTION = result;
        }
    }
}
