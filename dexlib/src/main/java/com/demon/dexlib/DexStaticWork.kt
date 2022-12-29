package com.demon.dexlib

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.demon.dexlib.utils.BitmapUtils

/**
 * @author DeMon
 * Created on 8/12/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
object DexStaticWork {

    @JvmStatic
    fun showNavToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun loadImage(imageView: ImageView) {
        Glide.with(imageView.context).load(DexInit.imgUrl).into(imageView)
    }

    @JvmStatic
    fun getClassName(): String {
        return this.javaClass.canonicalName
    }

    @JvmStatic
    fun synthesisQRCode(imageView: ImageView) {
        BitmapUtils.synthesisQRCode(DexInit.qrUrl, imageView)
    }
}