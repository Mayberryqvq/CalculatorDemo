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
    private val numList = mutableListOf<Number>()
    private val operatorList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clearBtn.setOnClickListener {
            currentInputStringBuilder.clear()
            numList.clear()
            operatorList.clear()
            isNumStart = true
            process_textView.text = ""
            result_textView.text = "0"
        }
        backBtn.setOnClickListener {
            if (operatorList.isNotEmpty() || numList.isNotEmpty()) {
                if (numList.size > operatorList.size) {
                    numList.removeLast()
                    isNumStart = true
                    currentInputStringBuilder.clear()
                } else {
                    operatorList.removeLast()
                    isNumStart = false
                    currentInputStringBuilder.append(numList.last())
                }
                showProcess()
                showResult()
            }
        }
        negateBtn.setOnClickListener {
            if (numList.isNotEmpty()) {
                if (numList[numList.lastIndex] is Int) {
                    numList[numList.size - 1] = 0 - numList[numList.size - 1].toInt()
                } else {
                    numList[numList.size - 1] = String.format("%.2f", (0 - numList[numList.size - 1].toFloat())).toFloat()
                }
                Log.v("numList", "$numList")
                showProcess()
                showResult()
            }
        }
        dotBtn.setOnClickListener {
            if (!isNumStart && !currentInputStringBuilder.contains('.')) {
                currentInputStringBuilder.append('.')
                process_textView.text = StringBuilder(process_textView.text).append('.').toString()
            }
        }
        equalBtn.setOnClickListener {
            showResult()
            currentInputStringBuilder.clear()
            process_textView.text = ""
            numList.clear()
            currentInputStringBuilder.append(result_textView.text)
            if (currentInputStringBuilder.contains('.')) {
                numList.add(String.format("%.2f", currentInputStringBuilder.toString().toFloat()).toFloat())
            } else {
                numList.add(currentInputStringBuilder.toString().toInt())
            }
            Log.v("numList", "$numList")
            operatorList.clear()
        }
        plusBtn.setOnClickListener {
            operatorButtonClicked(it)
        }
        subBtn.setOnClickListener {
            operatorButtonClicked(it)
        }
        multipleBtn.setOnClickListener {
            operatorButtonClicked(it)
        }
        divideBtn.setOnClickListener {
            operatorButtonClicked(it)
        }
        modBtn.setOnClickListener {
            operatorButtonClicked(it)
        }
        num0.setOnClickListener {
            numberButtonClicked(it)
        }
        num1.setOnClickListener {
            numberButtonClicked(it)
        }
        num2.setOnClickListener {
            numberButtonClicked(it)
        }
        num3.setOnClickListener {
            numberButtonClicked(it)
        }
        num4.setOnClickListener {
            numberButtonClicked(it)
        }
        num5.setOnClickListener {
            numberButtonClicked(it)
        }
        num6.setOnClickListener {
            numberButtonClicked(it)
        }
        num7.setOnClickListener {
            numberButtonClicked(it)
        }
        num8.setOnClickListener {
            numberButtonClicked(it)
        }
        num9.setOnClickListener {
            numberButtonClicked(it)
        }
    }

    private fun numberButtonClicked(view: View) {
        val textView = view as TextView
        currentInputStringBuilder.append(textView.text)
        if (isNumStart) {
            numList.add(textView.text.toString().toInt())
            isNumStart = false
        } else {
            if (currentInputStringBuilder.contains('.')) {
                numList[numList.size - 1] = currentInputStringBuilder.toString().toFloat()
            } else {
                numList[numList.size - 1] = currentInputStringBuilder.toString().toInt()
            }
        }
        Log.v("numList", "$numList")
        showProcess()
        showResult()

    }

    private fun operatorButtonClicked(view: View) {
        if (operatorList.size < numList.size) {
            val textView = view as TextView
            operatorList.add(textView.text.toString())
            currentInputStringBuilder.clear()
            isNumStart = true
            showProcess()
        }
    }

    private fun showProcess() {
        val sb = StringBuilder()
        for ((i, num) in numList.withIndex()) {
            sb.append(num)
            if (i < operatorList.size) {
                sb.append(" ${operatorList[i]} ")
            }
        }
        process_textView.text = sb.toString()
    }

    private fun showResult() {
        if (numList.size > 0) {
            var i = 0
            var param1 = numList[0].toFloat()
            var param2 = 0.0f
            if (operatorList.size > 0) {
                while (true) {
                    val operator = operatorList[i]
                    //若当前运算符为乘除，则直接运算
                    if (operator == "x" || operator == "÷" || operator == "%") {
                        if (i + 1 < numList.size) {
                            param2 = numList[i + 1].toFloat()
                            param1 = calculate(param1, operator, param2)
                        }
                    } else {
                        //若当前运算符为加减，则先考虑 1.这是最后一个运算符 和 2.接下来的运算符也是加减 这两种情况
                        if (i == operatorList.size - 1 || (operatorList[i + 1] != "x" && operatorList[i + 1] != "÷" && operatorList[i + 1] != "%")) {
                            param2 = numList[i + 1].toFloat()
                            param1 = calculate(param1, operator, param2)
                        } else {
                            //后面还有运算符且运算符是乘除
                            var j = i + 1
                            var mParam1 = numList[j].toFloat()
                            var mParam2 = 0.0f
                            while (true) {
                                if (operatorList[j] == "x" || operatorList[j] == "÷" || operatorList[j] == "%") {
                                    mParam2 = numList[j + 1].toFloat()
                                    mParam1 = calculate(mParam1, operatorList[j], mParam2)
                                } else {
                                     break
                                }
                                if (++j == operatorList.size) {
                                    break
                                }
                            }
                            param2 = mParam1
                            param1 = calculate(param1, operator, param2)
                            i = j - 1
                        }
                    }
                    if (++i == operatorList.size) {
                        break
                    }
                }
            }
            result_textView.text = String.format("%.2f", param1)
        } else {
            result_textView.text = "0"
        }
    }

    private fun calculate(param1: Float, operator: String, param2: Float): Float {
        var res = 0.0f
         when (operator) {
             "+" -> res = param1 + param2
             "—" -> res = param1 - param2
             "x" -> res = param1 * param2
             "÷" -> res = param1 / param2
             "%" -> res = param1 % param2
        }
        return res
    }
}