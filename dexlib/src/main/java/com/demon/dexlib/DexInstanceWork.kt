package com.demon.dexlib

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

/**
 * @author DeMon
 * Created on 9/12/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
class DexInstanceWork {

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DexInstanceWork()
        }
    }

    fun showNavToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context).load(url).into(imageView)
    }

    fun getClassName(): String {
        return this.javaClass.canonicalName
    }
}