if(typeof (locamat) == "undefined") {
    var locamat = {};
        locamat.device = {};
} else if( typeof(locamat.device) == "undefined") {
    locamat.device = {};
}
locamat.device.mac ={
    getMac: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "PushPlugin", "register");
    }
};


