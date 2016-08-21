package com.example.android.sunshine.app;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

public class MessageListenerService extends WearableListenerService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final String WEAR_MESSAGE_PATH = "/wear_message_path";
    public static final String MOBILE_MESSAGE_PATH = "/mobile_message_path";

    private GoogleApiClient mApiClient;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if(messageEvent.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH)) {
            setUpGoogleApiClient();
        }
    }

    private void setUpGoogleApiClient(){
        mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
        mApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        sendData();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("TAG", connectionResult.getErrorCode()+"");
    }

    private void sendData() {
        ArrayList<Double> tempValues = ForecastFragment.getTempValues();
        Log.v("TAG", tempValues.size()+"");
        final DataMap dataMap = new DataMap();
        dataMap.putDouble("max", tempValues.get(0));
        dataMap.putDouble("min", tempValues.get(1));
        dataMap.putInt("icon", ForecastFragment.getIconId());
        dataMap.putLong("timestamp", System.currentTimeMillis());

        new Thread( new Runnable() {
            @Override
            public void run() {
                PutDataMapRequest dataMapRequest = PutDataMapRequest.create(MOBILE_MESSAGE_PATH).setUrgent();
                dataMapRequest.getDataMap().putAll(dataMap);
                PutDataRequest request = dataMapRequest.asPutDataRequest();

                DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mApiClient, request).await();
                if(result.getStatus().isSuccess()){
                    Log.v("TAG", "Sent successfully");
                }else {
                    Log.v("TAG", "Failed to send");
                }
            }
        }).start();
    }
}
