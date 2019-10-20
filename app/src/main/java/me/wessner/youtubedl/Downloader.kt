package me.wessner.youtubedl

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder


class Downloader {


    companion object {
        private val CONFIG_START = "ytplayer.config = "
        private val CONFIG_END = ";ytplayer.load"

        fun getVideo(videoId: String) {
            val page = loadPage(videoId)
            val configPart = parseConfigPagePart(page)
            val streamMapString = getStreamMapString(configPart)
            val formats = getFormats(streamMapString)
            formats.toString()
        }

        private fun getFormats(streamMapString: String): List<AudioVideoFormat> {
            val formats = mutableListOf<AudioVideoFormat>()
            formats.add(AudioVideoFormat(splitQuery(streamMapString)))
            return formats
        }

        private fun getStreamMapString(configPart: String): String {
            val config = JSONObject(configPart);
            val args = config.getJSONObject("args");

            val streamMapString = args.getString("url_encoded_fmt_stream_map");
            // val adaptive_fmts = args.getString("adaptive_fmts")

            return streamMapString
        }

        private fun parseConfigPagePart(page: String): String {
            val start = page.indexOf(CONFIG_START)
            val end = page.indexOf(CONFIG_END)
            val config = page.substring(start + CONFIG_START.length, end)
            return config
        }

        private fun loadPage(videoUrl: String): String {
            val connection = URL(videoUrl).openConnection() as HttpURLConnection;
            connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36"
            );
            connection.setRequestProperty("Accept-Language", "en-US,en;");

            try {
                val reader = connection.inputStream.bufferedReader()
                reader.use { return it.readText() }
            } finally {
                connection.disconnect()
            }
        }

        private fun splitQuery(requestString: String): JSONObject {
            val queryPairs = JSONObject()
            try {
                val pairs = requestString.split("&".toRegex())
                for (pair in pairs) {
                    for (commaPair in pair.split(",".toRegex())) {
                        val idx = commaPair.indexOf("=")
                        queryPairs.put(
                            URLDecoder.decode(commaPair.substring(0, idx), "UTF-8"),
                            URLDecoder.decode(commaPair.substring(idx + 1), "UTF-8")
                        )
                    }
                }
            } catch (e: Exception) {
                System.err.println(requestString)
                e.printStackTrace()
            }

            return queryPairs
        }
    }
}