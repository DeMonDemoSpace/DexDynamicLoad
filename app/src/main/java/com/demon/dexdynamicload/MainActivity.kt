package com.demon.dexdynamicload

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.demon.dexlib.DexWork

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val work = DexWork()

        findViewById<TextView>(R.id.text).text = work.getClassName()

        findViewById<Button>(R.id.btn1).setOnClickListener {
            work.showNavToast(this, work.getClassName())
        }

        val img = findViewById<ImageView>(R.id.iv)
        findViewById<Button>(R.id.btn2).setOnClickListener {
            work.loadImage(img, "https://idemon.oss-cn-guangzhou.aliyuncs.com/D.png")
        }



    }
}