package com.example.flickrsearchapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flickrsearchapp.R
import com.example.flickrsearchapp.databinding.FragmentImageDetailsBinding
import com.example.flickrsearchapp.domain.PhotoDetails
import com.squareup.picasso.Picasso

class ImageDetailsFragment : Fragment() {
    companion object {
        val TAG = ImageDetailsFragment::class.java.simpleName
        private const val PHOTO_DETAILS_KEY = "idf.photo_details.key"

        fun newInstance(photoDetails: PhotoDetails): Fragment {
            val bundle = Bundle().also {
                it.putParcelable(PHOTO_DETAILS_KEY, photoDetails)
            }

            return ImageDetailsFragment().also {
                it.arguments = bundle
            }
        }
    }

    private var binding: FragmentImageDetailsBinding? = null
    private lateinit var photoDetails: PhotoDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoDetails = arguments?.getParcelable(PHOTO_DETAILS_KEY)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageDetailsBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.post { showPhotoDetails() }
    }

    private fun showPhotoDetails() {
        val width = binding?.photoImage?.width!!

        binding?.title?.text =
            resources.getString(R.string.photo_title, photoDetails.title.content)
        binding?.description?.text =
            resources.getString(R.string.photo_description, photoDetails.description.content)
        binding?.dateUploaded?.text =
            resources.getString(R.string.photo_date_uploaded, photoDetails.getUploadedDate())
        Picasso.get()
            .load(photoDetails.getPhotoUrl())
            .resize(width, width)
            .centerCrop()
            .into(binding?.photoImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}