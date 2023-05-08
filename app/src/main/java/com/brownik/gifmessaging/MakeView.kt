package com.brownik.gifmessaging

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.utils.px
import com.giphy.sdk.ui.views.GPHMediaView

object MakeView {

    private fun makeUrl(id: String) = "https://media3.giphy.com/media/${id}/giphy.gif"

    fun makeGPHMediaView(context: Context, media: Media): GPHMediaView {
        val view = GPHMediaView(context)
        val layoutParams = ConstraintLayout.LayoutParams(
            100.px,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
        ).apply {
            startToStart = ConstraintSet.PARENT_ID
        }
        view.apply {
            cornerRadius = 4.px.toFloat()
            setMedia(media)
            isBackgroundVisible = false
        }.layoutParams = layoutParams
        return view
    }

    fun makeImageView(context: Context, media: Media): ImageView {
        val view = ImageView(context)
        val layoutParams = ConstraintLayout.LayoutParams(
            100.px,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
        ).apply {
            startToStart = ConstraintSet.PARENT_ID
            endToEnd = ConstraintSet.PARENT_ID
        }
        view.apply {
            Glide.with(context).asGif().load(media.images.original?.gifUrl).into(this)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }.layoutParams = layoutParams
        return view
    }

    fun makeSimpleDraweeView(context: Context, media: Media): SimpleDraweeView {
        val view = SimpleDraweeView(context)
        val uri = Uri.parse(media.images.original?.gifUrl)
        val layoutParams = ConstraintLayout.LayoutParams(
            100.px,
            ConstraintLayout.LayoutParams.MATCH_PARENT,
        ).apply {
            endToEnd = ConstraintSet.PARENT_ID
        }
        val controller = Fresco.newDraweeControllerBuilder().apply {
            setUri(uri)
            autoPlayAnimations = true
        }.build()
        view.controller = controller
        view.layoutParams = layoutParams
        return view
    }
}