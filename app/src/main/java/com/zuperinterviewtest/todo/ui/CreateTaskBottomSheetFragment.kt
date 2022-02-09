package com.zuperinterviewtest.todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zuperinterviewtest.todo.R
import com.zuperinterviewtest.todo.databinding.FragmentCreateTaskBottomSheetBinding

class CreateTaskBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCreateTaskBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBottomSheetBinding.inflate(inflater, container, false)
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

}