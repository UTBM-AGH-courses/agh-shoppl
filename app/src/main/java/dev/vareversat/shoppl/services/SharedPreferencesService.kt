package dev.vareversat.shoppl.services

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson

class SharedPreferencesService(appContext: Context) {
    private val preferences: SharedPreferences =
        appContext.getSharedPreferences("shopping_list", Context.MODE_PRIVATE)

    private fun getListString(key: String?): ArrayList<String> {
        return ArrayList(listOf(*TextUtils.split(preferences.getString(key, ""), "‚‗‚")))
    }

    private fun putListString(key: String?, stringList: ArrayList<String>) {
        val myStringList = stringList.toTypedArray()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
    }

    fun getListObject(key: String?, mClass: Class<*>?): ArrayList<Any> {
        val gson = Gson()
        val objStrings = getListString(key)
        val objects = ArrayList<Any>()
        for (jObjString in objStrings) {
            val value = gson.fromJson(jObjString, mClass)
            objects.add(value)
        }
        return objects
    }

    fun putListObject(key: String?, objArray: ArrayList<Any>) {
        val gson = Gson()
        val objStrings = ArrayList<String>()
        for (obj in objArray) {
            objStrings.add(gson.toJson(obj))
        }
        putListString(key, objStrings)
    }

}