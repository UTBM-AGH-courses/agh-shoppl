package dev.vareversat.shoppl.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.vareversat.shoppl.R
import dev.vareversat.shoppl.adaptaters.ShoppingItemAdapter
import dev.vareversat.shoppl.models.Product
import dev.vareversat.shoppl.models.ShoppingItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listOfShoppingItem = ArrayList<ShoppingItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initShoppingList()
        val adapter = ShoppingItemAdapter(this, listOfShoppingItem)
        shopping_item_list.adapter = adapter
    }

    private fun initShoppingList() {
        val product1 = Product("Coffee", 1, "Box")
        val product2 = Product("Pasta", 1, "Box")
        val product3 = Product("Apple", 1, "Kg")
        val product4 = Product("Sugar", 1, "Kg")

        val shoppingItem1 = ShoppingItem("My List 1", listOf(product1))
        val shoppingItem2 = ShoppingItem("My List 2", listOf(product2, product3, product4))

        listOfShoppingItem.addAll(listOf(shoppingItem1, shoppingItem2))
    }
}

