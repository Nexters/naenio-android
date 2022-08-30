package com.nexters.teamvs.naenio.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

object ShareUtils {

    private const val webUrl = "https://naenio.shop/posts/"

    fun share(postId: Int, context: Context) {
        val shareLink = "${webUrl}${postId}"
        val shareWith = "ShareWith"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "네니오로 오세요~~")
            putExtra(Intent.EXTRA_TEXT, shareLink)
        }

        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, shareWith),
            null
        )
    }
}