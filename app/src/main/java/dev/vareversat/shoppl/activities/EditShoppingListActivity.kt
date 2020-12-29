package dev.vareversat.shoppl.activities

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.vareversat.shoppl.R
import dev.vareversat.shoppl.adaptaters.ProductAdapter
import dev.vareversat.shoppl.adaptaters.TinyDB
import dev.vareversat.shoppl.databinding.ActivityEditShoppingListBinding
import dev.vareversat.shoppl.databinding.ConfirmDeleteShopingListDialogBinding
import dev.vareversat.shoppl.databinding.ProductDialogBinding
import dev.vareversat.shoppl.databinding.ShoppingListDialogBinding
import dev.vareversat.shoppl.models.Product
import dev.vareversat.shoppl.models.ShoppingList


class EditShoppingListActivity : AppCompatActivity() {

    private lateinit var shoppingList: ShoppingList
    private lateinit var binding: ActivityEditShoppingListBinding
    private var index: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_shopping_list)
        binding = ActivityEditShoppingListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val actionBar: ActionBar? = actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        index = intent.getIntExtra("shoppingListIndex", 0)
        getShoppingList()
        binding.shoppingListEditName.text = shoppingList.name

        val adapter = ProductAdapter(this, shoppingList.products)
        binding.productList.adapter = adapter
        binding.productList.setOnItemClickListener { _, _, position, _ ->
            showEditProductDialog(position)
        }
        if (shoppingList.products.isNotEmpty()) {
            binding.noProducts.visibility = View.GONE
            binding.saveProductBtn.visibility = View.VISIBLE
        } else {
            binding.noProducts.visibility = View.VISIBLE
            binding.saveProductBtn.visibility = View.GONE
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
        val dialogBinding = ConfirmDeleteShopingListDialogBinding.inflate(layoutInflater)
        dialogBinding.confirmButton.setOnClickListener {
            val tinyDB = TinyDB(applicationContext)
            val list = tinyDB.getListObject("shopping_list", ShoppingList::class.java)
            list.removeAt(index!!)
            tinyDB.putListObject("shopping_list", list)
            finish()
        }
        dialogBinding.cancelButton.setOnClickListener {
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
        binding.productList.adapter = ProductAdapter(this, shoppingList.products)
    }

    private fun showEditShoppingListNameDialog() {
        val dialog = Dialog(this)
        dialog.setTitle("Edit shopping list name")
        dialog.setContentView(R.layout.shopping_list_dialog)
        val dialogBinding = ShoppingListDialogBinding.inflate(layoutInflater)
        dialogBinding.createShoppingListBtn.text = getString(R.string.update)
        dialogBinding.shoppingListInputText.setText(shoppingList.name)
        dialogBinding.createShoppingListBtn.setOnClickListener {
            shoppingList.name = dialogBinding.shoppingListInputText.text.toString()
            binding.shoppingListEditName.text = shoppingList.name
            saveShoppingList()
            dialog.dismiss()
            Toast.makeText(this, "Shopping list name updated", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    fun showAddProductDialog(@Suppress("UNUSED_PARAMETER") view: View) {
        val dialog = Dialog(this)
        dialog.setTitle("New product")
        dialog.setContentView(R.layout.product_dialog)
        val dialogBinding = ProductDialogBinding.inflate(layoutInflater)
        dialogBinding.deleteProductBtn.visibility = View.GONE
        dialogBinding.createProductBtn.setOnClickListener {
            shoppingList.products.add(
                Product(
                    dialogBinding.productNameInputText.text.toString(),
                    dialogBinding.productQuantityInputText.text.toString().toInt(),
                    dialogBinding.productUnitInputText.text.toString()
                )
            )
            saveShoppingList()
            dialog.dismiss()
            if (shoppingList.products.isNotEmpty()) {
                binding.noProducts.visibility = View.GONE
                binding.saveProductBtn.visibility = View.VISIBLE
            } else {
                binding.noProducts.visibility = View.VISIBLE
                binding.saveProductBtn.visibility = View.GONE
            }
            Toast.makeText(
                this,
                dialogBinding.productNameInputText.text.toString() + " added to " + shoppingList.name,
                Toast.LENGTH_SHORT
            ).show()
        }
        dialog.show()
    }

    private fun showEditProductDialog(position: Int) {
        val dialog = Dialog(this)
        dialog.setTitle("Edit product")
        dialog.setContentView(R.layout.product_dialog)
        val dialogBinding = ProductDialogBinding.inflate(layoutInflater)
        dialogBinding.createProductBtn.text = getString(R.string.update)
        dialogBinding.productNameInputText.setText(shoppingList.products[position].name)
        dialogBinding.productQuantityInputText.setText(shoppingList.products[position].quantity.toString())
        dialogBinding.productUnitInputText.setText(shoppingList.products[position].unit)
        dialogBinding.createProductBtn.setOnClickListener {
            shoppingList.products[position].name = dialogBinding.productNameInputText.text.toString()
            val quantity = dialogBinding.productQuantityInputText.text.toString()
            try {
                shoppingList.products[position].quantity = quantity.toInt()
            } catch (e: NumberFormatException) {
                shoppingList.products[position].quantity = 0
            }
            shoppingList.products[position].unit = dialogBinding.productUnitInputText.text.toString()
            saveShoppingList()
            dialog.dismiss()
            Toast.makeText(
                this,
                dialogBinding.productNameInputText.text.toString() + " updated",
                Toast.LENGTH_SHORT
            ).show()
        }
        dialogBinding.deleteProductBtn.setOnClickListener {
            shoppingList.products.removeAt(position)
            saveShoppingList()
            dialog.dismiss()
            if (shoppingList.products.isNotEmpty()) {
                binding.noProducts.visibility = View.GONE
                binding.saveProductBtn.visibility = View.VISIBLE
            } else {
                binding.noProducts.visibility = View.VISIBLE
                binding.saveProductBtn.visibility = View.GONE
            }
            Toast.makeText(
                this,
                dialogBinding.productNameInputText.text.toString() + " deleted from " + shoppingList.name,
                Toast.LENGTH_SHORT
            ).show()
        }
        dialog.show()
    }

    fun saveCheckedProducts(@Suppress("UNUSED_PARAMETER") view: View) {
        saveShoppingList()
        Toast.makeText(
            this,
            "Checked product(s) saved",
            Toast.LENGTH_SHORT
        ).show()
    }
}