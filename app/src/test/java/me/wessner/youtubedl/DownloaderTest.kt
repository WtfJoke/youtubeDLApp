package me.wessner.youtubedl

import org.junit.Assert
import org.junit.Test

class DownloaderTest {

    @Test
    fun testGetVideo() {
        Assert.assertEquals("", Downloader.getVideo("https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
    }
}