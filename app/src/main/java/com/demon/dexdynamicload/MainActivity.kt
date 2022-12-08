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


    }
}