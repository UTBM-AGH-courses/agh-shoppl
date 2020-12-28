package dev.vareversat.shoppl.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.vareversat.shoppl.R
import dev.vareversat.shoppl.adaptaters.ShoppingListAdapter
import dev.vareversat.shoppl.adaptaters.TinyDB
import dev.vareversat.shoppl.models.Product
import dev.vareversat.shoppl.models.ShoppingList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.shopping_list_dialog.*

class MainActivity : AppCompatActivity() {

    private var listOfShoppingItem = ArrayList<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
    }

    private fun saveShoppingList(shoppingItems: ArrayList<Any>) {
        val tinyDB = TinyDB(applicationContext)
        tinyDB.putListObject("shopping_list", shoppingItems)
        listOfShoppingItem = tinyDB.getListObject("shopping_list", ShoppingList::class.java)
        Log.i("CC", listOfShoppingItem.toString())

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

