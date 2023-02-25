package com.example.calculationapplication

import java.util.Observable

class CalculatorModel : Observable() {
   private var calculationString :String = ""

    public fun getString() :String{
        return calculationString
    }
    public fun setString(string:String)
    {
        calculationString=string
        setChanged()
        notifyObservers()
    }
     public fun appendString(string:String)
     {
         calculationString += string
         setChanged()
         notifyObservers()
     }
    fun changeString(text:String)
    {
        calculationString.dropLast(1)
        calculationString += text
    }
    fun clearString()
    {
        calculationString=""
        setChanged()
        notifyObservers()
    }
}