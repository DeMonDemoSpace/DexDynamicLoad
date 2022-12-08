package com.demon.dexdynamicload

import android.content.Context
import dalvik.system.DexClassLoader
import java.io.*

/**
 * @author DeMon
 * Created on 8/12/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
object Utils {

    /**
     * 复制dex到沙盒中
     */
    fun copyDex(context: Context, dexName: String) {
        val cacheFile = File(context.filesDir, "dex")
        if (!cacheFile.exists()) {
            cacheFile.mkdirs()
        }
        val internalPath = cacheFile.absolutePath + File.separator + dexName
        val desFile = File(internalPath)
        if (!desFile.exists()) {
            desFile.createNewFile()
        }
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {
            `in` = context.applicationContext.assets.open(dexName)
            out = FileOutputStream(desFile.absolutePath)
            val bytes = ByteArray(1024)
            var len = 0
            while (`in`.read(bytes).also { len = it } != -1) out.write(bytes, 0, len)
            out.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `in`?.close()
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * 加载dex
     */
    fun loadDexClass(context: Context, dexName: String): DexClassLoader? {
        try {
            //下面开始加载dex class
            //1.待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限,
            //2.解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写
            //3.指向包含本地库(so)的文件夹路径，可以设为null
            //4.父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
            val cacheFile = File(context.filesDir, "dex")
            val internalPath = cacheFile.absolutePath + File.separator + dexName
            return DexClassLoader(internalPath, cacheFile.absolutePath, null, context.classLoader)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}