package com.demon.dexlib.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Environment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.demon.dexlib.DexInit
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*


/**
 * @author DeMon
 * Created on 22/11/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
object BitmapUtils {

    /**
     * 将Bitmap保存到沙盒环境中
     */
    fun saveBitmap(context: Context, bitmap: Bitmap?): File? {
        bitmap ?: return null
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")
        return if (save(bitmap, file, Bitmap.CompressFormat.JPEG, true)) {
            file
        } else {
            null
        }
    }

    fun save(src: Bitmap, file: File, format: Bitmap.CompressFormat?, recycle: Boolean): Boolean {
        var os: OutputStream? = null
        var ret = false
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            ret = src.compress(format, 100, os)
            if (recycle && !src.isRecycled) src.recycle()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ret
    }

    /**
     *
     * @param background  背景
     * @param foreground 前景
     * @param x  合成在背景上的x坐标
     * @param y  合成在背景上的y坐标
     * @param isRecycle 是否回收背景&前景Bitmap
     */
    fun synthesis(
        background: Bitmap?,
        foreground: Bitmap?,
        x: Float = 0f,
        y: Float = 0f,
        isRecycle: Boolean = false
    ): Bitmap? {
        var newBitmap: Bitmap? = null
        try {
            background ?: return null
            foreground ?: return null
            val bgWidth = background.width
            val bgHeight = background.height
            newBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(newBitmap)
            canvas.drawBitmap(background, 0f, 0f, null)
            canvas.drawBitmap(foreground, x, y, null)
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (isRecycle) {
                background?.recycle()
                foreground?.recycle()
            }
        }
        return newBitmap
    }


    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  宽度
     * @param heightPix 高度
     * @return Bitmap
     */
    fun createQRCode(content: String?, widthPix: Int, heightPix: Int): Bitmap? {
        try {
            if (content.isNullOrEmpty()) {
                return null
            }
            // 配置参数
            val hints = mutableMapOf<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            // 容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            // 空白边距设置
            // hints[EncodeHintType.MARGIN] = 5
            // 图像数据转换，使用了矩阵转换
            val bitMatrix: BitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = -0x1000000
                    } else {
                        pixels[y * widthPix + x] = -0x1
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            val bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)
            //压缩，减少内存损耗
            return compressBitmap(bitmap, 50)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 质量压缩
     */
    fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap? {
        val bos = ByteArrayOutputStream()
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos)
            val bytes: ByteArray = bos.toByteArray()
            return BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bos.flush()
            bos.close()
        }
        return null
    }


    fun synthesisQRCode(url: String, img: ImageView) {
        DexInit.scopeIO.launch {
            val bitmap = DexInit.imgUrl.saveImageBitmap(DexInit.mContext)
            val qrBitmap = createQRCode(url, 300, 300)
            val synthesisBitmap = synthesis(bitmap, qrBitmap, 80.0f, 80.0f)
            val file = saveBitmap(DexInit.mContext, synthesisBitmap)
            file.saveToAlbum()
            withContext(Dispatchers.Main) {
                Glide.with(img.context).load(file).into(img)
            }
        }
    }


}