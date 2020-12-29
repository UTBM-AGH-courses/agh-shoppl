package dev.vareversat.shoppl.adaptaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import dev.vareversat.shoppl.R
import dev.vareversat.shoppl.databinding.ProductDialogBinding
import dev.vareversat.shoppl.databinding.ProductItemBinding
import dev.vareversat.shoppl.models.Product

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
        val binding = ProductItemBinding.inflate(layoutInflater)
        val productNameView = binding.productName
        val productQuantityView = binding.productQuantity
        val productCheckedView = binding.productCheckBox
        productCheckedView.setOnCheckedChangeListener { _, checked ->
            getItem(p0).checked = checked
        }
        productNameView.text = getItem(p0).name
        productCheckedView.isChecked = getItem(p0).checked
        productQuantityView.text = String.format(
            context.getString(R.string.string_with_spaces),
            getItem(p0).quantity.toString(), getItem(p0).unit
        )
        return binding.root
    }
}