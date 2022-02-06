package com.zuperinterviewtest.todo

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.zuperinterviewtest.todo.adapters.PriorityMenuAdapter
import com.zuperinterviewtest.todo.databinding.DialogPriorityPickerBinding

class PriorityDialogFragment : DialogFragment() {
    private lateinit var listener: PriorityDialogListener

    interface PriorityDialogListener {
        fun onPriorityLowClick()
        fun onPriorityMediumClick()
        fun onPriorityHighClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = parentFragment as PriorityDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                ("$context must implement PriorityDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val priorityList = listOf("Low", "Medium", "High")
            val adapter = PriorityMenuAdapter(
                requireContext(),
                R.layout.item_priority_picker,
                priorityList = priorityList
            )
            builder.setAdapter(adapter) { dialogInterface, i ->
                when (i) {
                    0 -> listener.onPriorityLowClick()
                    1 -> listener.onPriorityMediumClick()
                    2 -> listener.onPriorityHighClick()
                    else -> listener.onPriorityLowClick()
                }
                dialogInterface.dismiss()
            }

            //builder.setView(inflater.inflate(R.layout.dialog_priority_picker, null))
            // Initialize binding variable that references [DialogPriorityPickerBinding] and
            // inflates the corresponding layout
            //val binding: DialogPriorityPickerBinding = DialogPriorityPickerBinding.inflate(
            //  LayoutInflater.from(activity)
            //)
            //binding.lifecycleOwner = activity
            //builder.setView(binding.root)

            /*// Handle Priority Low click
            binding.cardPriorityLow.setOnClickListener {
                DialogInterface.OnClickListener { dialogInterface, _ ->
                    listener.onPriorityLowClick()
                    dialogInterface.dismiss()
                }
            }
            // Handle Priority Medium click
            binding.cardPriorityMedium.setOnClickListener {
                DialogInterface.OnClickListener { dialogInterface, _ ->
                    listener.onPriorityMediumClick()
                    dialogInterface.dismiss()
                }
            }
            // Handle Priority High click
            binding.cardPriorityHigh.setOnClickListener {
                DialogInterface.OnClickListener { dialogInterface, _ ->
                    listener.onPriorityHighClick()
                    dialogInterface.dismiss()
                }
            }*/

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}