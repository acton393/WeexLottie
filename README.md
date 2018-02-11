# WXLottie
[![Pod version](https://badge.fury.io/co/WXLottie.svg)](https://cocoapods.org/pods/WXLottie)
WXLottie is a [weex](https://github.com/apache/incubator-weex) component  plugin using [lottie](https://github.com/airbnb/lottie-ios)

weex-toolkit version： >= 1.2.0
WeexSDK ： >= 0.15.1

### WeexLottie

[lottie project](https://github.com/airbnb/lottie-ios)

[WeexLottie](https://github.com/acton393/WeexLottie)

![example](examples/gif/lottie.gif)
usage:

1. run start script in your terminal
2. front-end
  vue description:

```
<template>
  <div style='margin-top:50px;margin-left:150px'>
	<text style='margin-left:150px;border-width:2px;width:80px;' @click='click' :value="status"></text>
    <lottie src='http://127.0.0.1:12580/examples/animations/LottieWalkthrough.json' loop=true style='width:300px;height:300px'  ref='lottie'></lottie>
	<lottie src='http://127.0.0.1:12580/examples/animations/MotionCorpse-Jrcanest.json' loop=true style='width:300px;height:300px'  ref='lottie1'></lottie>
  </div>
</template>

<script>
  module.exports = {
    data: {
      play:false,
	  status:"play"
    },
    methods: {
      click : function(e) {
        var lottie = this.$refs.lottie;
		var lottie1 = this.$refs.lottie1;
        if (this.play) {
          this.play = false;
          lottie.reset();
		  lottie1.reset();
		  this.status = "play";
        }else {
          this.play = true;
          lottie.play();
		  lottie1.play();
		  this.status='stop'
        }
      }
    }
  }  
</script>
```

# integrate to your project
## WXLottie for iOS 
- integrate by weex

  ```
  weex plugin add weex-plugin-lottie
  ```
- integrate with cocoaPods
   - add the follow in your Podfile

      ```
      pod 'WXLottie'
      ```
   - register this weex component after init weexSDK env

## WXLottie for Android

-   integrate by weex

    ```
    weex plugin add weex-plugin-lottie
    ```

-   integrate with maven

    ```groovy
    compile 'org.asialee.weex:weexplugin:1.0.0'
    ```

-   integrate with source code:

    -   move `WXLottie.java` in your project

    -   integrate lottie: 

        ```
        compile "com.airbnb.android:lottie:2.3.1"
        ```



Then do not forget to regiter `WXLottie` in your `Application`:

```java
WXSDKEngine.registerComponent("lottie", WXLottie.class);
```


## WXLottie for Web

-   integrate by weex
    ```
    weex plugin add weex-plugin-lottie
    ```

-   integrate with npm

    ```
    npm i weex-plugin-lottie --save
    ```
Then do not forget to regiter `weex-plugin-lottie` in your web entry like blow:

```
import Vue from 'vue';

import weex from 'weex-vue-render';

import weex-plugin-lottie from 'weex-plugin-lottie';

weex.init(Vue);

weex.install(weex-plugin-lottie)

const App = require('./index.vue');
App.el = '#root';
new Vue(App);
```
   
  
