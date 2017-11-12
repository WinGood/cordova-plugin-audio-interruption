/*global cordova, module*/

module.exports = {
  addListener: function(successCallback, errorCallback) {
    var errorCallback = errorCallback || function() {};
    cordova.exec(successCallback, errorCallback, 'AudioInterruption', 'addListener');
  }
};
  