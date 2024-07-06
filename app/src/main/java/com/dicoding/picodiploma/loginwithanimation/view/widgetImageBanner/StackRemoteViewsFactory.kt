package com.dicoding.picodiploma.loginwithanimation.view.widgetImageBanner

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.di.Injection
import kotlinx.coroutines.runBlocking

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = mutableListOf<Bitmap>()

    override fun onCreate() {}

    override fun onDataSetChanged() = runBlocking {
        try {
            val stories = Injection.provideRepository(mContext).getStories()
//            if (stories.error == true) return@runBlocking
//            val bitmap = stories.listStory!!.map {
//                Glide.with(mContext)
//                    .asBitmap()
//                    .load(it!!.photoUrl)
//                    .override(256, 2356)
//                    .submit().get()
//            }
            mWidgetItems.clear()
//            mWidgetItems.addAll(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ImageBannerWidget.triggerDataChanged(mContext)
    }

    override fun onDestroy() {
        mWidgetItems.clear()
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])
        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

}