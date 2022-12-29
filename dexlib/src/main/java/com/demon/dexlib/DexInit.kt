package com.demon.dexlib

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * @author DeMon
 * Created on 29/12/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
@SuppressLint("StaticFieldLeak")
object DexInit {

    @JvmStatic
    val scopeIO = CoroutineScope((SupervisorJob() + Dispatchers.IO))

    @JvmStatic
    lateinit var mContext: Context

    //背景图链接
    @JvmStatic
    var imgUrl: String = ""

    //二维码链接
    @JvmStatic
    var qrUrl: String = ""

    @JvmStatic
    fun init(context: Context, imgUrl: String, qrUrl: String) {
        mContext = context
        this.imgUrl = imgUrl
        this.qrUrl = qrUrl
    }


}