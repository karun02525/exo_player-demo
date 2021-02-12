package io.chingari.finalexoplayer

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import io.chingari.finalexoplayer.lib.youtube.YouTubeOverlay
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.exo_playback_control_view_yt.*

class MainActivity : BaseVideoActivity() {
    private var isVideoFullscreen = false

   private var fullscreens:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.videoPlayer = previewPlayerView

        releasePlayer()
        initializePlayer()
        ytOverlay.player(player!!)


        buildMediaSource(Uri.parse("https://media.chingari.io/uploads/863fc92f-20e8-4a5e-9fc6-856bdfd60c8d-1610723727970/webpath_863fc92f-20e8-4a5e-9fc6-856bdfd60c8d-1610723727970.mp4"))
        initDoubleTapPlayerView()

        fullscreen_button.setOnClickListener {
            if (fullscreens) {
                fullscreen_button.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_fullscreen_24)
                )
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
               /* if (supportActionBar != null) {
                    supportActionBar!!.show()
                }*/

                previewPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL;


                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                val params = previewPlayerView.layoutParams as FrameLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                previewPlayerView.layoutParams = params
                fullscreens = false
            } else {
                fullscreen_button.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_baseline_fullscreen_24)
                )
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                if (supportActionBar != null) {
                    supportActionBar!!.hide()
                }

                previewPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                val params = previewPlayerView.layoutParams as FrameLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                previewPlayerView.layoutParams = params
                fullscreens = true
            }
        }
        
    }




    private fun initDoubleTapPlayerView() {
        ytOverlay
                // Uncomment this line if the DoubleTapPlayerView is not set via XML
                //.playerView(previewPlayerView)
                .performListener(object : YouTubeOverlay.PerformListener {
                    override fun onAnimationStart() {
                        previewPlayerView.useController = false
                        ytOverlay.visibility = View.VISIBLE
                    }
                    override fun onAnimationEnd() {
                        ytOverlay.visibility = View.GONE
                        previewPlayerView.useController = true
                    }
                })

        previewPlayerView.doubleTapDelay = 200
        // Uncomment this line if the PlayerDoubleTapListener is not set via XML
        // previewPlayerView.controller(ytOverlay)
    }

    private fun toggleFullscreen() {
        if (isVideoFullscreen) {
            setFullscreen(false)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
            if(supportActionBar != null){
                supportActionBar?.show();
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            isVideoFullscreen = false
        } else {
            setFullscreen(true)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

            if(supportActionBar != null){
                supportActionBar?.hide();
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            isVideoFullscreen = true
        }
    }

    override fun onBackPressed() {
        if (isVideoFullscreen) {
            toggleFullscreen()
            return
        }
        super.onBackPressed()
    }

}