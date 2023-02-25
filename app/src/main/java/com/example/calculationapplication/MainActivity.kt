package com.example.calculationapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.*

class MainActivity() : AppCompatActivity(),java.util.Observer {
    var equalpressed: Boolean = false
    lateinit var resulttext: TextView
    var operatorpressed: Boolean = false
    var decimalpressed: Boolean = false
    var errorhan: Boolean = false
    lateinit var toast: Toast
    lateinit var resultText: TextView
    private lateinit var modelCalculate: CalculatorModel

    constructor(parcel: Parcel) : this() {
        equalpressed = parcel.readByte() != 0.toByte()
        operatorpressed = parcel.readByte() != 0.toByte()
        decimalpressed = parcel.readByte() != 0.toByte()
        errorhan = parcel.readByte() != 0.toByte()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toast = Toast.makeText(this, "Invalid Operation", Toast.LENGTH_SHORT)
        resultText = findViewById(R.id.result_text__view)
        modelCalculate = CalculatorModel()
        modelCalculate.addObserver(this)
    }
    override fun update(arg0: Observable, arg1: Any?) {

        // changing text of the buttons
        // according to updated values
        Log.d("TAG", "update: "+modelCalculate.getString())
        resultText!!.text = modelCalculate.getString()
    }

    fun onClearPressed(view: View) {
        if (view is Button) {
            modelCalculate.clearString()
        }
    }

    fun onOperatorPressed(view: View) {
        if (view is Button) {
            if (!operatorpressed) {
                operatorpressed = true
                if (modelCalculate.getString() == "" || modelCalculate.getString().last() == '.') {
                    modelCalculate.appendString("0" + view.text.toString())


                } else {
                    modelCalculate.appendString(view.text.toString())
                }
                decimalpressed = false
            } else {
                 toast.show()
            }
        }
    }

    fun onDigitPressed(view: View) {
        if (view is Button) {
            if (errorhan) {
                modelCalculate.clearString()
                errorhan = false
            }
            modelCalculate.appendString(view.text.toString())
            operatorpressed = false
        }
    }

    fun onDecimalPressed(view: View) {
        if (view is Button) {
            if (errorhan) {
                modelCalculate.clearString()
                errorhan = false
            }
            if (!decimalpressed) {
                decimalpressed = true
                if (operatorpressed) {
                    modelCalculate.appendString("0.")
                    operatorpressed = false

                } else if (modelCalculate.getString().isEmpty()) {
                    modelCalculate.appendString("0.")
                } else {
                    modelCalculate.appendString(".")
                }
            } else {
                toast.show()
            }
        }
    }

    fun onEqualPressed(view: View) {
        if (errorhan) {
            modelCalculate.clearString()
            errorhan = false
        }
        calcuate()
    }

    private fun calcuate() {
        var data = modelCalculate.getString()
        data = data.replace('x', '*')
        data = data.replace('÷', '/')

        val expression = if (data.last().isDigit() || data.last().equals(".")) {
            ExpressionBuilder(data).build()
        } else {
            data=data.dropLast(1)
            ExpressionBuilder(data).build()
        }

        try {
            val result = expression.evaluate()
            val longResult = result.toLong()
            if (result == longResult.toDouble()) {
                modelCalculate.setString(longResult.toString())
            } else {
                modelCalculate.setString(result.toString())
            }
        } catch (e: java.lang.ArithmeticException) {
            e.localizedMessage?.let { modelCalculate.setString(it) }
            errorhan = true

        }
    }
}


