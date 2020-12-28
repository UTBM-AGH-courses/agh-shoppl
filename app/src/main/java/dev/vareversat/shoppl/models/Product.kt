package dev.vareversat.shoppl.models

class Product(var name: String, var quantity: Int, var unit: String) {

    override fun toString(): String {
        return "Product(name='$name', quantity=$quantity, unit='$unit')\n"
    }
}