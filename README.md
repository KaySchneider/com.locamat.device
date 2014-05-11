com.locamat.device
==================

Cordova Plugin for Android to read the Mac Adress of the device
and to login an given user with oauth via googla account as described
in this blogpost:

https://android-developers.blogspot.de/2013/01/verifying-back-end-calls-from-android.html

### usage:

* window.plugins.locamat.getMac( successCallbackFunction, errorCallback );

-->receives the current mac address,  but be awere somethines you receive an error when the
wifi is turned off or other issues. Dont trust the mac ;)

* window.plugins.locamat.getToken( successCallbackFunction, errorCallback );

-->authenticate the current user of the device in "silent mode" so there isnt any popup from
google to authenticate the user, but your app needs the "Get Accounts" permission. If you have
this you can authenticate the user via oauth against google servers and you can use this to
identify the user in your backend to store user data or something else.





