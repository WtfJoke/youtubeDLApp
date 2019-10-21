package me.wessner.youtubedl

import android.os.Bundle
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
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
}
