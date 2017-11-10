/*global cordova, module*/

module.exports = {
  onCall: function(successCallback, errorCallback) {
    var errorCallback = errorCallback || function() {};
    cordova.exec(successCallback, errorCallback, 'PhoneCallInterruption', 'onCall');
  }
};
  