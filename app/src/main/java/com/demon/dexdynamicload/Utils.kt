package com.demon.dexdynamicload

import android.content.Context
import android.content.Intent
import android.util.ArrayMap
import dalvik.system.DexClassLoader
import java.io.*
import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


/**
 * @author DeMon
 * Created on 8/12/22.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
object Utils {

    var loader: DexClassLoader? = null

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

    /**
     * 替换 LoadedApk 中的 类加载器 ClassLoader
     *
     *  @param context
     *  @param loader 动态加载dex的ClassLoader
     */
    fun replaceLoadedApkClassLoader(context: Context, loader: DexClassLoader) {
        // I. 获取 ActivityThread 实例对象
        // 获取 ActivityThread 字节码类 , 这里可以使用自定义的类加载器加载
        // 原因是 基于 双亲委派机制 , 自定义的 DexClassLoader 无法加载 , 但是其父类可以加载
        // 即使父类不可加载 , 父类的父类也可以加载
        var activityThreadClass: Class<*>? = null
        try {
            activityThreadClass = loader.loadClass("android.app.ActivityThread")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        // 获取 ActivityThread 中的 sCurrentActivityThread 成员
        // 获取的字段如下 :
        // private static volatile ActivityThread sCurrentActivityThread;
        // 获取字段的方法如下 :
        // public static ActivityThread currentActivityThread() {return sCurrentActivityThread;}
        var currentActivityThreadMethod: Method? = null
        try {
            currentActivityThreadMethod = activityThreadClass?.getDeclaredMethod("currentActivityThread")
            // 设置可访问性 , 所有的 方法 , 字段 反射 , 都要设置可访问性
            currentActivityThreadMethod?.isAccessible = true
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

        // 执行 ActivityThread 的 currentActivityThread() 方法 , 传入参数 null
        var activityThreadObject: Any? = null
        try {
            activityThreadObject = currentActivityThreadMethod?.invoke(null)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        // II. 获取 LoadedApk 实例对象
        // 获取 ActivityThread 实例对象的 mPackages 成员
        // final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap<>();
        var mPackagesField: Field? = null
        try {
            mPackagesField = activityThreadClass?.getDeclaredField("mPackages")
            // 设置可访问性 , 所有的 方法 , 字段 反射 , 都要设置可访问性
            mPackagesField?.isAccessible = true
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        // 从 ActivityThread 实例对象 activityThreadObject 中
        // 获取 mPackages 成员
        var mPackagesObject: ArrayMap<String, WeakReference<Any>>? = null
        try {
            mPackagesObject = mPackagesField?.get(activityThreadObject) as ArrayMap<String, WeakReference<Any>>?
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        // 获取 WeakReference<LoadedApk> 弱引用对象
        val weakReference: WeakReference<Any>? = mPackagesObject?.get(context.packageName)
        // 获取 LoadedApk 实例对象
        val loadedApkObject = weakReference?.get()


        // III. 替换 LoadedApk 实例对象中的 mClassLoader 类加载器
        // 加载 android.app.LoadedApk 类
        var loadedApkClass: Class<*>? = null
        try {
            loadedApkClass = loader.loadClass("android.app.LoadedApk")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        // 通过反射获取 private ClassLoader mClassLoader; 类加载器对象
        var mClassLoaderField: Field? = null
        try {
            mClassLoaderField = loadedApkClass?.getDeclaredField("mClassLoader")
            // 设置可访问性
            mClassLoaderField?.isAccessible = true
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        // 替换 mClassLoader 成员
        try {
            mClassLoaderField?.set(loadedApkObject, loader)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }


    /**
     * 启动Dex中的Activity
     * @param context
     * @param actClas Activity全绝对路径类名，如com.demon.dexlib.TestActivity
     */
    fun startDexActivity(context: Context, actClas: String) {
        // 加载Activity类
        // 该类中有可执行方法 test()
        var clazz: Class<*>? = null
        try {
            clazz = loader?.loadClass(actClas)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        // 启动Activity组件
        if (clazz != null) {
            val intent = Intent(context, clazz)
            intent.putExtra("string", "hello dex~")
            intent.putExtra("number", "1024")
            context.startActivity(intent)
        }
    }
}