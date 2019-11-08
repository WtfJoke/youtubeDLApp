package me.wessner.youtubedl

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_DENIED
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        if (hasNoStoragePermission()) {
            askForStoragePermission()
            return
        }

        Toast.makeText(this, "Clicked on Download", Toast.LENGTH_LONG).show()
        val youtubeDLDir = File(
            getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS),
            "youtubedl-android"
        )
        val editableUrl = videoLinkText.text.toString()
        val url = editableUrl.ifBlank { getString(R.string.sample_link) }

        val request = YoutubeDLRequest(url)
        request.setOption("-o", youtubeDLDir.absolutePath + "/%(title)s.%(ext)s")
        GlobalScope.launch { execute(request) }
    }

    private suspend fun execute(request: YoutubeDLRequest) =
        withContext(Dispatchers.IO) {
            YoutubeDL.getInstance()
                .execute(request) { progress, etaInSeconds ->
                    Log.d(
                        "YTDL",
                        "$progress% (ETA $etaInSeconds seconds)"
                    )
                }
        }


    private fun askForStoragePermission() {
        Toast.makeText(this, "Need write access to storage for Download", Toast.LENGTH_LONG).show()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE),
            100 // TODO replace this
        )
    }

    private fun hasNoStoragePermission(): Boolean {
        val storagePermission = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        return storagePermission == PERMISSION_DENIED
    }
}
