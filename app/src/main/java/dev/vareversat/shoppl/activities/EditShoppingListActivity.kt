package dev.vareversat.shoppl.activities

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.vareversat.shoppl.R
import dev.vareversat.shoppl.adaptaters.ProductAdapter
import dev.vareversat.shoppl.adaptaters.TinyDB
import dev.vareversat.shoppl.models.Product
import dev.vareversat.shoppl.models.ShoppingList
import kotlinx.android.synthetic.main.activity_edit_shopping_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.confirm_delete_shoping_list_dialog.*
import kotlinx.android.synthetic.main.product_dialog.*
import kotlinx.android.synthetic.main.product_item.*
import kotlinx.android.synthetic.main.product_item.view.*
import kotlinx.android.synthetic.main.shopping_list_dialog.*


class EditShoppingListActivity : AppCompatActivity() {

    private lateinit var shoppingList: ShoppingList
    private var index: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_shopping_list)
        val actionBar: ActionBar? = actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        index = intent.getIntExtra("shoppingListIndex", 0)
        getShoppingList()
        shopping_list_edit_name.text = shoppingList.name

        val adapter = ProductAdapter(this, shoppingList.products)
        product_list.adapter = adapter
        product_list.setOnItemClickListener { parent, view, position, id ->
            showEditProductDialog(position)
        }
        if (shoppingList.products.isNotEmpty()) {
            no_products.visibility = View.GONE
            save_product_btn.visibility = View.VISIBLE
        } else {
            no_products.visibility = View.VISIBLE
            save_product_btn.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.edit_shopping_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_shopping_list -> {
                showConfirmDeleteShoppingList()
                true
            }
            R.id.edit_shopping_list -> {
                showEditShoppingListNameDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmDeleteShoppingList() {
        val dialog = Dialog(this)
        dialog.setTitle("Delete shopping list")
        dialog.setContentView(R.layout.confirm_delete_shoping_list_dialog)
        dialog.confirm_button.setOnClickListener {
            val tinyDB = TinyDB(applicationContext)
            val list = tinyDB.getListObject("shopping_list", ShoppingList::class.java)
            list.removeAt(index!!)
            tinyDB.putListObject("shopping_list", list)
            finish()
        }
        dialog.cancel_button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun getShoppingList() {
        val tinyDB = TinyDB(applicationContext)
        val list = tinyDB.getListObject("shopping_list", ShoppingList::class.java)
        shoppingList = list[index!!] as ShoppingList
    }

    private fun saveShoppingList() {
        val tinyDB = TinyDB(applicationContext)
        val list = tinyDB.getListObject("shopping_list", ShoppingList::class.java)
        list[index!!] = shoppingList
        tinyDB.putListObject("shopping_list", list)
        product_list.adapter = ProductAdapter(this, shoppingList.products)
    }

    private fun showEditShoppingListNameDialog() {
        val dialog = Dialog(this)
        dialog.setTitle("Edit shopping list name")
        dialog.setContentView(R.layout.shopping_list_dialog)
        dialog.create_shopping_list_btn.text = getString(R.string.update)
        dialog.shopping_list_input_text.setText(shoppingList.name)
        dialog.create_shopping_list_btn.setOnClickListener {
            shoppingList.name = dialog.shopping_list_input_text.text.toString()
            shopping_list_edit_name.text = shoppingList.name
            saveShoppingList()
            dialog.dismiss()
            Toast.makeText(this, "Shopping list name updated", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    fun showAddProductDialog(view: View) {
        val dialog = Dialog(this)
        dialog.setTitle("New product")
        dialog.setContentView(R.layout.product_dialog)
        dialog.delete_product_btn.visibility = View.GONE
        dialog.create_product_btn.setOnClickListener {
            shoppingList.products.add(
                Product(
                    dialog.product_name_input_text.text.toString(),
                    dialog.product_quantity_input_text.text.toString().toInt(),
                    dialog.product_unit_input_text.text.toString()
                )
            )
            saveShoppingList()
            dialog.dismiss()
            if (shoppingList.products.isNotEmpty()) {
                no_products.visibility = View.GONE
                save_product_btn.visibility = View.VISIBLE
            } else {
                no_products.visibility = View.VISIBLE
                save_product_btn.visibility = View.GONE
            }
            Toast.makeText(
                this,
                dialog.product_name_input_text.text.toString() + " added to " + shoppingList.name,
                Toast.LENGTH_SHORT
            ).show()
        }
        dialog.show()
    }

    private fun showEditProductDialog(position: Int) {
        val dialog = Dialog(this)
        dialog.setTitle("Edit product")
        dialog.setContentView(R.layout.product_dialog)
        dialog.create_product_btn.text = getString(R.string.update)
        dialog.product_name_input_text.setText(shoppingList.products[position].name)
        dialog.product_quantity_input_text.setText(shoppingList.products[position].quantity.toString())
        dialog.product_unit_input_text.setText(shoppingList.products[position].unit)
        dialog.create_product_btn.setOnClickListener {
            shoppingList.products[position].name = dialog.product_name_input_text.text.toString()
            val quantity =dialog.product_quantity_input_text.text.toString()
            try {
                shoppingList.products[position].quantity = quantity.toInt()
            } catch (e: NumberFormatException) {
                shoppingList.products[position].quantity = 0
            }
            shoppingList.products[position].unit = dialog.product_unit_input_text.text.toString()
            saveShoppingList()
            dialog.dismiss()
            Toast.makeText(
                this,
                dialog.product_name_input_text.text.toString() + " updated",
                Toast.LENGTH_SHORT
            ).show()
        }
        dialog.delete_product_btn.setOnClickListener {
            shoppingList.products.removeAt(position)
            saveShoppingList()
            dialog.dismiss()
            if (shoppingList.products.isNotEmpty()) {
                no_products.visibility = View.GONE
                save_product_btn.visibility = View.VISIBLE
            } else {
                no_products.visibility = View.VISIBLE
                save_product_btn.visibility = View.GONE
            }
            Toast.makeText(
                this,
                dialog.product_name_input_text.text.toString() + " deleted from " + shoppingList.name,
                Toast.LENGTH_SHORT
            ).show()
        }
        dialog.show()
    }

    fun saveCheckedProducts(view: View) {
        saveShoppingList()
        Toast.makeText(
            this,
            "Checked product saved",
            Toast.LENGTH_SHORT
        ).show()
    }
}