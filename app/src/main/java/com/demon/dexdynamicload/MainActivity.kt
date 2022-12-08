package com.demon.dexdynamicload

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val dexName = "dexlib_dex.jar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Utils.copyDex(this, dexName)

        val loader = Utils.loadDexClass(this, dexName)

        //非静态类
        val cla = loader?.loadClass("com.demon.dexlib.DexWork")

        cla?.run {
            val className = getDeclaredMethod("getClassName").invoke(newInstance()) as String
            findViewById<TextView>(R.id.text).text = className

            findViewById<Button>(R.id.btn1).setOnClickListener {
                getDeclaredMethod("showNavToast", Context::class.java, String::class.java).invoke(newInstance(), this@MainActivity, className)
            }

            val img = findViewById<ImageView>(R.id.iv)
            findViewById<Button>(R.id.btn2).setOnClickListener {
                getDeclaredMethod("loadImage", ImageView::class.java, String::class.java).invoke(
                    newInstance(), img,
                    "https://idemon.oss-cn-guangzhou.aliyuncs.com/D.png"
                )
            }
        }

        //静态类
        val claStatic = loader?.loadClass("com.demon.dexlib.DexStaticWork")

        claStatic?.run {
            val className = getMethod("getClassName").invoke(null) as String
            findViewById<TextView>(R.id.text2).text = className

            findViewById<Button>(R.id.btn22).setOnClickListener {
                getDeclaredMethod("showNavToast", Context::class.java, String::class.java).invoke(this, this@MainActivity, className)
            }

            val img = findViewById<ImageView>(R.id.iv)
            findViewById<Button>(R.id.btn22).setOnClickListener {
                getDeclaredMethod("loadImage", ImageView::class.java, String::class.java).invoke(
                    this, img,
                    "https://idemon.oss-cn-guangzhou.aliyuncs.com/luffy.jpg"
                )
            }
        }


    }
}