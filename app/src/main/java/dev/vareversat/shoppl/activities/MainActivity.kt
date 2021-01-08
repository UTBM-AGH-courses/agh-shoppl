package dev.vareversat.shoppl.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.vareversat.shoppl.adaptaters.ShoppingListAdapter
import dev.vareversat.shoppl.adaptaters.TinyDB
import dev.vareversat.shoppl.databinding.ActivityMainBinding
import dev.vareversat.shoppl.databinding.ShoppingListDialogBinding
import dev.vareversat.shoppl.models.ShoppingList

class MainActivity : AppCompatActivity() {

    private var listOfShoppingItem = ArrayList<Any>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val adapter = ShoppingListAdapter(this, listOfShoppingItem)
        binding.shoppingItemList.adapter = adapter
        binding.shoppingItemList.setOnItemClickListener { _, _, position, _ ->
            showEditShoppingListActivity(position)
        }
        if (listOfShoppingItem.isNotEmpty()) {
            binding.noShoppingList.visibility = View.GONE
        } else {
            binding.noShoppingList.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        val tinyDB = TinyDB(applicationContext)
        listOfShoppingItem = tinyDB.getListObject("shopping_list", ShoppingList::class.java)
        binding.shoppingItemList.adapter = ShoppingListAdapter(this, listOfShoppingItem)
        if (listOfShoppingItem.isNotEmpty()) {
            binding.noShoppingList.visibility = View.GONE
        } else {
            binding.noShoppingList.visibility = View.VISIBLE
        }
    }

    private fun saveShoppingList(shoppingItems: ArrayList<Any>) {
        val tinyDB = TinyDB(applicationContext)
        tinyDB.putListObject("shopping_list", shoppingItems)
        listOfShoppingItem = tinyDB.getListObject("shopping_list", ShoppingList::class.java)
        binding.shoppingItemList.adapter = ShoppingListAdapter(this, listOfShoppingItem)
        if (listOfShoppingItem.isNotEmpty()) {
            binding.noShoppingList.visibility = View.GONE
        } else {
            binding.noShoppingList.visibility = View.VISIBLE
        }

    }

    private fun showEditShoppingListActivity(position: Int) {
        val intent = Intent(this, EditShoppingListActivity::class.java)
        intent.putExtra("shoppingListIndex", position)
        startActivityForResult(intent, position)
    }

    fun showCreateShoppingListDialog(@Suppress("UNUSED_PARAMETER") view: View) {
        val dialog = Dialog(this)
        dialog.setTitle("New shopping list")
        val dialogBinding = ShoppingListDialogBinding.inflate(layoutInflater)
        dialogBinding.createShoppingListBtn.setOnClickListener {
            listOfShoppingItem.add(
                ShoppingList(
                    dialogBinding.shoppingListInputText.text.toString(),
                    hashMapOf("No category" to arrayListOf())
                )
            )
            saveShoppingList(listOfShoppingItem)
            dialog.dismiss()
            Toast.makeText(
                this,
                dialogBinding.shoppingListInputText.text.toString() + " created",
                Toast.LENGTH_SHORT
            ).show()
        }
        dialog.setContentView(dialogBinding.root)
        dialog.show()
    }
}

