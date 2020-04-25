# SimpleVoiceRecorder
Simple voice recorder view for android
# Usage
## Gradle
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
 
 dependencies {
       implementation 'com.github.mutasemhajhasan:SimpleVoiceRecorder:1.0.0'
}
```

## AndroidManifest.xml
```xml
 <uses-permission android:name="android.permission.RECORD_AUDIO" />
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
### Note
Starting from android M you need to request permissions at runtime
```kotlin
 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )
                , RC
            )
        }
```
## activity.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center_horizontal"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="30dp"
        android:text="Tap on the icon to (start / stop) recording"
        android:textSize="16sp"
        android:textStyle="bold" />

    <me.mutasem.simplevoicerecorder.RecorderView
        android:id="@+id/recorderView"
        android:layout_width="90dp"
        android:layout_height="90dp"
        android:padding="0dp"
        app:recordIconRes="@drawable/record"
        app:stopIconRes="@drawable/stop" />
</LinearLayout>
```
## activity.kt
```kotlin
    private val RC = 1010
    lateinit var recorderView: RecorderView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recorderView = findViewById(R.id.recorderView)
        recorderView.setSavePath(
            "${externalCacheDir?.absolutePath}/recordTest.m4a"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )
                , RC
            )
        }
        recorderView.setRecordingListener(object : RecorderView.RecordingListener {
            override fun onRecordingStarted() {
                Toast.makeText(this@MainActivity, "recording started", Toast.LENGTH_SHORT).show()
            }

            override fun onRecordingCompleted(file: File?) {
                Toast.makeText(
                    this@MainActivity,
                    "recording saved to${file?.absolutePath}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onError(error: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "recording error${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    override fun onDestroy() {
        recorderView.release()
        super.onDestroy()
    }
```
The default output format is "MediaRecorder.OutputFormat.MPEG_4"

The default audio encoder is "MediaRecorder.AudioEncoder.AAC"

The project is built using android <a href="https://developer.android.com/reference/android/media/MediaRecorder">MediaRecorder</a> so the same limitations of the MediaRecorder are applied to the project
