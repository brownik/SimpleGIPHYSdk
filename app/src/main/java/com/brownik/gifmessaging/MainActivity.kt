package com.brownik.gifmessaging

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.brownik.gifmessaging.databinding.ActivityMainBinding
import com.facebook.drawee.backends.pipeline.Fresco
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.themes.GPHCustomTheme
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var gifsDialog: GiphyDialogFragment? = null

    private val gifListAdapter: GifListAdapter by lazy {
        GifListAdapter()
    }

    private val list = mutableListOf<Media>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configGiphy()
        initFresco()
        setListAdapter()
        addOnClickListener()
    }

    private fun initFresco() = Fresco.initialize(this)

    private fun configGiphy() {
        Giphy.configure(this@MainActivity, "HFb5BOI8pV8ofOFdRupatLQ4wdVbfAfa")
    }


    private fun setListAdapter() {
        binding.gifList.adapter = gifListAdapter
    }

    private fun addOnClickListener() = with(binding) {
        openGiphy.setOnClickListener {

            GPHCustomTheme.channelColor = 0xFF000000.toInt() // 파악 안됨
            GPHCustomTheme.handleBarColor = 0xFFAAAAAA.toInt() // 상단 핸들링 바
            GPHCustomTheme.backgroundColor = 0xFF000000.toInt() // 프래그먼트 배경
            GPHCustomTheme.dialogOverlayBackgroundColor = 0xFF000000.toInt() // 이미지 길게 눌렀을 때 배경
            GPHCustomTheme.textColor = 0xFF000000.toInt() // 미확인 안됨
            GPHCustomTheme.activeTextColor = 0xFF000000.toInt() // 확인 안됨
            GPHCustomTheme.imageColor = 0xFFFFFFFF.toInt() // 그룹 버튼
            GPHCustomTheme.activeImageColor = 0xFFCCCCCC.toInt() // 활성 그룹 버튼
            GPHCustomTheme.searchBackgroundColor = 0xFF000000.toInt() // 검색 창 배경
            GPHCustomTheme.searchQueryColor = 0xFFFFFFFF.toInt() // 검색 창 글자
            GPHCustomTheme.suggestionBackgroundColor = 0xFFFFFFFF.toInt() // 파악 안됨
            GPHCustomTheme.moreByYouBackgroundColor = 0xFFFFFFFF.toInt() // 파악 안됨
            GPHCustomTheme.backButtonColor = 0xFFFFFFFF.toInt() // 뒤로가기 버튼

            val settings = GPHSettings().apply {
                theme = GPHTheme.Custom
                mediaTypeConfig =
                    arrayOf(GPHContentType.gif, GPHContentType.sticker, GPHContentType.recents)
                showCheckeredBackground = false
                stickerColumnCount = 4
                showSuggestionsBar = false
            }

            gifsDialog = GiphyDialogFragment.newInstance(settings)

            gifsDialog?.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                @SuppressLint("LogNotTimber")
                override fun didSearchTerm(term: String) {
                    Log.d("qwe123", "term: $term")
                }

                override fun onDismissed(selectedContentType: GPHContentType) {
                    gifsDialog = null
                }

                @SuppressLint("LogNotTimber")
                override fun onGifSelected(
                    media: Media,
                    searchTerm: String?,
                    selectedContentType: GPHContentType,
                ) {
                    Log.d(
                        "qwe123",
                        "networkContent: ${media.images.original?.gifUrl}\n" +
                                "fixedHeightDownsampled: ${media.images.fixedHeightDownsampled?.gifUrl}"
                    )
                    updateList(media)
                }
            }
            gifsDialog?.show(supportFragmentManager, "gifsDialog")
        }

        openFragment.setOnClickListener {
            val giphyDialogFragment = GiphyFragment().apply {
                result = {
                    updateList(it)
                }
            }
            giphyDialogFragment.show(supportFragmentManager, "giphyDialogFragment")
        }
    }

    private fun updateList(media: Media) {
        list.add(media)
        gifListAdapter.submitList(list.reversed())
        binding.gifList.smoothScrollToPosition(0)
    }
}