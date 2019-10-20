package me.wessner.youtubedl

import org.json.JSONObject


data class AudioVideoFormat(val qualityLabel: String, val width: Int, val height: Int) {

    constructor(json: JSONObject) : this(
        json.getString("qualityLabel"),
        json.getInt("width"),
        json.getInt("height")
    )

    fun type(): String {
        return "audio/video"
    }
}
