package com.example.videoplayer.modules.ui

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.videoplayer.R
import com.example.videoplayer.databinding.ActivityMainBinding
import com.example.videoplayer.modules.ui.adapter.SpacePagerSnapHelper
import com.example.videoplayer.modules.ui.adapter.VideoListAdapter
import com.example.videoplayer.modules.viewModel.VideoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<VideoListViewModel>()
    private val layoutManager: LinearLayoutManager? = null

    private val adapter =
        VideoListAdapter { absoluteAdapterPosition ->
            binding.rvVideo.scrollToPosition(absoluteAdapterPosition + 1)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        initRecyclerView()
        observeData()
    }

    private fun initRecyclerView() {
        binding.rvVideo.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
            val pagerSnapHelper = PagerSnapHelper()
            val spacePagerSnapHelper = SpacePagerSnapHelper(pagerSnapHelper)
            pagerSnapHelper.attachToRecyclerView(this)
            spacePagerSnapHelper.attachToRecyclerView(this)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.getVideoListPaging.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}