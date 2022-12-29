package com.demon.dexlib.utils

import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import com.demon.dexlib.DexInit
import java.io.*
import java.net.URLConnection

/**
 * @author DeMon
 * Created on 29/12/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
/**
 * 根据文件名获取MimeType
 */
fun String.getMimeTypeByFileName(): String {
    var mimeType = ""
    runCatching {
        mimeType = URLConnection.getFileNameMap().getContentTypeFor(this)
    }.onFailure {
        it.printStackTrace()
    }
    return mimeType
}
/**
 * 是否是以下作用域父文件夹内的文件，如华为手机：
 * 手机内部存储：/data/user/0/
 * 手机外部存储：/storage/emulated/0/Android/data/
 * ps:不同手机可能不一致，主要是看filesDir，getExternalFilesDir的返回结果
 */
fun String?.isAndroidDataFile(): Boolean {
    this ?: return false
    //内部存储
    val filesDirString = DexInit.mContext.filesDir.parent
    Log.i("FileExt", "isAndroidDataFile: file=$this,filesDirString=$filesDirString")
    if (!filesDirString.isNullOrEmpty()) {
        val dir = File(filesDirString).parent
        if (!dir.isNullOrEmpty() && this.contains(dir)) {
            return File(this).exists()
        }
    }
    //外部存储
    val externalFilesDirString = DexInit.mContext.getExternalFilesDir(null)?.parent
    Log.i("FileExt", "isAndroidDataFile: file=$this,externalFilesDirString=$externalFilesDirString")
    if (!externalFilesDirString.isNullOrEmpty()) {
        val dir = File(externalFilesDirString).parent
        if (!dir.isNullOrEmpty() && this.contains(dir)) {
            return File(this).exists()
        }
    }
    return false
}
/**
 * 将图片保存至相册，兼容AndroidQ
 *
 * @param name 图片名称
 */
fun File?.saveToAlbum(name: String? = null): Boolean {
    if (this == null || !exists()) return false
    Log.i("FileExt", "saveToAlbum: ${this.absolutePath}")
    runCatching {
        val values = ContentValues()
        val resolver = DexInit.mContext.contentResolver
        val fileName = name?.run {
            this
        } ?: run {
            this.name
        }
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        values.put(MediaStore.MediaColumns.MIME_TYPE, fileName.getMimeTypeByFileName())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //AndroidQ更新图库需要将拍照后保存至沙盒的原图copy到系统多媒体
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            val saveUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (saveUri != null) {
                val out = resolver.openOutputStream(saveUri)
                val input = FileInputStream(this)
                if (out != null) {
                    FileUtils.copy(input, out) //直接调用系统方法保存
                }
                out?.close()
                input.close()
            }
        } else {
            //作用域内的文件多媒体无法显示
            //会抛异常：UNIQUE constraint failed: files._data (code 2067)
            if (this.absolutePath.isAndroidDataFile()) {
                val file = getFileInPublicDir(fileName, Environment.DIRECTORY_PICTURES)
                //AndroidQ以下作用域的需要将文件复制到公共目录，再插入多媒体中
                this.copyFile(file)
                values.put(MediaStore.MediaColumns.DATA, file.absolutePath)
            } else {
                //AndroidQ以下非作用域的直接将文件路径插入多媒体中即可
                values.put(MediaStore.MediaColumns.DATA, this.absolutePath)
            }

            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
        return true
    }.onFailure {
        it.printStackTrace()
    }
    return false
}


/**
 * new一个用于保存在公有目录的文件，不会创建空文件，用于拍照，裁剪路径
 * 公有目录无需读写权限也可操作媒体文件：图片，适配，音频
 * @param name 文件名
 * @param dir 公有文件目录
 *  @see android.os.Environment.DIRECTORY_DOWNLOADS
 * @see android.os.Environment.DIRECTORY_DCIM,
 * @see android.os.Environment.DIRECTORY_MUSIC,
 * @see android.os.Environment.DIRECTORY_PODCASTS,
 * @see android.os.Environment.DIRECTORY_RINGTONES,
 * @see android.os.Environment.DIRECTORY_ALARMS,
 * @see android.os.Environment.DIRECTORY_NOTIFICATIONS,
 * @see android.os.Environment.DIRECTORY_PICTURES,
 * @see android.os.Environment.DIRECTORY_MOVIES,
 * @see android.os.Environment.DIRECTORY_DOCUMENTS
 */
fun getFileInPublicDir(name: String, dir: String = Environment.DIRECTORY_DOCUMENTS): File {
    return File("${Environment.getExternalStorageDirectory().absolutePath}/${dir}", name)
}


/**
 * 复制文件
 */
fun File?.copyFile(dest: File) {
    this ?: return
    var input: InputStream? = null
    var output: OutputStream? = null
    try {
        if (!dest.exists()) {
            dest.createNewFile()
        }
        input = FileInputStream(this)
        output = FileOutputStream(dest)
        val buf = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buf).also { bytesRead = it } > 0) {
            output.write(buf, 0, bytesRead)
        }
        output.flush()
        Log.i("FileExt", "copyFile succeed: ${dest.absolutePath}")
    } catch (e: Exception) {
        Log.d("FileExt", "copyFile error: " + e.message)
        e.printStackTrace()
    } finally {
        try {
            input?.close()
            output?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
