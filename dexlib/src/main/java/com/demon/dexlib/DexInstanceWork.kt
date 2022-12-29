package com.demon.dexlib

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.demon.dexlib.utils.BitmapUtils

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

    fun getClassName(): String {
        return this.javaClass.canonicalName
    }

    fun showNavToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


    fun loadImage(imageView: ImageView) {
        Glide.with(imageView.context).load(DexInit.imgUrl).into(imageView)
    }

    fun synthesisQRCode(imageView: ImageView) {
        BitmapUtils.synthesisQRCode(DexInit.qrUrl, imageView)
    }
}