Cordova Audio Interruption
=======================

It's a simple Apache Cordova plugin to simplify handling audio interruptions in iOS and Android devices, 
this plugin will be useful if you want to resume audio in your application after phone call or any other audio interruption in the os.


## Install

    $ cordova plugin add cordova-plugin-audio-interruption


## Quick Example

    let playingWasInterrupted = false;
    let audio = new Audio();

    AudioInterruption.addListener(status => {
        switch (status) {
          case 'INTERRUPTION_BEGIN':
            if (!audio.paused) {
              playingWasInterrupted = true;
              audio.pause();
            } 
            break;
          case 'INTERRUPTION_ENDED':
            if (playingWasInterrupted) {
              playingWasInterrupted = false;
              audio.play();
            }
            break;
        }
    });


## Supported platforms

- Android 2.3.3 or higher
- iOS 9.0 or higher

## License

Cordova PhoneCall Trap is released under the [MIT License](http://www.opensource.org/licenses/MIT).
