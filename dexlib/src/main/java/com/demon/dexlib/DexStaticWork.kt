package com.demon.dexlib

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

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
    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context).load(url).into(imageView)
    }

    @JvmStatic
    fun getClassName(): String {
        return this.javaClass.canonicalName
    }
}