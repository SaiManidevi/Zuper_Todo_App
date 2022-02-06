package com.zuperinterviewtest.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.PEEK_HEIGHT_AUTO
import com.zuperinterviewtest.todo.adapters.PriorityMenuAdapter
import com.zuperinterviewtest.todo.databinding.FragmentTodoBinding

class TodoFragment : Fragment(), PriorityDialogFragment.PriorityDialogListener {
    private lateinit var binding: FragmentTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        val priorityList = listOf("Low", "Medium", "High")
        val adapter = PriorityMenuAdapter(
            requireContext(),
            R.layout.item_priority_picker,
            priorityList = priorityList
        )

        val createTaskBottomSheet =
            BottomSheetBehavior.from(binding.includedBottomSheet.bottomSheet)

        createTaskBottomSheet.isGestureInsetBottomIgnored = true

        binding.includedBottomSheet.ivPriorityMenu.setOnClickListener {
            PriorityDialogFragment().show(
                childFragmentManager,
                "PriorityPicker"
            )
            //showMenu(it, R.menu.priority_menu)
        }


        return binding.root
    }

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

    override fun onPriorityLowClick() {
        binding.includedBottomSheet.ivPriority.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.circle_solid_priority_low
            )
        )
        Toast.makeText(context, "Low clicked!", Toast.LENGTH_SHORT).show()
    }

    override fun onPriorityMediumClick() {
        binding.includedBottomSheet.ivPriority.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.circle_solid_priority_medium
            )
        )
        Toast.makeText(context, "Medium clicked!", Toast.LENGTH_SHORT).show()
    }

    override fun onPriorityHighClick() {
        binding.includedBottomSheet.ivPriority.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.circle_solid_priority_high
            )
        )
        Toast.makeText(context, "High clicked!", Toast.LENGTH_SHORT).show()
    }

}