package dev.vareversat.shoppl.adaptaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import dev.vareversat.shoppl.R
import dev.vareversat.shoppl.models.ShoppingList
import kotlinx.android.synthetic.main.shopping_list_item.view.*

class ShoppingListAdapter(
    var context: Context,
    var shoppingListArray: ArrayList<Any>
) : BaseAdapter() {

    override fun getCount(): Int {
        return shoppingListArray.size
    }

    override fun getItem(p0: Int): ShoppingList {
        return shoppingListArray[p0] as ShoppingList
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = layoutInflater.inflate(R.layout.shopping_list_item, null, true)
        val shoppingItemNameView = itemView.shopping_item_name as TextView
        val shoppingItemSizeView = itemView.shopping_item_size as TextView
        shoppingItemNameView.text = getItem(p0).name
        shoppingItemSizeView.text = String.format(
            context.resources.getQuantityString(
                R.plurals.products_quantity,
                getItem(p0).products.size
            ), getItem(p0).products.size
        )
        return itemView
    }
}