var locamatDeviceMac = function() {

};

locamatDeviceMac.prototype.getMac = function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "GetMacAdress", "getmac", []);
};


if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.locamat) {
    window.plugins.locamat = new locamatDeviceMac();
}

if (module.exports) {
    module.exports = locamatDeviceMac;
}