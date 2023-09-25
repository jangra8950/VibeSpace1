package com.app.vibespace.util

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import androidx.core.content.ContextCompat
import com.app.vibespace.R

class ColorArrayAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    private var selectedItem = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        // Set the text color of the item
        val textColor = if (position == selectedItem) {
            ContextCompat.getColor(context, R.color.colorPrimary)
        } else {
            ContextCompat.getColor(context, R.color.colorTxt)
        }
        view.findViewById<TextView>(android.R.id.text1).setTextColor(textColor)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_dropdown, parent, false)

        if (position == selectedItem) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            view.setBackgroundColor(Color.TRANSPARENT)
        }
        // Set the text color of the dropdown item
        val textColor = if (position == selectedItem) {
            ContextCompat.getColor(context, R.color.colorPrimary)
        } else {
            ContextCompat.getColor(context, R.color.colorWhite)
        }
        view.findViewById<TextView>(android.R.id.text1).setTextColor(textColor)

        return view
    }

    fun setSelectedItem(position: Int) {
        selectedItem = position
        notifyDataSetChanged()
    }
}




