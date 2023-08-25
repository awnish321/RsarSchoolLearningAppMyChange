package rsarschoolmodel.com.rsarschoolmodel;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Push_Notification_Android extends FirebaseMessagingService {
    private static final String TAG = "checkfire";

    public Push_Notification_Android() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}