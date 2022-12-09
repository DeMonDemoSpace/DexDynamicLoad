package com.demon.dexdynamicload

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
    }
}