package com.zuperinterviewtest.todo.ui

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
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
        setUpSearchViewListener()
        setUpAdapterLoadStateListener()
        // [Not using it now - since Inline-Search is required]
        // Get the SearchView and set the searchable configuration
        // setUpSearchableConfig()
        return binding.root
    }

    private fun setUpAdapterLoadStateListener() {
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading) {
                recyclerViewVisibilityState(
                    showRecyclerView = false,
                    lottieAnimFile = R.raw.loading
                )
            } else recyclerViewVisibilityState(showRecyclerView = true)

            if (loadState.source.refresh is LoadState.NotLoading && adapter.itemCount < 1) {
                recyclerViewVisibilityState(
                    showRecyclerView = false,
                    lottieAnimFile = R.raw.empty_box
                )
            } else recyclerViewVisibilityState(showRecyclerView = true)
        }
    }

    private fun setUpSearchViewListener() {
        binding.searchTasks.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Now that submit is clicked, keyboard will be hidden so display bottomSheet
                bottomSheetVisibilityState(showBottomSheet = true)
                query?.let { fetchTodoByTag(query) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Keyboard is currently visible, so hide the bottomSheet
                bottomSheetVisibilityState(showBottomSheet = false)
                newText?.let {
                    if (newText.length > 3) fetchTodoByTag(newText)
                    if (newText.isEmpty()) {
                        // User cleared the search text, so display all results & show bottomSheet
                        fetchAllTodos()
                        bottomSheetVisibilityState(showBottomSheet = true)
                    }
                }
                if (newText == null) {
                    // Somethings wrong, show display all results & show bottomSheet
                    fetchAllTodos()
                    bottomSheetVisibilityState(showBottomSheet = true)
                }
                return false
            }
        })
    }

    private fun fetchTodoByTag(searchTag: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            viewmodel.getAllTodosByTag(searchTag = searchTag).distinctUntilChanged()
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    private fun fetchAllTodos() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            viewmodel.getAllTodos().distinctUntilChanged().collectLatest {
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

    private fun bottomSheetVisibilityState(showBottomSheet: Boolean) {
        if (showBottomSheet) {
            binding.includedBottomSheet.bottomSheet.visibility = View.VISIBLE
        } else {
            binding.includedBottomSheet.bottomSheet.visibility = View.GONE
        }
    }

    private fun recyclerViewVisibilityState(
        showRecyclerView: Boolean,
        @RawRes lottieAnimFile: Int = R.raw.loading
    ) {
        if (showRecyclerView) {
            // Show Recyclerview & hide Lottie Animation
            binding.lottieAnim.pauseAnimation()
            binding.lottieAnim.visibility = View.GONE
            binding.rvTodoList.visibility = View.VISIBLE
        } else {
            // Hide recyclerview & show Lottie Animation
            binding.lottieAnim.visibility = View.VISIBLE
            binding.rvTodoList.visibility = View.GONE
            binding.lottieAnim.setAnimation(lottieAnimFile)
            binding.lottieAnim.playAnimation()
        }
    }


    /**
     * Helper function to set up Searchbar configuration to display search results in another screen
     * Refer [SearchableActivity] to know more, this is not added currently.
     */
    /*private fun setUpSearchableConfig() {
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchTasks.apply {
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    ComponentName(
                        requireActivity().applicationContext,
                        SearchableActivity::class.java
                    )
                )
            )
        }
    }*/
}