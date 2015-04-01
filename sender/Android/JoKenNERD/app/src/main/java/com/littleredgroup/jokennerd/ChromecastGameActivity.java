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

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by fbvictorhugo on 10/4/14.
 * <p/>
 * implements Google APIs
 */
public class ChromecastGameActivity extends GameActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

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
        addCallbackMediaRouter();
    }

    @Override
    public void onResume() {
        super.onResume();
        addCallbackMediaRouter();
    }

    @Override
    protected void onPause() {
        removeCallbackMediaRouter();
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
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat
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
            tvMessageLayout.setText(R.string.msg_need_chromecast_connection);
        } else {
            rlGameContent.setVisibility(View.INVISIBLE);
            tvMessageLayout.setVisibility(View.VISIBLE);
        }
    }

    /*  Google SDK Cast Implements */
    public String getCastNamespace() {
        return getString(R.string.cast_namespace);
    }

    public String getCastAppId() {
        return getString(R.string.cast_app_id);
    }

    private void addCallbackMediaRouter() {
        mMediaRouter.addCallback(
                mMediaRouteSelector,
                mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    private void removeCallbackMediaRouter() {
        mMediaRouter.removeCallback(mMediaRouterCallback);
    }

    void initializeMediaRouter() {
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent
                        .categoryForCast(getString(R.string.cast_app_id))).build();
        mMediaRouterCallback = new MyMediaRouterCallback();
    }

    void launchReceiver() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Cast.API, getCastOptionsBuild(mSelectedDevice))
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            mGoogleApiClient.connect();
        } catch (Exception e) {
            Log.e(TAG, "Failed launchReceiver", e);
        }
    }

    private Cast.CastOptions getCastOptionsBuild(final CastDevice castDevice) {

        Cast.Listener castListener = new Cast.Listener() {

            @Override
            public void onApplicationDisconnected(int errorCode) {
                Log.d(TAG, "Cast.Listener: Application Disconnected!");
                tearDown();
                invalidateOptionsMenu();
            }
        };


        Cast.CastOptions.Builder builder = Cast.CastOptions.builder(castDevice, castListener);
        builder.setVerboseLoggingEnabled(true);

        return builder.build();
    }

    ResultCallback<Cast.ApplicationConnectionResult> appConnectionResultCallback =
            new ResultCallback<Cast.ApplicationConnectionResult>() {

                @Override
                public void onResult(Cast.ApplicationConnectionResult result) {
                    Status status = result.getStatus();
                    Log.d(TAG, "ApplicationConnectionResult " + status.getStatusCode());

                    if (status.isSuccess()) {

                        wasLaunched = result.getWasLaunched();
                        mMyMessageReceivedCallbacks = new MyMessageReceivedCallback();

                        try {
                            Cast.CastApi.setMessageReceivedCallbacks(mGoogleApiClient,
                                    getCastNamespace(),
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
            Log.d("MediaRouter.Callback", route.getName() + " Added.");
            tvMessageLayout.setText(String.format(getString(R.string.msg_suggest_cast),
                    route.getName()));
        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {
            Log.d("MediaRouter.Callback", route.getName() + " Removed.");
        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
            Log.d("MediaRouter.Callback", route.getName() + " Selected!");
            mSelectedDevice = CastDevice.getFromBundle(route.getExtras());
            setMessageStatus(String.format(getString(R.string.msg_cast_connected), route.getName()));
            launchReceiver();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo route) {
            Log.d("MediaRouter.Callback", route.getName() + " Unselected.");
            tearDown();
            mSelectedDevice = null;
            showGameContent(false);
        }
    }

    class MyMessageReceivedCallback implements Cast.MessageReceivedCallback {

        @Override
        public void onMessageReceived(CastDevice castDevice,
                                      String namespace, String message) {

            try {
                String msgResult = getMessageStatus(new JSONObject(message));
                setMessageStatus(msgResult);

                Log.d("onMessageReceived ", message);
            } catch (Exception e) {
                setMessageStatus(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    void sendMessageToCast(final String message) {
        try {
            Cast.CastApi.sendMessage(mGoogleApiClient,
                    getCastNamespace(), message)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status result) {
                            if (result.isSuccess()) {
                                Log.d("Cast.sendMessage", message);
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void tearDown() {
        Log.d(TAG, "teardown");

        if (wasLaunched) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                try {
                    Cast.CastApi.leaveApplication(mGoogleApiClient);
                    if (mMyMessageReceivedCallbacks != null) {
                        Cast.CastApi.removeMessageReceivedCallbacks(
                                mGoogleApiClient, getCastNamespace());
                        mMyMessageReceivedCallbacks = null;
                        invalidateOptionsMenu();
                        showGameContent(false);
                        hideMessageStatus();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception while removing channel", e);
                }
                mGoogleApiClient.disconnect();
            }
            wasLaunched = false;
        }

        mGoogleApiClient = null;
        mSelectedDevice = null;
        mWaitingForReconnect = false;
    }

    /* Google APIs  */

    @Override
    public void onConnected(Bundle bundle) {
        showGameContent(true);
        if (mWaitingForReconnect) {
            mWaitingForReconnect = false;
            if ((bundle != null) && bundle.getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
                Log.d(TAG, "App não está mais sendo executado");
            } else {
                try {
                    Cast.CastApi.setMessageReceivedCallbacks(
                            mGoogleApiClient, getCastNamespace(),
                            mMyMessageReceivedCallbacks);
                } catch (IOException e) {
                    Log.e(TAG, "Exception while creating channel", e);
                }
            }
        } else {
            Cast.CastApi.launchApplication(mGoogleApiClient,
                    getCastAppId()).setResultCallback(appConnectionResultCallback);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mWaitingForReconnect = true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("onConnectionFailed", connectionResult.toString());
        tearDown();
    }

    /* Game */
    private String getMessageStatus(JSONObject json) throws Exception {

        if (json == null) {
            return "";
        }

        String message = "";

        if (json.has(Constants.KEY_SUCESS)
                && json.getString(Constants.KEY_SUCESS).equals(
                Constants.RESULT_SUCCESS_CONECTED)) {

            message = String.format(getString(R.string.msg_success), getPlayerName());

        } else if (json.has(Constants.KEY_ERROR)
                && json.getString(Constants.KEY_ERROR).equals(
                Constants.RESULT_ROOM_IS_FULL)) {

            return getString(R.string.msg_room_full);

        } else if (json.has(Constants.KEY_RESULT)) {
            if (json.getString(Constants.KEY_RESULT).equals(
                    Constants.RESULT_WIN)) {

                message = getString(R.string.msg_win);

            } else if (json.getString(Constants.KEY_RESULT).equals(
                    Constants.RESULT_LOSE)) {

                message = getString(R.string.msg_loses);

            } else if (json.getString(Constants.KEY_RESULT).equals(
                    Constants.RESULT_DRAW)) {

                message = getString(R.string.msg_draw);
            }
        }

        return message;
    }

    @Override
    public void onRockClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_ROCK));
        setMessageStatus(getString(R.string.msg_wait_oponent));
    }

    @Override
    public void onPaperClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_PAPER));
        setMessageStatus(getString(R.string.msg_wait_oponent));
    }

    @Override
    public void onScissorClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_SCISSOR));
        setMessageStatus(getString(R.string.msg_wait_oponent));
    }

    @Override
    public void onLizardClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_LIZARD));
        setMessageStatus(getString(R.string.msg_wait_oponent));
    }

    @Override
    public void onSpockClick() {
        sendMessageToCast(CastMessages.messageToChoice(Constants.CHOICE_SPOCK));
        setMessageStatus(getString(R.string.msg_wait_oponent));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tearDown();
    }
}
