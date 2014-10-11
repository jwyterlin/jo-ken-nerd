package com.littleredgroup.jokennerd;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.littleredgroup.jokennerd.utils.CastMessages;
import com.littleredgroup.jokennerd.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by fbvictorhugo on 10/4/14.
 */
public class ChromecastGameActivity extends GameActivity {

    private String TAG = ChromecastGameActivity.class.getSimpleName();
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;
    private CastDevice mSelectedDevice;
    private int mRouteCount = 0;
    private GoogleApiClient mGoogleApiClient;
    private Cast.Listener mCastListener;
    private boolean wasLaunched;
    private boolean mWaitingForReconnect;
    private MyMessageReceivedCallback mMyMessageReceivedCallbacks;

    ResultCallback<Cast.ApplicationConnectionResult> castResultCallback = new ResultCallback<Cast.ApplicationConnectionResult>() {

        @Override
        public void onResult(Cast.ApplicationConnectionResult result) {
            Status status = result.getStatus();
            Log.d(TAG,
                    "ApplicationConnectionResultCallback.onResult: statusCode"
                            + status.getStatusCode());

            if (status.isSuccess()) {
                // mSessionId = result.getSessionId();
                wasLaunched = result.getWasLaunched();
                mMyMessageReceivedCallbacks = new MyMessageReceivedCallback();

                try {
                    Cast.CastApi.setMessageReceivedCallbacks(mGoogleApiClient,
                            mMyMessageReceivedCallbacks.getNamespace(),
                            mMyMessageReceivedCallbacks);
                } catch (IOException e) {
                    Log.e(TAG, "Exception while creating channel", e);
                }

                sendMessage(CastMessages.messageSetupUser("Hello"));

            } else {
                Log.e(TAG, "application could not launch");
                tearDown();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeMediaRouter();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the callback to start device discovery
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    @Override
    protected void onPause() {
        // Remove the callback to stop device discovery
        mMediaRouter.removeCallback(mMediaRouterCallback);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        tearDown();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.old_main, menu);

        MenuItem mediaRouteMenuItem = menu.findItem(R.id.action_media_route);
        MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat
                .getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
        return true;
    }

    @Override
    public void onRockClick() {
        sendMessage(CastMessages.messageToChoice(Constants.CHOICE_ROCK));
        setResult(R.string.msg_wait_oponent);
    }

    @Override
    public void onPaperClick() {
        sendMessage(CastMessages.messageToChoice(Constants.CHOICE_PAPER));
        setResult(R.string.msg_wait_oponent);

    }

    @Override
    public void onScissorClick() {
        sendMessage(CastMessages.messageToChoice(Constants.CHOICE_SCISSOR));
        setResult(R.string.msg_wait_oponent);
    }

    @Override
    public void onLizardClick() {
        sendMessage(CastMessages.messageToChoice(Constants.CHOICE_LIZARD));
        setResult(R.string.msg_wait_oponent);
    }

    @Override
    public void onSpockClick() {
        sendMessage(CastMessages.messageToChoice(Constants.CHOICE_SPOCK));
        setResult(R.string.msg_wait_oponent);
    }

    private void initializeMediaRouter() {
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(
                        CastMediaControlIntent
                                .categoryForCast(getString(R.string.cast_app_id)))
                .build();
        mMediaRouterCallback = new MyMediaRouterCallback();
    }

    private String getMessageStatus(JSONObject json) throws Exception {
        if (json == null) {
            return "";
        }

        if (json.has(Constants.KEY_SUCESS)) {
            if (json.getString(Constants.KEY_SUCESS) != null
                    && json.getString(Constants.KEY_SUCESS).equals(
                    Constants.RESULT_SUCCES_CONECTED)) {
                return getString(R.string.msg_sucess);

            }
        } else if (json.has(Constants.KEY_ERROR)) {
            if (json.getString(Constants.KEY_ERROR) != null
                    && json.getString(Constants.KEY_ERROR).equals(
                    Constants.RESULT_ROOM_IS_FULL)) {
                return getString(R.string.msg_room_full);

            }

        } else if (json.has(Constants.KEY_RESULT)) {
            if (json.getString(Constants.KEY_RESULT) != null
                    && json.getString(Constants.KEY_RESULT).equals(
                    Constants.RESULT_WIN)) {
                return getString(R.string.msg_win);
            } else if (json.getString(Constants.KEY_RESULT) != null
                    && json.getString(Constants.KEY_RESULT).equals(
                    Constants.RESULT_LOSE)) {
                return getString(R.string.msg_loses);
            } else if (json.getString(Constants.KEY_RESULT) != null
                    && json.getString(Constants.KEY_RESULT).equals(
                    Constants.RESULT_DRAW)) {
                return getString(R.string.msg_draw);
            }
        }

        return "";
    }

    private void tearDown() {
        Log.d(TAG, "teardown");
        if (mGoogleApiClient != null) {
            if (wasLaunched) {
                if (mGoogleApiClient.isConnected()) {
                    try {
                        Cast.CastApi.leaveApplication(mGoogleApiClient);
                        if (mMyMessageReceivedCallbacks != null) {
                            Cast.CastApi.removeMessageReceivedCallbacks(
                                    mGoogleApiClient,
                                    mMyMessageReceivedCallbacks.getNamespace());
                            mMyMessageReceivedCallbacks = null;
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception while removing channel", e);
                    }
                    mGoogleApiClient.disconnect();
                }
                wasLaunched = false;
            }
            mGoogleApiClient = null;
        }
        mSelectedDevice = null;
        mWaitingForReconnect = false;
    }


    private void launchReceiver() {
        try {
            mCastListener = new Cast.Listener() {

                @Override
                public void onApplicationDisconnected(int errorCode) {
                    Log.d(TAG, "application has stopped");
                    tearDown();
                }

            };
            Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                    .builder(mSelectedDevice, mCastListener);
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Cast.API, apiOptionsBuilder.build())
                    .addConnectionCallbacks(new GooglePlayConnectionCallbacks())
                    .addOnConnectionFailedListener(
                            new GooglePlayConnectionFailedListener()).build();

            mGoogleApiClient.connect();
        } catch (Exception e) {
            Log.e(TAG, "Failed launchReceiver", e);
        }
    }

    private void sendMessage(String message) {
        if (mGoogleApiClient != null && mMyMessageReceivedCallbacks != null) {
            try {
                Cast.CastApi.sendMessage(mGoogleApiClient,
                        mMyMessageReceivedCallbacks.getNamespace(), message)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status result) {
                                if (!result.isSuccess()) {
                                    Log.e(TAG, "Sending message failed");
                                }
                            }
                        });
            } catch (Exception e) {
                Log.e(TAG, "Exception while sending message", e);
            }
        }
    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {

            showToastLong("onRouteAdded");
            if (++mRouteCount == 1) {
                // Show the button when a device is discovered.
            }
        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {

            showToastLong("onRouteRemoved");
            if (--mRouteCount == 0) {
                // Hide the button if there are no devices discovered.
            }
        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {

            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            launchReceiver();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {

            showToastLong("onRouteUnselected: info=" + info);
            tearDown();
            mSelectedDevice = null;
        }
    }

    class MyMessageReceivedCallback implements Cast.MessageReceivedCallback {

        public String getNamespace() {
            return getString(R.string.cast_namespace);
        }

        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace,
                                      String message) {
            JSONObject jResult = null;
            try {
                jResult = new JSONObject(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String msgResult = "";
            try {
                msgResult = getMessageStatus(jResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setMessaegStatus(msgResult);

            Log.d("onMessageReceived ", message);
        }
    }

    private class GooglePlayConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks {

        @Override
        public void onConnected(Bundle connectionHint) {

            try {
                if (mWaitingForReconnect) {
                    mWaitingForReconnect = false;

                    // Check if the receiver app is still running
                    if ((connectionHint != null)
                            && connectionHint
                            .getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
                        Log.d(TAG, "App  is no longer running");
                    } else {

                        // Re-create the custom message channel
                        try {
                            Cast.CastApi.setMessageReceivedCallbacks(
                                    mGoogleApiClient,
                                    mMyMessageReceivedCallbacks.getNamespace(),
                                    mMyMessageReceivedCallbacks);
                        } catch (IOException e) {
                            Log.e(TAG,
                                    "Exception while creating channel", e);
                        }
                    }

                } else {
                    Cast.CastApi.launchApplication(mGoogleApiClient,
                            getString(R.string.cast_app_id)).setResultCallback(
                            castResultCallback);

                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to launch application", e);
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            mWaitingForReconnect = true;
        }

    }

    private class GooglePlayConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            tearDown();
            Log.d("onConnectionFailed", result.toString());
        }
    }

}
