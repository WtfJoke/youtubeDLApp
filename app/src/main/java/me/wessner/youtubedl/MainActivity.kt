package me.wessner.youtubedl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickDownload(view: View){
        Toast.makeText(this, "Clicked on Download", Toast.LENGTH_LONG).show()
    }
}
