/*global cordova, module*/

module.exports = {
    greet: function(params, successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, 'PhoneCallInterruption', 'greet', [params]);
    },
    // setLoop: function(flag) {
    //   cordova.exec(function() {}, function() {}, 'RCPlayer', 'setLoopFromJS', [flag]);
    // },
    // setCurrentTime: function(seconds) {
    //   cordova.exec(function() {}, function() {}, 'RCPlayer', 'setCurrentTimeFromJS', [seconds]);
    // },
    // play: function(successCallback, errorCallback) {
    //   cordova.exec(successCallback, errorCallback, 'RCPlayer', 'play');
    // },
    // pause: function(successCallback, errorCallback) {
    //   cordova.exec(successCallback, errorCallback, 'RCPlayer', 'pause');
    // },
    // stop: function(successCallback, errorCallback) {
    //   cordova.exec(successCallback, errorCallback, 'RCPlayer', 'stop');
    // },
    // subscribe: function (onUpdate) {
    //   module.exports.updateCallback = onUpdate;
    // },
    // listen: function () {
    //   cordova.exec(module.exports.receiveCallbackFromNative, function (res) {
    //   }, 'RCPlayer', 'setWatcherFromJS', []);
    // },
    // receiveCallbackFromNative: function (messageFromNative) {
    //   module.exports.updateCallback(messageFromNative);
    //   cordova.exec(module.exports.receiveCallbackFromNative, function (res) {
    //   }, 'RCPlayer', 'setWatcherFromJS', []);
    // }
  };
  