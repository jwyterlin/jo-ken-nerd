package com.littleredgroup.jokennerd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.littleredgroup.jokennerd.utils.CastMessages;
import com.littleredgroup.jokennerd.utils.Constants;
import com.littleredgroup.jokennerd.utils.Utils;

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
    private GoogleApiClient mGoogleApiClient;

    private boolean wasLaunched;
    private boolean mWaitingForReconnect;
    private MyMessageReceivedCallback mMyMessageReceivedCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.lbl_multi_player);
        showGameContent(false);
        initializeMediaRouter();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the callback to start device discovery
        mMediaRouter.addCallback(
                mMediaRouteSelector,
                mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
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
        getMenuInflater().inflate(R.menu.game_cast, menu);

        MenuItem mediaRouteMenuItem = menu.findItem(R.id.action_media_route);
        MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat
                .getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_rules:
                Intent i = new Intent(this, RulesActivity.class);
                startActivity(i);
                break;

            case R.id.action_chromecast:
                Utils.openHiperlink(this, Constants.URL_CHROMECAST);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showGameContent(boolean isShow) {
        if (isShow) {
            rlGameContent.setVisibility(View.VISIBLE);
            tvMessageLayout.setVisibility(View.GONE);
        } else {
            rlGameContent.setVisibility(View.INVISIBLE);
            tvMessageLayout.setVisibility(View.VISIBLE);
        }
    }


    /*  Google SDK Cast Implements */

    void initializeMediaRouter() {
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent
                        .categoryForCast(getString(R.string.cast_app_id)))
                .build();
        mMediaRouterCallback = new MyMediaRouterCallback();
    }

    void launchReceiver() {
        try {


            final Cast.Listener castListener = new Cast.Listener() {

                @Override
                public void onApplicationDisconnected(int errorCode) {
                    Log.d(TAG, "application has stopped");
                    tearDown();
                }
            };

            // A builder to create an instance of Cast.CastOptions to set API configuration parameters for Cast.
            Cast.CastOptions.Builder castOptionsBuilder =
                    Cast.CastOptions.builder(mSelectedDevice, castListener);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Cast.API, castOptionsBuilder.build())
                    .addConnectionCallbacks(new MyGoogleApiConnectionCallbacks())
                    .addOnConnectionFailedListener(new MyGoogleApiConnectionFailedListener()).build();

            mGoogleApiClient.connect();
        } catch (Exception e) {
            Log.e(TAG, "Failed launchReceiver", e);
        }
    }

    ResultCallback<Cast.ApplicationConnectionResult> appConnectionResult =
            new ResultCallback<Cast.ApplicationConnectionResult>() {

                @Override
                public void onResult(Cast.ApplicationConnectionResult result) {
                    Status status = result.getStatus();
                    Log.d(TAG, "ApplicationConnectionResult " + status.getStatusCode());

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

                        sendMessageToCast(CastMessages.messageSetupUser(getPlayerName()));

                    } else {
                        Log.e(TAG, "application could not launch");
                        tearDown();
                    }
                }
            };

    class MyMediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {

        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {

        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
            mSelectedDevice = CastDevice.getFromBundle(route.getExtras());
            launchReceiver();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo route) {
            tearDown();
            mSelectedDevice = null;
            showGameContent(false);
        }
    }

    class MyGoogleApiConnectionCallbacks
            implements GoogleApiClient.ConnectionCallbacks {

        @Override
        public void onConnected(Bundle connectionHint) {

            showGameContent(true);
            try {
                if (mWaitingForReconnect) {
                    mWaitingForReconnect = false;

                    if ((connectionHint != null) &&
                            connectionHint.getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {

                        Log.d(TAG, "App não está mais sendo executado");
                    } else {
                        try {
                            Cast.CastApi.setMessageReceivedCallbacks(
                                    mGoogleApiClient,
                                    mMyMessageReceivedCallbacks.getNamespace(),
                                    mMyMessageReceivedCallbacks);
                        } catch (IOException e) {
                            Log.e(TAG, "Exception while creating channel", e);
                        }
                    }

                } else {
                    Cast.CastApi.launchApplication(mGoogleApiClient,
                            getString(R.string.cast_app_id)).setResultCallback(
                            appConnectionResult);

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

    class MyGoogleApiConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            tearDown();
            Log.d("onConnectionFailed", result.toString());
        }
    }

    class MyMessageReceivedCallback implements Cast.MessageReceivedCallback {

        public String getNamespace() {
            return getString(R.string.cast_namespace);
        }

        @Override
        public void onMessageReceived(CastDevice castDevice,
                                      String namespace,
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

    void tearDown() {
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

    void sendMessageToCast(String message) {
        try {
            Cast.CastApi.sendMessage(mGoogleApiClient,
                    mMyMessageReceivedCallbacks.getNamespace(), message)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status result) {
                            if (result.isSuccess()) {

                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Game */
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

    @Override
    public void onRockClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_ROCK));
        setResult(R.string.msg_wait_oponent);
    }

    @Override
    public void onPaperClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_PAPER));
        setResult(R.string.msg_wait_oponent);
    }

    @Override
    public void onScissorClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_SCISSOR));
        setResult(R.string.msg_wait_oponent);
    }

    @Override
    public void onLizardClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_LIZARD));
        setResult(R.string.msg_wait_oponent);
    }

    @Override
    public void onSpockClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_SPOCK));
        setResult(R.string.msg_wait_oponent);
    }
}
