package com.demon.dexdynamicload

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ObjectActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        try {

            /**
             * Object懒汉式单例类反射
             * 1. 先获取到INSTANCE对象
             * 2. invoke 第一个参数传入cast(INSTANCE)
             *
             */
            val cla = Utils.loader?.loadClass("com.demon.dexlib.DexObjectWork")

            cla?.run {
                val instance = getDeclaredField("INSTANCE").get(null)
                val className = getMethod("getClassName").invoke(cast(instance)) as String
                findViewById<TextView>(R.id.text).text = className

                findViewById<Button>(R.id.btn1).setOnClickListener {
                    getMethod("showNavToast", Context::class.java, String::class.java).invoke(cast(instance), this@ObjectActivity, className)
                }

                val img = findViewById<ImageView>(R.id.iv)
                findViewById<Button>(R.id.btn2).setOnClickListener {
                    getMethod("loadImage", ImageView::class.java).invoke(
                        cast(instance), img
                    )
                }

                findViewById<Button>(R.id.btn3).setOnClickListener {
                    getMethod("synthesisQRCode", ImageView::class.java).invoke(
                        cast(instance), img
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}