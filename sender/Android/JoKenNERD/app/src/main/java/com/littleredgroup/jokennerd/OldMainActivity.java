package com.littleredgroup.jokennerd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.ApplicationConnectionResult;
import com.google.android.gms.cast.Cast.MessageReceivedCallback;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class OldMainActivity extends ActionBarActivity implements OnClickListener {
    private final String CLASS_TAG = OldMainActivity.class.getSimpleName();

    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;
    private CastDevice mSelectedDevice;
    private GoogleApiClient mGoogleApiClient;
    private Cast.Listener mCastListener;
    private boolean wasLaunched;
    private boolean mWaitingForReconnect;
    // private String mSessionId;
    private MyMessageReceivedCallback mMyMessageReceivedCallbacks;
    private EditText etName;
    private ImageView ivRock;
    private ImageView ivPaper;
    private ImageView ivScissor;
    private ImageView ivLizard;
    private ImageView ivSpock;

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);

        initializeMediaRouter();
        setComponentsView();
        setViewListerns();

        etName.setText(getNameUserRandom());
    }

    private void setComponentsView() {
        ivRock = (ImageView) findViewById(R.id.game_iv_rock);
        ivPaper = (ImageView) findViewById(R.id.game_iv_paper);
        ivScissor = (ImageView) findViewById(R.id.game_iv_scissor);
        ivLizard = (ImageView) findViewById(R.id.game_iv_lizard);
        ivSpock = (ImageView) findViewById(R.id.game_iv_spock);

        etName = (EditText) findViewById(R.id.editText1);
        tvResult = (TextView) findViewById(R.id.game_tv_result);
    }

    private void setViewListerns() {
        ivRock.setOnClickListener(this);
        ivPaper.setOnClickListener(this);
        ivScissor.setOnClickListener(this);
        ivLizard.setOnClickListener(this);
        ivSpock.setOnClickListener(this);

        etName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                sendMessage(CastMessages.messageUpdateUser(s.toString()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_tutorial:
                Intent intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
        return super.onOptionsItemSelected(item);
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
    protected void onResume() {
        super.onResume();
        startMediaRouterDiscovery();
    }

    @Override
    public void onDestroy() {
        tearDown();
        super.onDestroy();
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

    private void startMediaRouterDiscovery() {
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
    }

    private void launchReceiver() {
        try {
            mCastListener = new Cast.Listener() {

                @Override
                public void onApplicationDisconnected(int errorCode) {
                    Log.d(CLASS_TAG, "application has stopped");
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
            Log.e(CLASS_TAG, "Failed launchReceiver", e);
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
                                    Log.e(CLASS_TAG, "Sending message failed");
                                }
                            }
                        });
            } catch (Exception e) {
                Log.e(CLASS_TAG, "Exception while sending message", e);
            }
        }
    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteSelected(MediaRouter router, RouteInfo route) {
            mSelectedDevice = CastDevice.getFromBundle(route.getExtras());
            launchReceiver();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, RouteInfo route) {
            tearDown();
            mSelectedDevice = null;
        }
    }

    class MyMessageReceivedCallback implements MessageReceivedCallback {

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
                        Log.d(CLASS_TAG, "App  is no longer running");
                    } else {

                        // Re-create the custom message channel
                        try {
                            Cast.CastApi.setMessageReceivedCallbacks(
                                    mGoogleApiClient,
                                    mMyMessageReceivedCallbacks.getNamespace(),
                                    mMyMessageReceivedCallbacks);
                        } catch (IOException e) {
                            Log.e(CLASS_TAG,
                                    "Exception while creating channel", e);
                        }
                    }

                } else {
                    Cast.CastApi.launchApplication(mGoogleApiClient,
                            getString(R.string.cast_app_id)).setResultCallback(
                            castResultCallback);

                }
            } catch (Exception e) {
                Log.e(CLASS_TAG, "Failed to launch application", e);
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            mWaitingForReconnect = true;
        }

    }

    ResultCallback<ApplicationConnectionResult> castResultCallback = new ResultCallback<ApplicationConnectionResult>() {

        @Override
        public void onResult(ApplicationConnectionResult result) {
            Status status = result.getStatus();
            Log.d(CLASS_TAG,
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
                    Log.e(CLASS_TAG, "Exception while creating channel", e);
                }

                sendMessage(CastMessages.messageSetupUser(etName.getText()
                        .toString()));

            } else {
                Log.e(CLASS_TAG, "application could not launch");
                tearDown();
            }
        }
    };

    private class GooglePlayConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            tearDown();
            Log.d("onConnectionFailed", result.toString());
        }
    }

    private void tearDown() {
        Log.d(CLASS_TAG, "teardown");
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
                        Log.e(CLASS_TAG, "Exception while removing channel", e);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_iv_rock:
                sendMessage(CastMessages.messageToChoice(Constants.CHOICE_ROCK));
                setResult(R.string.msg_wait_oponent);
                break;

            case R.id.game_iv_paper:
                sendMessage(CastMessages.messageToChoice(Constants.CHOICE_PAPER));
                setResult(R.string.msg_wait_oponent);
                break;

            case R.id.game_iv_scissor:
                sendMessage(CastMessages.messageToChoice(Constants.CHOICE_SCISSOR));
                setResult(R.string.msg_wait_oponent);
                break;

            case R.id.game_iv_lizard:
                sendMessage(CastMessages.messageToChoice(Constants.CHOICE_LIZARD));
                setResult(R.string.msg_wait_oponent);
                break;

            case R.id.game_iv_spock:
                sendMessage(CastMessages.messageToChoice(Constants.CHOICE_SPOCK));
                setResult(R.string.msg_wait_oponent);
                break;

            default:
                break;
        }
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

    private void setMessaegStatus(String msg) {
        tvResult.setText(msg);
    }

    private String getNameUserRandom() {
        return getString(R.string.lbl_user)
                + new SimpleDateFormat("SSS").format(new Date());
    }
}
