package com.example.flickrsearchapp.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

data class PhotoResponse(
    val photos: PresentationPhotosListResponse,
    @SerializedName("stat") val status: String
)

data class PresentationPhotosListResponse(
    val total: Long,
    @SerializedName("photo") val presentationPhotos: List<PresentationPhotoId>
)

data class PresentationPhotoId(val id: Long)
data class PhotoDetailsResponse(
    val photo: PhotoDetails,
    @SerializedName("stat") val status: String
)

private const val FLICKR_PHOTO_URL = "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg"

object PhotoSize {
    const val MEDIUM = "z"
}

data class PhotoDetails(
    val id: Long,
    @SerializedName("dateuploaded") val dateUploaded: Long,
    val title: PhotoTitle,
    val description: PhotoDescription,
    val server: String,
    val secret: String,
    val farm: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readParcelable(PhotoTitle::class.java.classLoader)!!,
        parcel.readParcelable(PhotoDescription::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()
    )

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is PhotoDetails)
            return false

        return id == other.id &&
                dateUploaded == other.dateUploaded &&
                title == other.title &&
                description == other.description
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + dateUploaded.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    fun getPhotoUrl(): String {
        return String.format(FLICKR_PHOTO_URL, farm, server, id, secret, PhotoSize.MEDIUM)
    }

    fun getUploadedDate(): String {
        return Date()
            .apply { time = dateUploaded }
            .toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(dateUploaded)
        parcel.writeParcelable(title, flags)
        parcel.writeParcelable(description, flags)
        parcel.writeString(server)
        parcel.writeString(secret)
        parcel.writeLong(farm)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoDetails> {
        override fun createFromParcel(parcel: Parcel): PhotoDetails {
            return PhotoDetails(parcel)
        }

        override fun newArray(size: Int): Array<PhotoDetails?> {
            return arrayOfNulls(size)
        }
    }
}

data class PhotoTitle(@SerializedName("_content") val content: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is PhotoTitle)
            return false

        return content == other.content
    }

    override fun hashCode(): Int {
        return content.hashCode()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoTitle> {
        override fun createFromParcel(parcel: Parcel): PhotoTitle {
            return PhotoTitle(parcel)
        }

        override fun newArray(size: Int): Array<PhotoTitle?> {
            return arrayOfNulls(size)
        }
    }
}

data class PhotoDescription(@SerializedName("_content") val content: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is PhotoDescription)
            return false

        return content == other.content
    }

    override fun hashCode(): Int {
        return content.hashCode()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoDescription> {
        override fun createFromParcel(parcel: Parcel): PhotoDescription {
            return PhotoDescription(parcel)
        }

        override fun newArray(size: Int): Array<PhotoDescription?> {
            return arrayOfNulls(size)
        }
    }
}