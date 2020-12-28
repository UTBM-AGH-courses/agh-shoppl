package dev.vareversat.shoppl.adaptaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import dev.vareversat.shoppl.R
import dev.vareversat.shoppl.models.Product
import kotlinx.android.synthetic.main.product_item.view.*
import kotlinx.android.synthetic.main.shopping_list_item.view.*

class ProductAdapter(
    var context: Context,
    var productList: ArrayList<Product>
) : BaseAdapter() {

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(p0: Int): Product {
        return productList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = layoutInflater.inflate(R.layout.product_item, null, true)
        val productNameView = itemView.product_name as TextView
        val productQuantityView = itemView.product_quantity as TextView
        productNameView.text = getItem(p0).name
        productQuantityView.text = getItem(p0).quantity.toString() + getItem(p0).unit
        return itemView
    }
}