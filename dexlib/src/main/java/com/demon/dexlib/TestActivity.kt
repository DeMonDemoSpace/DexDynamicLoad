package com.demon.dexlib

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


/**
 * @author DeMon
 * Created on 4/1/23.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = TextView(this)
        setContentView(text)

        val sb :StringBuilder = StringBuilder()
        intent.extras?.run {
            keySet().forEach {
                sb.append("$it=${get(it)}\n")
            }
        }
        text.text = sb.toString()
    }
}