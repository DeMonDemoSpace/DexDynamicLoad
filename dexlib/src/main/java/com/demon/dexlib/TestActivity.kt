package com.demon.dexlib

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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


        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        val text = TextView(this)
        val button = Button(this)
        button.text = "Click"
        layout.addView(text)
        layout.addView(button)
        setContentView(layout)

        val sb: StringBuilder = StringBuilder()
        sb.append("TestActivity\n")
        intent.extras?.run {
            keySet().forEach {
                sb.append("$it=${get(it)}\n")
            }
        }
        text.text = sb.toString()


        button.setOnClickListener {
           Toast.makeText(this@TestActivity,"Button~",Toast.LENGTH_SHORT).show()
        }
    }
}