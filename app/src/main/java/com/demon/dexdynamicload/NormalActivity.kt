package com.demon.dexdynamicload

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class NormalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        try {


            /**
             * 非静态类反射，kt代码跟java代码反射调用完全一致
             * invoke 第一个参数传入类实例
             */
            val cla = Utils.loader?.loadClass("com.demon.dexlib.DexWork")

            cla?.run {
                val className = getMethod("getClassName").invoke(newInstance()) as String
                findViewById<TextView>(R.id.text).text = className

                findViewById<Button>(R.id.btn1).setOnClickListener {
                    getMethod("showNavToast", Context::class.java, String::class.java).invoke(newInstance(), this@NormalActivity, className)
                }

                val img = findViewById<ImageView>(R.id.iv)
                findViewById<Button>(R.id.btn2).setOnClickListener {
                    getMethod("loadImage", ImageView::class.java).invoke(
                        newInstance(), img
                    )
                }

                findViewById<Button>(R.id.btn3).setOnClickListener {
                    getMethod("synthesisQRCode", ImageView::class.java).invoke(
                        newInstance(), img
                    )
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}