package me.mutasem.simplevoicerecorder

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File
import java.util.*
import java.util.jar.Manifest

class Utils {
    companion object {
        fun createFile(): File {
            val dir =
                File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "recorder")
            if (!dir.exists())
                dir.mkdir()
            val f = File(dir.absolutePath + File.separator + UUID.randomUUID().toString() + ".m4a")
            return f
        }
    }
}