package com.example.android.sunshine.app;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class DataListenerService extends WearableListenerService {

    public static final String MOBILE_MESSAGE_PATH = "/mobile_message_path";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        DataMap dataMap = new DataMap();

        for(DataEvent dataEvent : dataEvents){
            if(dataEvent.getType()==DataEvent.TYPE_CHANGED){
                String path = dataEvent.getDataItem().getUri().getPath();

                if(path.equalsIgnoreCase(MOBILE_MESSAGE_PATH)){
                    dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                }
            }
        }

        Intent messageIntent = new Intent();
        messageIntent.setAction(Intent.ACTION_SEND);
        messageIntent.putExtra("datamap", dataMap.toBundle());
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
    }
}
