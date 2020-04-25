package me.mutasem.voicerecorderexample

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import me.mutasem.simplevoicerecorder.RecorderView
import java.io.File

class MainActivity : AppCompatActivity() {
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
}
