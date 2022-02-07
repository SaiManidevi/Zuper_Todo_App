package com.zuperinterviewtest.todo.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zuperinterviewtest.todo.R
import com.zuperinterviewtest.todo.adapters.TodoAdapter
import com.zuperinterviewtest.todo.databinding.FragmentTodoBinding
import com.zuperinterviewtest.todo.utils.PriorityDialogFragment
import com.zuperinterviewtest.todo.viewmodels.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoFragment : Fragment(), PriorityDialogFragment.PriorityDialogListener {
    private lateinit var binding: FragmentTodoBinding
    private lateinit var adapter: TodoAdapter
    private val viewmodel: TodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding for the TodoFragment layout
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        // To populate the options menu, set the [setHasOptionsMenu] to true
        setHasOptionsMenu(true)
        // To ensure that BottomSheet's peek height works properly,
        // set - isGestureInsetBottomIgnored to TRUE
        // Refer: [https://github.com/material-components/material-components-android/issues/1219]
        val createTaskBottomSheet =
            BottomSheetBehavior.from(binding.includedBottomSheet.bottomSheet)
        createTaskBottomSheet.isGestureInsetBottomIgnored = true
        // Display the priority picker dialog on click of the dropdown icon
        binding.includedBottomSheet.ivPriorityMenu.setOnClickListener {
            PriorityDialogFragment().show(
                childFragmentManager,
                "PriorityPicker"
            )
        }
        // Initialize the TodoAdapter
        adapter = TodoAdapter(viewmodel)
        binding.rvTodoList.adapter = adapter
        fetchAllTodos()
        return binding.root
    }

    private fun fetchAllTodos() {
        viewmodel.testValue.observe(viewLifecycleOwner) {
            Log.d("TODOTEST", it.toString())
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            viewmodel.getAllTodos().distinctUntilChanged().collectLatest {
                Log.d("TODOTEST", it.toString())
                adapter.submitData(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarTodoFragment as Toolbar?)
    }

    override fun onPriorityLowClick() {
        binding.includedBottomSheet.ivPriority.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.circle_solid_priority_low
            )
        )
    }

    override fun onPriorityMediumClick() {
        binding.includedBottomSheet.ivPriority.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.circle_solid_priority_medium
            )
        )
    }

    override fun onPriorityHighClick() {
        binding.includedBottomSheet.ivPriority.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.circle_solid_priority_high
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_tag_view -> {
                showTagView()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTagView() {
        findNavController().navigate(R.id.action_todoFragment_to_tagViewFragment)
    }
}