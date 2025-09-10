package com.example.satelliteapp.presentation.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.satelliteapp.R
import com.example.satelliteapp.databinding.FragmentListBinding
import com.example.satelliteapp.util.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var viewBinding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels()

    @Inject
    lateinit var adapter: SatelliteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        viewBinding.satelliteList.apply {
            adapter = this@ListFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
        }
    }

    private fun setupSearchBar() {
        viewBinding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.onSearchTextChanged(it) }
                viewBinding.searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.onSearchTextChanged(it) }
                return true
            }
        })
    }

    private fun observeViewModel() {
        collectLatestLifecycleFlow(viewModel.state) { uiState ->
            when {
                uiState.isLoading -> viewBinding.loadingIndicator.visibility = View.VISIBLE
                uiState.error != null -> {
                    viewBinding.loadingIndicator.visibility = View.GONE
                    adapter.listDiffer.submitList(emptyList())
                    viewBinding.listError.apply {
                        text = uiState.error
                        visibility = View.VISIBLE
                    }
                }
                else -> {
                    viewBinding.loadingIndicator.visibility = View.GONE
                    adapter.listDiffer.submitList(uiState.data)
                    viewBinding.listError.visibility = View.GONE
                }
            }
        }
    }
}
