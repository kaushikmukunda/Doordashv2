package km.com.doordash.nearbyRestaurants.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("cover_img_url")
    val coverImgUrl: String,
    val status: String
) {
    fun getDescription(maxLen: Int = 15): String {

        val sb = StringBuilder()
        val split = description.split(',')

        var prefix = ""
        for (desc in split) {
            if ((sb.length + desc.length) > maxLen) continue

            sb.append(prefix)
            sb.append(desc)
            prefix = ", "
        }

        return sb.toString()
    }
}

