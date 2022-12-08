package com.demon.dexlib

import android.content.Context
import android.widget.ImageView

/**
 * @author DeMon
 * Created on 8/12/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
interface IDexWork {

    fun showNavToast(context: Context, text: String)

    fun loadImage(imageView: ImageView, url: String)

    fun getClassName(): String
}