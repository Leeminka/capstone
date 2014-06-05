/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kr.ac.kookmin.cs.cap3;

import static kr.ac.kookmin.cs.cap3.adapter.CommonUtilities.SENDER_ID;
import static kr.ac.kookmin.cs.cap3.adapter.CommonUtilities.displayMessage;










import kr.ac.kookmin.cs.cap3.adapter.ServerUtilities;
import kr.ac.kookmin.cs.cap3.adapter.ShowMsg;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;


/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {
	private static PowerManager.WakeLock 	sCpuWakeLock;
	
	Handler postHandler = new Handler();
    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        MainActivity.reg_id = registrationId;
        displayMessage(context, getString(R.string.gcm_registered));
        ServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }
    @SuppressLint("Wakelock")
	@SuppressWarnings("deprecation")
	@Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        PowerManager pm =(PowerManager) context.getSystemService(Context.POWER_SERVICE);
		sCpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK 
				| PowerManager.ACQUIRE_CAUSES_WAKEUP , "02");
		sCpuWakeLock.acquire(10000);

		Log.i(TAG, "Received message");

		
		Bundle extra = intent.getExtras(); 
		//final String message2 = extra.getString("title");
		final String title = extra.getString("title");
		displayMessage(context, title);

		// notifies user
		generateNotification(context, title);        

		//final String message3 = extra.getString("msg");
		final String message = extra.getString("msg");
		final String level = extra.getString("accept");

		
		displayMessage(context, message);

		final Context cte = context;
		Log.d("level", level);
		
		Intent i = new Intent(getApplicationContext(), ShowMsg.class);
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("msg", message);
		b.putString("accept", level);
		i.putExtras(b);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		cte.startActivity(i);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }
    

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_stat_gcm;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

}
