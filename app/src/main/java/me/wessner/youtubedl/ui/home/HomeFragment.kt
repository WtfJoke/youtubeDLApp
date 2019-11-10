package me.wessner.youtubedl.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.wessner.youtubedl.R
import java.io.File

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.text_home
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })
        root.downloadFAB.setOnClickListener { onClickDownload(it) }

        return root
    }

    private fun onClickDownload(view: View) {
        if (hasNoStoragePermission()) {
            askForStoragePermission()
            return
        }

        Toast.makeText(context, "Clicked on Download", Toast.LENGTH_LONG).show()
        val youtubeDLDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "youtubedl-android"
        )
        val editableUrl = view.rootView.videoLinkText.text.toString()
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
        Toast.makeText(context, "Need write access to storage for Download", Toast.LENGTH_LONG)
            .show()
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            100 // TODO replace this
        )
    }

    private fun hasNoStoragePermission(): Boolean {
        val storagePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return storagePermission == PackageManager.PERMISSION_DENIED
    }
}