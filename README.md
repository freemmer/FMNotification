![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
[![](https://jitpack.io/v/freemmer/FMNotification.svg)](https://jitpack.io/#freemmer/FMNotification)

# FMNotification
For FCM Receive and Notification on Android. (Kotlin)
+ This project is based on 'https://github.com/firebase/quickstart-android/tree/master/messaging'

## Demo ScreenShot
![Screenshot](https://github.com/freemmer/FMNotification/blob/master/Screenshots/screenshots.gif)

## Setup
Project build.gradle
```Groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

App build.gradle
```Groovy
dependencies {
    implementation 'com.github.freemmer:FMNotification:1.0.1'
}
```

## How To Use

+ Application Class
```Kotlin
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // When only payload type is used like 'GCM'
        FMNotification.PAYLOAD_TITLE_KEY = "title"
        FMNotification.PAYLOAD_BODY_KEY = "body"

        // In case automatically create default channel
        FMNotification.initialize(this)
        
        // In case manually create default channel
        //FMNotification.instance(this).createChannel("default_channel_id", "Default Channel")
    }
}
```

+ Process Activity when notification message is tapped.
```Kotlin
class MainActivity : FMNotificationAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logTokenButton.setOnClickListener {
            // Get push token
            FMNotification.getPushToken { isSuccessful: Boolean, token: String? ->
                if (!isSuccessful) {
                    Log.w(TAG, "getInstanceId failed")
                } else {
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClickedNotification(bundle: Bundle) {
        // In case a notification message is tapped.
        // Handle possible data accompanying notification message.
        for (key in bundle.keySet()) {
            val value = bundle.get(key)
            Log.d(TAG, "Key: $key Value: $value")
        }
    }
}
```

## Activity Type
+ FMNotificationActivity : extends Activity
+ FMNotificationAppCompatActivity : extends AppCompatActivity
+ FMNotificationFragmentActivity : extends FragmentActivity

## License
```code
This software is licensed under the [Apache 2 license](LICENSE), quoted below.

Copyright 2019 freemmer. <http://freemmer.tistory.com>

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this project except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```
