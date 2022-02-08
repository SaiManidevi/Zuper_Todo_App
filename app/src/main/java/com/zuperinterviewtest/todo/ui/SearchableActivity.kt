package com.zuperinterviewtest.todo.ui

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.zuperinterviewtest.todo.R
import com.zuperinterviewtest.todo.adapters.TodoAdapter
import com.zuperinterviewtest.todo.viewmodels.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Currently not using this Searchable Activity, although for future scaling,
 * this activity can be used to display search results in a separate screen along with
 * suggestions and recent searches
 */

@AndroidEntryPoint
class SearchableActivity : AppCompatActivity() {
    private val viewmodel: TodoViewModel by viewModels()
    private lateinit var adapter: TodoAdapter
    private lateinit var binding: com.zuperinterviewtest.todo.databinding.ActivitySearchableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searchable)
        // Initialize the TodoAdapter
        adapter = TodoAdapter(viewmodel)
        binding.rvSearchTodolist.adapter = adapter
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                this.lifecycleScope.launch(Dispatchers.Default) {
                    viewmodel.getAllTodosByTag(searchTag = query).distinctUntilChanged()
                        .collectLatest {
                            adapter.submitData(it)
                        }
                }
            }
        }
    }
}