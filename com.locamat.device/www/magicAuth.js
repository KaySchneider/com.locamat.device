var magicAuth = {};

magicAuth.getMac = function(successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "PushPlugin", "register");
};

