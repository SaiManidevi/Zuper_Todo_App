package com.zuperinterviewtest.todo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.zuperinterviewtest.todo.adapters.TagViewAdapter
import com.zuperinterviewtest.todo.databinding.FragmentTagViewBinding
import com.zuperinterviewtest.todo.viewmodels.TagViewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TagViewFragment : Fragment() {
    private lateinit var binding: FragmentTagViewBinding
    private lateinit var adapter: TagViewAdapter
    private val viewModel: TagViewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding for the TagViewFragment layout
        binding = FragmentTagViewBinding.inflate(inflater, container, false)
        adapter = TagViewAdapter()
        binding.rvTagViewFragment.adapter = adapter
        fetchAllTodos()
        observeTodoList()
        return binding.root
    }

    private fun observeTodoList() {
        viewModel.allTodos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun fetchAllTodos() {
        viewModel.getAllTodos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarTagviewFragment as Toolbar?)
    }

}