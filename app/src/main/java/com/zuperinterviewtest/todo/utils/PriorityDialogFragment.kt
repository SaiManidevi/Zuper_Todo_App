package com.zuperinterviewtest.todo.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.zuperinterviewtest.todo.R
import com.zuperinterviewtest.todo.adapters.PriorityMenuAdapter

class PriorityDialogFragment : DialogFragment() {
    private lateinit var listener: PriorityDialogListener

    interface PriorityDialogListener {
        fun onTodoPriorityClick(selectedPriority: Constants.PRIORITY)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity/fragment implements the callback interface
        try {
            // Instantiate the PriorityDialogListener so events (function callbacks) can be sent
            // to host
            listener = parentFragment as PriorityDialogListener
        } catch (e: ClassCastException) {
            // The activity/fragment doesn't implement the interface, throw exception
            throw ClassCastException(
                ("$context must implement PriorityDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val priorityList = listOf("Low", "Medium", "High")
            val adapter = PriorityMenuAdapter(
                requireContext(),
                R.layout.item_priority_picker,
                priorityList = priorityList
            )
            builder.setAdapter(adapter) { dialogInterface, i ->
                val selectedPriority = when (i) {
                    0 -> Constants.PRIORITY.LOW
                    1 -> Constants.PRIORITY.MEDIUM
                    2 -> Constants.PRIORITY.HIGH
                    else -> Constants.PRIORITY.LOW
                }
                listener.onTodoPriorityClick(selectedPriority)
                dialogInterface.dismiss()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}