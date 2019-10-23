package me.wessner.youtubedl

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            YoutubeDL.getInstance().init(application)
        } catch (e: YoutubeDLException) {
            Log.e("YTDL", "failed to initialize youtubedl-android", e)
        }
    }


    fun onClickDownload(view: View) {
        val hasNoStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        if (hasNoStoragePermission) {
            askForStoragePermission()
            return
        }

        Toast.makeText(this, "Clicked on Download", Toast.LENGTH_LONG).show()
        val youtubeDLDir = File(
            getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS),
            "youtubedl-android"
        )
        val request = YoutubeDLRequest("https://vimeo.com/22439234")
        request.setOption("-o", youtubeDLDir.absolutePath + "/%(title)s.%(ext)s")
        YoutubeDL.getInstance()
            .execute(request) { progress, etaInSeconds -> println("$progress% (ETA $etaInSeconds seconds)") }
    }

    private fun askForStoragePermission() {
        Toast.makeText(this, "Need write access to storage for Download", Toast.LENGTH_LONG).show()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            100 // TODO replace this
        )
    }
}
