package com.cotyoragames.shoppinglist.ui.shoppingitemlist

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.core.view.get
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import kotlinx.android.synthetic.main.dialog_add.*

class AddShoppingItemDialog(context: Context,var addDialogListener: AddDialogListener):AppCompatDialog(context), AdapterView.OnItemSelectedListener {
    private var positition:Int = 0
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add)
        tvApprove.setOnClickListener { 
            val name = etName.text.toString()
            val amount = etAmount.text.toString()
            if(name.isEmpty() || amount.isEmpty())
            {
                Toast.makeText(context, "Please enter all information", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            spinner.get(0)
            val item = ShoppingItem(name,amount.toDouble(),spinner.selectedItem.toString())
            addDialogListener.onAddButtonClicked(item)
            dismiss()
        }
        tvCancel.setOnClickListener {
            cancel()
        }
        spinner
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        positition=pos
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}