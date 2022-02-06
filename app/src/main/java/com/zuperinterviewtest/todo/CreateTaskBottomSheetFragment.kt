package com.zuperinterviewtest.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zuperinterviewtest.todo.adapters.PriorityMenuAdapter
import com.zuperinterviewtest.todo.databinding.FragmentCreateTaskBottomSheetBinding

class CreateTaskBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCreateTaskBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBottomSheetBinding.inflate(inflater, container, false)
        val priorityList = listOf("Low", "Medium", "High")
        val adapter = PriorityMenuAdapter(
            requireContext(),
            R.layout.item_priority_picker,
            priorityList = priorityList
        )

        binding.ivPriorityMenu.setOnClickListener {
            showMenu(it, R.menu.priority_menu)
            //popupWindow.showAsDropDown(it, -153, 0)
        }
        //binding.autoCompleteTextViewPriority.setAdapter(adapter)
        // Inflate the layout for this fragment
        return binding.root
    }

    /* private fun inflatePopUpWindow() {
         val view =
             requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
         val popupBind = ItemPriorityPickerBinding.inflate(view)
         val popupWindow = PopupWindow(
             popupBind.root, 126.fromDpToPx.toInt(),
             89.fromDpToPx.toInt(), true
         ).apply { contentView.setOnClickListener { dismiss() } }

         ---
         val view =
             requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
         val popupBind = ItemPriorityPickerBinding.inflate(view)
         val popupWindow = PopupWindow(
             popupBind.root, ConstraintLayout.LayoutParams.WRAP_CONTENT,
             ConstraintLayout.LayoutParams.WRAP_CONTENT, true
         ).apply { contentView.setOnClickListener { dismiss() } }
     }
 */
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            return@setOnMenuItemClickListener true
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

}