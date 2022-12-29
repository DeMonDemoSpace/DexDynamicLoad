package com.demon.dexlib.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

/**
 * @author DeMon
 * Created on 21/11/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */

/**
 * 下载图片到缓存，此方法必须在子线程中执行
 *
 * @return 返回缓存文件File
 */
suspend fun String?.saveImage(context: Context): File? {
    this ?: return null
    val target = Glide.with(context).asFile().load(this).listener(object : RequestListener<File> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
            Log.e("GlideExt", "saveImage onLoadFailed", e)
            return false
        }

        override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            Log.i("GlideExt", "saveImage onResourceReady: resource=${resource?.absolutePath}")
            return false
        }
    }).submit()
    return target.get()
}


/**
 * 下载图片到缓存，此方法必须在子线程中执行
 *
 * @return 返回Bitmap
 */
suspend fun String?.saveImageBitmap(context: Context): Bitmap? {
    this ?: return null
    val target = Glide.with(context).asBitmap().load(this).listener(object : RequestListener<Bitmap> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
            Log.e("GlideExt", "saveImageBitmap onLoadFailed", e)
            return false
        }

        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            Log.i("GlideExt", "saveImageBitmap onResourceReady: resource")
            return false
        }

    }).submit()
    return target.get()
}