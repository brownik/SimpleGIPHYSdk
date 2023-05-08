package com.brownik.gifmessaging

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import com.brownik.gifmessaging.databinding.FragmentGiphyBinding
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GiphyLoadingProvider
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.views.GPHGridCallback
import com.giphy.sdk.ui.views.GiphyGridView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.selects.select

class GiphyFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentGiphyBinding
    var result: ((Media) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiphyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gridContainer.apply {
            direction = GiphyGridView.VERTICAL
            spanCount = 2
        }.apply {
            content = GPHContent.trendingGifs // 띄울 타입
            setGiphyLoadingProvider(loadingProviderClient) // gif 로딩 애니메이션
            callback = object : GPHGridCallback {
                override fun contentDidUpdate(resultCount: Int) {
                    Log.d("qwe123", "contentDidUpdate: $resultCount")
                }

                override fun didSelectMedia(media: Media) {
                    result?.invoke(media)
                    dismiss()
                }
            }
        }
        setOnClickListener()
    }

    private val loadingProviderClient = object : GiphyLoadingProvider {
        override fun getLoadingDrawable(position: Int): Drawable {
            return LoadingDrawable(if (position % 2 == 0) LoadingDrawable.Shape.Rect else LoadingDrawable.Shape.Circle)
        }
    }

    private fun setOnClickListener() = with(binding) {
        btnSearch.setOnClickListener {
            gridContainer.content =
                GPHContent.searchQuery(etSearch.text.toString(), MediaType.gif)
        }
    }
}