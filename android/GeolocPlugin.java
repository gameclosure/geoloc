package com.tealeaf.plugin.plugins;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.tealeaf.EventQueue;
import com.tealeaf.GLSurfaceView;
import com.tealeaf.TeaLeaf;
import com.tealeaf.logger;
import com.tealeaf.event.PluginEvent;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import java.util.HashMap;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.tealeaf.plugin.IPlugin;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.tealeaf.EventQueue;
import com.tealeaf.event.*;

public class GeolocPlugin implements IPlugin {
	public class GeolocEvent extends com.tealeaf.event.Event {
		boolean failed;
		double longitude, latitude;

		public GeolocEvent() {
			super("geoloc");
			this.failed = true;
		}

		public GeolocEvent(double longitude, double latitude) {
			super("geoloc");
			this.failed = false;
			this.longitude = longitude;
			this.latitude = latitude;
		}
	}

	public class MyLocationListener implements LocationListener {
		public boolean enabled;
		public int callback;

		@Override
			public void onLocationChanged(Location loc) {
				logger.log("{geoloc} Received location changed event");

				// If position is enabled,
				if (enabled) {
					EventQueue.pushEvent(new GeolocEvent(loc.getLongitude(), loc.getLatitude()));
				} else {
					EventQueue.pushEvent(new GeolocEvent());
				}
			}

		@Override
			public void onProviderDisabled(String provider) {
				enabled = false;
				logger.log("{geoloc} Location provider disabled: ", provider);
			}

		@Override
			public void onProviderEnabled(String provider) {
				enabled = true;
				logger.log("{geoloc} Location provider enabled: ", provider);
			}

		@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
	}

	MyLocationListener _listener;
	Context _ctx;
	LocationManager _mgr;

	public GeolocPlugin() {
		_listener = new MyLocationListener();
	}

	public void onCreateApplication(Context applicationContext) {
		_ctx = applicationContext;
	}

	public void onCreate(Activity activity, Bundle savedInstanceState) {
		_mgr = (LocationManager)_ctx.getSystemService(Context.LOCATION_SERVICE);
		_listener.enabled = _mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (_listener.enabled) {
			logger.log("{geoloc} GPS provider is initially enabled.");
		} else {
			logger.log("{geoloc} GPS provider is initially DISABLED.");
		}
	}

	public void onResume() {
	}

	public void onStart() {
	}

	public void onPause() {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public void onNewIntent(Intent intent) {
	}

	public void setInstallReferrer(String referrer) {
	}

	public void onActivityResult(Integer request, Integer result, Intent data) {
	}

	public void onRequest(String jsonData) {
		logger.log("{geoloc} Got position request");

		try {
			if (_listener.enabled) {
				// Start async request for GPS position update
				_mgr.requestSingleUpdate(LocationManager.GPS_PROVIDER, _listener, null);
			} else {
				EventQueue.pushEvent(new GeolocEvent());
			}
		} catch (Exception e) {
			logger.log(e);
		}
	}

	public void logError(String error) {
	}

	public boolean consumeOnBackPressed() {
		return false;
	}

	public void onBackPressed() {
	}
}

