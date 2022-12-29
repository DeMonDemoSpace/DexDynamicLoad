package com.demon.dexdynamicload

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InstanceActivity : AppCompatActivity() {

    private val TAG = "InstanceActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        try {

            /**
             * kt线程安全式单例类反射
             * 1. 获取静态内部类，通过私有构造函数实例化
             * 2. 私有构造函数获取单例实例getInstance
             * 2. invoke 第一个参数传入单例实例getInstance
             */
            val cla = Utils.loader?.loadClass("com.demon.dexlib.DexInstanceWork")

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
                        //单例实例
                        val getInstance = companion.getMethod("getInstance").invoke(instance)
                        val className = getMethod("getClassName").invoke(getInstance) as String
                        findViewById<TextView>(R.id.text).text = className

                        findViewById<Button>(R.id.btn1).setOnClickListener {
                            getMethod("showNavToast", Context::class.java, String::class.java).invoke(getInstance, this@InstanceActivity, className)
                        }

                        val img = findViewById<ImageView>(R.id.iv)
                        findViewById<Button>(R.id.btn2).setOnClickListener {
                            getMethod("loadImage", ImageView::class.java).invoke(
                                getInstance, img
                            )
                        }

                        findViewById<Button>(R.id.btn3).setOnClickListener {
                            getMethod("synthesisQRCode", ImageView::class.java).invoke(
                                getInstance, img
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