package com.example.flickrsearchapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flickrsearchapp.App
import com.example.flickrsearchapp.FlickrViewModel
import com.example.flickrsearchapp.R
import com.example.flickrsearchapp.databinding.FragmentMainBinding
import javax.inject.Inject

class MainFragment : Fragment() {
    @Inject
    lateinit var viewModel: FlickrViewModel

    private lateinit var imageAdapter: ImageAdapter
    private var binding: FragmentMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageAdapter = ImageAdapter(mutableListOf())
        binding?.foundItemsRecyclerList?.apply {
            adapter = imageAdapter
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.flickr_grid_list_span_count),
                StaggeredGridLayoutManager.VERTICAL
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

class ImageAdapter(val data: MutableList<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageTitle: TextView = view.findViewById(R.id.image_title)
        val imageThumbnail: ImageView = view.findViewById(R.id.image_thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_details_row_item, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is ImageViewHolder)
            return

        holder.imageThumbnail.setImageResource(R.drawable.ic_launcher_background)
    }

    override fun getItemCount(): Int = data.size + 10
}