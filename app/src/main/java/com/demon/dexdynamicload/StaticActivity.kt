package com.demon.dexdynamicload

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class StaticActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        /**
         * 静态类反射
         *
         * kt的静态方法有两种方式:
         * 1. object xxx{} 懒汉式单例
         * 2. companion object{}  静态内部类式单例
         *
         * 需要注意，kt静态类为java后默认转为单例模式，直接按照java的静态方法反射invoke(null)会报错，
         * 最简单的解决方案就是kt方法增加注释@JvmStatic
         * 其次是获取到单例对象，可参考单例类中的实现。
         */
        val claStatic = Utils.loader?.loadClass("com.demon.dexlib.DexStaticWork")

        claStatic?.run {

            val className = getMethod("getClassName").invoke(null) as String
            findViewById<TextView>(R.id.text).text = className

            findViewById<Button>(R.id.btn1).setOnClickListener {
                getDeclaredMethod("showNavToast", Context::class.java, String::class.java).invoke(null, this@StaticActivity, className)
            }

            val img = findViewById<ImageView>(R.id.iv)
            findViewById<Button>(R.id.btn2).setOnClickListener {
                getDeclaredMethod("loadImage", ImageView::class.java, String::class.java).invoke(
                    null, img,
                    "https://idemon.oss-cn-guangzhou.aliyuncs.com/luffy.jpg"
                )
            }
        }


    }
}