package com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.models

import android.content.Context
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder
import android.util.DisplayMetrics
import android.view.WindowManager

@EpoxyModelClass(layout = R.layout.photo_fullscreen_item_view)
abstract class PhotoFullscreenItemModel : EpoxyModelWithHolder<PhotoFullscreenItemHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String

    override fun bind(holder: PhotoFullscreenItemHolder) {
        setImageHeight(holder.photo)

        if (photoUrl != "") {
            Glide.with(holder.photo.context)
                .load(photoUrl)
                .apply(RequestOptions.centerInsideTransform())
                .into(holder.photo)
        }
    }

    private fun setImageHeight(imageView: ImageView) {
        val imageHeight = getScreenHeight(imageView) - getStatusBarHeight(imageView)
        imageView.layoutParams.height = imageHeight
    }

    private fun getScreenHeight(imageView: ImageView): Int {
        val wm = imageView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val display = wm?.defaultDisplay
        val metrics = DisplayMetrics()
        display?.getMetrics(metrics)
        return metrics.heightPixels
    }

    private fun getStatusBarHeight(imageView: ImageView): Int {
        var statusBarHeight = 0
        val resourceId = imageView.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = imageView.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}

class PhotoFullscreenItemHolder : KotlinHolder() {
    val photo by bind<ImageView>(R.id.photo_fullscreen_item_image)
}