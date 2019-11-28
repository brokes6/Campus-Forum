package com.example.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.bottomnavigationabar2.PostDetails;
import com.example.bottomnavigationabar2.message.MessageType;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyReceiver extends JPushMessageReceiver {
    private static final String TAG = "MyReceiver";
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        Log.i(TAG, "onMessage: 收到消息拉"+customMessage.message);
    }
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        Log.i(TAG, "onNotifyMessageOpened: 被点击拉"+context);
        String extrasInfo =notificationMessage.notificationExtras;
        try {
            JSONObject jsonObject=new JSONObject(extrasInfo);
            Gson gson =new Gson();
            int type= jsonObject.getInt("type");
            Log.i(TAG, "onNotifyMessageOpened: "+extrasInfo+type);
            switch (type) {
                case MessageType.COMMENT:
                    Log.i(TAG, "onNotifyMessageOpened: 开始跳转");
                    Intent intent1 = new Intent(context, PostDetails.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent1.putExtra("postId",jsonObject.getInt("cpid"));
                    context.startActivity(intent1);
                    break;
                case MessageType.POST:
                    Intent intent2 = new Intent(context, PostDetails.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent2.putExtra("postId",jsonObject.getInt("pid"));
                    context.startActivity(intent2);
                    break;
                case MessageType.REPLY:
                    Intent intent3 = new Intent(context, PostDetails.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent3.putExtra("postId",jsonObject.getInt("pid"));
                    context.startActivity(intent3);
                    break;
                default:break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Context context, boolean b) {
        Log.i(TAG, "onConnected: 开始连接");
        super.onConnected(context, b);
    }

}