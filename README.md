# Game Closure Devkit Plugin: Geolocation

## Usage

Include it in the `manifest.json` file under the "addons" section for your game:

~~~
"addons": [
	"geoloc"
],
~~~

At the top of your game's `src/Application.js`:

~~~
import plugins.geoloc.install;
~~~

Now you can use the normal HTML5 `navigator.geolocation.getCurrentPosition` API:

~~~
navigator.geolocation.getCurrentPosition(bind(this, function(pos) {
	var lat = pos.coords.latitude;
	var lng = pos.coords.longitude;
	var acc = pos.coords.accuracy;
}), bind(this, function(err) {
	logger.log("FAIL:", err.code);
}), {
	'enableHighAccuracy': true
});
~~~

This gives you lat, long and accuracy.  Accuracy is in meters.

To enable higher-accuracy location like GPS, be sure to pass 'enableHighAccuracy': true as an option.

## Platform-specific notes

### Browsers

Does not seem to work for file:// paths, so you will want to test it in the `basil serve` mode to see it working.

On Chrome you will want to add the localhost:9200 "domain" to the geolocation permitted groups.  There will be a new icon on the right of the navigator bar you can interact with to add it.

On other browsers it will work the same as the normal HTML5 geolocation API.

### iOS

The GPS subsystem will be enabled by a popup that the user must accept.

Requests until they accept the popup will fail.

### Android

The GPS subsystem is often manually turned off to save battery, so the user will be prompted to switch to the Settings app to enable GPS for use in your app.  If they tap the OK button it will take them right to that settings pane.  Pressing the back button will take them directly back to your game.

Until the network/GPS location provider warms up you will receive the last known position of the device.  Requesting the position repeatedly over time will improve accuracy.

The location requests are canceled when the user places your app in the background, to conserve battery.
