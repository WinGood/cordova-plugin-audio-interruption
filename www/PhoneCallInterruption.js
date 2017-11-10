/*global cordova, module*/

module.exports = {
  onCalling: function(successCallback, errorCallback) {
    var errorCallback = errorCallback || function() {};
    cordova.exec(successCallback, errorCallback, 'PhoneCallInterruption', 'onCalling');
  }
};
  