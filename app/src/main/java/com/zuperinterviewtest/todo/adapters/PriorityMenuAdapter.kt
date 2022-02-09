package com.zuperinterviewtest.todo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.zuperinterviewtest.todo.R

class PriorityMenuAdapter(context: Context, resource: Int, private val priorityList: List<String>) :
    ArrayAdapter<String?>(context, resource) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var currentView = convertView
        if (convertView == null) {
            currentView =
                LayoutInflater.from(context).inflate(R.layout.item_priority_picker, parent, false)
        }
        val priorityText: TextView = currentView?.findViewById(R.id.tv_item_priority) as TextView
        val priorityCircle: View = currentView.findViewById(R.id.item_priority_circle) as View
        val divider: View = currentView.findViewById(R.id.item_priority_divider) as View
        divider.visibility = View.GONE
        val currentPriority: String = priorityList[position]
        priorityText.text = currentPriority
        priorityCircle.background = when (currentPriority) {
            "Low" -> AppCompatResources.getDrawable(context, R.drawable.circle_solid_priority_low)
            "Medium" -> AppCompatResources.getDrawable(
                context,
                R.drawable.circle_solid_priority_medium
            )
            "High" -> AppCompatResources.getDrawable(context, R.drawable.circle_solid_priority_high)
            else -> AppCompatResources.getDrawable(context, R.drawable.circle_solid_priority_low)
        }
        if (currentPriority == "High")
        // Since it is the last priority option, hide the divider line
            divider.visibility = View.GONE
        else
        // Ensure that divider is visible for other priority options
            divider.visibility = View.VISIBLE
        return currentView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var currentView = convertView
        if (convertView == null) {
            currentView =
                LayoutInflater.from(context).inflate(R.layout.item_priority_picker, parent, false)
        }
        val priorityText: TextView = currentView?.findViewById(R.id.tv_item_priority) as TextView
        val priorityCircle: View = currentView.findViewById(R.id.item_priority_circle) as View
        val divider: View = currentView.findViewById(R.id.item_priority_divider) as View
        val currentPriority: String = priorityList[position]
        priorityText.text = currentPriority
        priorityCircle.background = when (currentPriority) {
            "Low" -> AppCompatResources.getDrawable(context, R.drawable.circle_solid_priority_low)
            "Medium" -> AppCompatResources.getDrawable(
                context,
                R.drawable.circle_solid_priority_medium
            )
            "High" -> AppCompatResources.getDrawable(context, R.drawable.circle_solid_priority_high)
            else -> AppCompatResources.getDrawable(context, R.drawable.circle_solid_priority_low)
        }
        if (currentPriority == "High")
        // Since it is the last priority option, hide the divider line
            divider.visibility = View.GONE
        else
        // Ensure that divider is visible for other priority options
            divider.visibility = View.VISIBLE
        return currentView
    }

    override fun getCount(): Int {
        return priorityList.size
    }

    override fun getItem(position: Int): String {
        return priorityList[position]
    }
}