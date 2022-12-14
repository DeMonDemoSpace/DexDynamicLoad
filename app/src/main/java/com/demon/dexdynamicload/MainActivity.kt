package com.demon.dexdynamicload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val dexName = "dexlib_dex.jar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utils.copyDex(this, dexName)

        Utils.loader = Utils.loadDexClass(this, dexName)
        Utils.loader?.run {
            Utils.replaceLoadedApkClassLoader(this@MainActivity, this)
        }

        /**
         * 初始化
         */
        val cla = Utils.loader?.loadClass("com.demon.dexlib.DexInit")
        cla?.run {
            getDeclaredMethod("init", Context::class.java, String::class.java, String::class.java).invoke(
                null, this@MainActivity.applicationContext,
                "https://idemon.oss-cn-guangzhou.aliyuncs.com/luffy.jpg",
                "https://www.baidu.com/"
            )

            findViewById<Button>(R.id.btn7).setOnClickListener {
                getDeclaredMethod("start").invoke(null)
            }
        }



        findViewById<Button>(R.id.btn1).setOnClickListener {
            startActivity(Intent(this, NormalActivity::class.java))
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            startActivity(Intent(this, StaticActivity::class.java))
        }

        findViewById<Button>(R.id.btn3).setOnClickListener {
            startActivity(Intent(this, ObjectActivity::class.java))
        }

        findViewById<Button>(R.id.btn4).setOnClickListener {
            startActivity(Intent(this, CompanionActivity::class.java))
        }
        findViewById<Button>(R.id.btn5).setOnClickListener {
            startActivity(Intent(this, InstanceActivity::class.java))
        }
        findViewById<Button>(R.id.btn6).setOnClickListener {
            Utils.startDexActivity(this, "com.demon.dexlib.TestActivity")
        }
    }
}