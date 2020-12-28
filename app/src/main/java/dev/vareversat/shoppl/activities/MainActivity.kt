package dev.vareversat.shoppl.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.vareversat.shoppl.R
import dev.vareversat.shoppl.adaptaters.ShoppingListAdapter
import dev.vareversat.shoppl.adaptaters.TinyDB
import dev.vareversat.shoppl.models.Product
import dev.vareversat.shoppl.models.ShoppingList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.product_dialog.*
import kotlinx.android.synthetic.main.shopping_list_dialog.*

class MainActivity : AppCompatActivity() {

    private var listOfShoppingItem = ArrayList<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initShoppingList()
        val adapter = ShoppingListAdapter(this, listOfShoppingItem)

        shopping_item_list.adapter = adapter
        shopping_item_list.setOnItemClickListener { parent, view, position, id ->
            showEditShoppingListActivity(position)
        }
    }

    override fun onResume() {
        super.onResume()
        val tinyDB = TinyDB(applicationContext)
        listOfShoppingItem = tinyDB.getListObject("shopping_list", ShoppingList::class.java)
        shopping_item_list.adapter = ShoppingListAdapter(this, listOfShoppingItem)
        Log.i("CC", "cc")
    }

    private fun saveShoppingList(shoppingItems: ArrayList<Any>) {
        val tinyDB = TinyDB(applicationContext)
        tinyDB.putListObject("shopping_list", shoppingItems)

    }

    private fun initShoppingList() {
        val product1 = Product("Coffee", 1, "Box")
        val product2 = Product("Pasta", 1, "Box")
        val product3 = Product("Apple", 1, "Kg")
        val product4 = Product("Sugar", 1, "Kg")

        val shoppingItem1 = ShoppingList("My List 1", arrayListOf(product1))
        val shoppingItem2 = ShoppingList("My List 2", arrayListOf(product2, product3, product4))
        listOfShoppingItem.addAll(listOf(shoppingItem1, shoppingItem2))
        saveShoppingList(listOfShoppingItem)
    }

    private fun showEditShoppingListActivity(position: Int) {
        val intent = Intent(this, EditShoppingListActivity::class.java)
        intent.putExtra("shoppingListIndex", position)
        startActivityForResult(intent, position)
    }

    fun showCreateShoppingListDialog(view: View) {
        val dialog = Dialog(this)
        dialog.setTitle("New shopping list")
        dialog.setContentView(R.layout.shopping_list_dialog)
        dialog.create_shopping_list_btn.setOnClickListener {
            listOfShoppingItem.add(
                ShoppingList(
                    dialog.shopping_list_input_text.text.toString(),
                    arrayListOf()
                )
            )
            saveShoppingList(listOfShoppingItem)
            dialog.dismiss()
            Toast.makeText(
                this,
                dialog.shopping_list_input_text.text.toString() + " created",
                Toast.LENGTH_SHORT
            ).show()
        }
        dialog.show()
    }
}

