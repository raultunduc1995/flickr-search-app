package com.example.flickrsearchapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flickrsearchapp.App
import com.example.flickrsearchapp.FlickrViewModel
import com.example.flickrsearchapp.R
import com.example.flickrsearchapp.databinding.FragmentMainBinding
import com.example.flickrsearchapp.domain.PhotoDetails
import com.example.flickrsearchapp.utils.*
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MainFragment : Fragment() {
    @Inject
    lateinit var viewModel: FlickrViewModel

    @Inject
    lateinit var compositeDisposable: LifecycleAwareCompositeDisposable

    private lateinit var imm: InputMethodManager
    private lateinit var imageAdapter: ImageAdapter
    private var binding: FragmentMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        compositeDisposable.attachLifecycle(lifecycle)
        lifecycle.addObserver(compositeDisposable)
        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initImageList()
        binding?.searchButton?.setOnClickListener {
            imm.hideSoftInputFromWindow(binding?.searchInput?.windowToken, 0)
            startImageSearch()
        }
        viewModel.searchPhotosErrorLiveData.observe(viewLifecycleOwner, { onPhotoSearchError() })
    }

    private fun initImageList() {
        imageAdapter = ImageAdapter { showImageDetails(it) }
        binding?.foundItemsRecyclerList?.apply {
            adapter = imageAdapter
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.flickr_grid_list_span_count),
                StaggeredGridLayoutManager.VERTICAL
            )
        }
        viewModel.photosLiveData.observe(viewLifecycleOwner, { photos ->
            binding?.progressBar?.remove()
            binding?.noPhotosFoundText?.let {
                if (photos.isEmpty())
                    it.show()
                else
                    it.remove()
            }
            imageAdapter.submitList(photos)
            imageAdapter.notifyDataSetChanged()
        })
    }

    private fun showImageDetails(photoDetails: PhotoDetails) {
        (activity as AppCompatActivity)
            .replaceFragment(
                R.id.fragments_container,
                ImageDetailsFragment.newInstance(photoDetails),
                shouldAddToBackStack = true,
                fragmentTag = ImageDetailsFragment.TAG
            )
    }

    private fun startImageSearch() {
        val searchText = binding?.searchInput?.text?.toString()

        if (searchText == null || searchText.isEmpty())
            return

        showProgress()
        compositeDisposable.subscribe(
            viewModel.searchPhotosByName(searchText)
                .defaultSchedulers()
                .subscribe({}, {})
        )
    }

    private fun showProgress() {
        binding?.apply {
            noPhotosFoundText.remove()
            progressBar.show()
        }
    }

    private fun onPhotoSearchError() {
        binding?.apply {
            progressBar.remove()
            noPhotosFoundText.show()
            imageAdapter.submitList(emptyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
        lifecycle.removeObserver(compositeDisposable)
    }
}

class ImageAdapter(val onClickListener: (PhotoDetails) -> Unit) :
    ListAdapter<PhotoDetails, ImageAdapter.ImageViewHolder>(PhotoIdDiffCallback) {
    inner class ImageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageTitle: TextView = view.findViewById(R.id.image_title)
        val imageThumbnail: ImageView = view.findViewById(R.id.image_thumbnail)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_details_row_item, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int
    ) {
        val photoDetails = getItem(position)

        holder.view.setOnClickListener { onClickListener.invoke(photoDetails) }
        holder.imageTitle.text = photoDetails.title.content
        Picasso.get()
            .load(photoDetails.getPhotoUrl())
            .into(holder.imageThumbnail)
    }
}

object PhotoIdDiffCallback : DiffUtil.ItemCallback<PhotoDetails>() {
    override fun areItemsTheSame(
        oldItem: PhotoDetails,
        newItem: PhotoDetails
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PhotoDetails,
        newItem: PhotoDetails
    ): Boolean {
        return oldItem == newItem
    }

}