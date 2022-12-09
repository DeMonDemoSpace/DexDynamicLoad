package com.demon.dexdynamicload

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CompanionActivity : AppCompatActivity() {

    private val TAG = "CompanionActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        try {

            /**
             * companion object{}  静态内部类式单例类反射
             * 1. 获取静态内部类，通过私有构造函数实例化
             * 2. invoke 第一个参数传入静态内部类实例化对象
             */
            val cla = Utils.loader?.loadClass("com.demon.dexlib.DexCompanionWork")

            cla?.run {
                //获取内部类方法
                declaredClasses.forEach { companion ->
                    Log.i(TAG, "onCreate: ${companion.canonicalName}  ${companion.simpleName}")
                    if (companion.simpleName == "Companion") {
                        //getDeclaredConstructor可获取所有构造方法，包括私有
                        //getConstructor只能获取公开的Public方法
                        val constructor = companion.getDeclaredConstructor()
                        //允许私有访问
                        constructor.isAccessible = true
                        //实例化
                        val instance = constructor.newInstance()
                        val className = companion.getMethod("getClassName").invoke(instance) as String
                        findViewById<TextView>(R.id.text).text = className

                        findViewById<Button>(R.id.btn1).setOnClickListener {
                            companion.getMethod("showNavToast", Context::class.java, String::class.java).invoke(instance, this@CompanionActivity, className)
                        }

                        val img = findViewById<ImageView>(R.id.iv)
                        findViewById<Button>(R.id.btn2).setOnClickListener {
                            companion.getMethod("loadImage", ImageView::class.java, String::class.java).invoke(
                                instance, img,
                                "https://idemon.oss-cn-guangzhou.aliyuncs.com/D.png"
                            )
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}