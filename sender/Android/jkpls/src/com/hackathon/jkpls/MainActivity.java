package com.hackathon.jkpls;

import java.io.IOException;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.ApplicationConnectionResult;
import com.google.android.gms.cast.Cast.MessageReceivedCallback;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends ActionBarActivity {
	private final String CLASS_TAG = MainActivity.class.getSimpleName();

	private MediaRouter mMediaRouter;
	private MediaRouteSelector mMediaRouteSelector;
	private MediaRouter.Callback mMediaRouterCallback;
	private CastDevice mSelectedDevice;
	private GoogleApiClient mGoogleApiClient;
	private Cast.Listener mCastListener;
	private boolean wasLaunched;
	private boolean mWaitingForReconnect;
	private String mSessionId;
	private MyMessageReceivedCallback mMyMessageReceivedCallbacks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initializeMediaRouter();

		Button b1 = (Button) findViewById(R.id.button1);

		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendMessage("Litle Reds Motherfucks");

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem mediaRouteMenuItem = menu.findItem(R.id.action_media_route);
		MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat
				.getActionProvider(mediaRouteMenuItem);
		// Set the MediaRouteActionProvider selector for device discovery.
		mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);

		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		startMediaRouterDiscovery();
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
					// tearDown();
				}

			};
			// Connect to Google Play services
			Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
					.builder(mSelectedDevice, mCastListener);
			// Flow 4 - Sender app creates a GoogleApiClient
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addApi(Cast.API, apiOptionsBuilder.build())
					// Flow 5 - Sender app connects the GoogleApiClient
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
			// tearDown();
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
			Toast.makeText(getApplicationContext(),
					castDevice.getFriendlyName() + ":" + message,
					Toast.LENGTH_LONG).show();
		}

	}

	private class GooglePlayConnectionCallbacks implements
			GoogleApiClient.ConnectionCallbacks {

		@Override
		public void onConnected(Bundle connectionHint) {
			// Flow 6 - SDK confirms that GoogleApiClient is connected
			try {
				if (mWaitingForReconnect) {
					mWaitingForReconnect = false;

					// Check if the receiver app is still running
					if ((connectionHint != null)
							&& connectionHint
									.getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
						Log.d(CLASS_TAG, "App  is no longer running");
						// tearDown();
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
					// Flow 7 - Sender app launches the receiver app
					Cast.CastApi.launchApplication(mGoogleApiClient,
							getString(R.string.cast_app_id), false)
					// Flow 8 - SDK confirms that the receiver app is connected
							.setResultCallback(castResultCallback);
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

	ResultCallback<Cast.ApplicationConnectionResult> castResultCallback = new ResultCallback<Cast.ApplicationConnectionResult>() {

		@Override
		public void onResult(ApplicationConnectionResult result) {
			Status status = result.getStatus();
			Log.d(CLASS_TAG,
					"ApplicationConnectionResultCallback.onResult: statusCode"
							+ status.getStatusCode());

			if (status.isSuccess()) {
				mSessionId = result.getSessionId();

				wasLaunched = result.getWasLaunched();
				// Flow 9 - Sender app creates a communication channel:
				mMyMessageReceivedCallbacks = new MyMessageReceivedCallback();

				try {
					Cast.CastApi.setMessageReceivedCallbacks(mGoogleApiClient,
							mMyMessageReceivedCallbacks.getNamespace(),
							mMyMessageReceivedCallbacks);
				} catch (IOException e) {
					Log.e(CLASS_TAG, "Exception while creating channel", e);
				}

			} else {
				Log.e(CLASS_TAG, "application could not launch");
				// tearDown();
			}
		}
	};

	// Google Play services callbacks
	private class GooglePlayConnectionFailedListener implements
			GoogleApiClient.OnConnectionFailedListener {
		@Override
		public void onConnectionFailed(ConnectionResult result) {
			// tearDown();
		}
	}
}
