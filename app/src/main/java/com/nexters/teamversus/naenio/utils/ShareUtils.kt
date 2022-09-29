package com.nexters.teamversus.naenio.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.nexters.teamversus.naenio.domain.model.Post

object ShareUtils {

    private const val webUrl = "https://naenio.shop/posts/"

    fun share(post: Post, context: Context) {
        val shareLink = "${webUrl}${post.id}"
        val shareWith = "ShareWith"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "[네니오] ${post.title}")
            putExtra(Intent.EXTRA_TEXT, shareLink)
        }

        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, shareWith),
            null
        )
    }
}