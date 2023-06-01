package com.permissionx.calculatordemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var isNumStart = true
    private val currentInputStringBuilder = StringBuilder()
    private val numsList = mutableListOf<Int>()
    private val operatorList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberButtonClicked(view: View) {
        val textView = view as TextView
        currentInputStringBuilder.append(textView.text)
        if (isNumStart) {
            numsList.add(textView.text.toString().toInt())
            isNumStart = false
        } else {
            numsList[numsList.size - 1] = currentInputStringBuilder.toString().toInt()
        }
        showUI()

    }

    fun operatorButtonClicked(view: View) {
        val textView = view as TextView
        operatorList.add(textView.text.toString())
        currentInputStringBuilder.clear()
        isNumStart = true
        showUI()
    }

    fun clearButtonClicked(view: View) {
        currentInputStringBuilder.clear()
        process_textView.text = ""
    }

    fun backButtonClicked(view: View) {
        Log.v("MainActivity", "back")
    }

    fun equalButtonClicked(view: View) {
        Log.v("MainActivity", "equal")
    }

    private fun showUI() {
        val sb = StringBuilder()
        for ((i, num) in numsList.withIndex()) {
            sb.append(num)
            if (i < operatorList.size) {
                sb.append(" ${operatorList[i]} ")
            }
        }
        process_textView.text = sb.toString()
    }
}