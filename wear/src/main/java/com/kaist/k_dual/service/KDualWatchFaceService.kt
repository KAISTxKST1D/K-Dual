package com.kaist.k_dual.service

import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.kaist.k_dual.watchface.KDualCanvasRenderer

class KDualWatchFaceService : WatchFaceService() {
    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        val renderer = KDualCanvasRenderer(
            applicationContext,
            surfaceHolder = surfaceHolder,
            currentUserStyleRepository = currentUserStyleRepository,
            watchState = watchState,
            canvasType = CanvasType.HARDWARE
        )

        return WatchFace(
            watchFaceType = WatchFaceType.ANALOG,
            renderer = renderer
        ).setTapListener(tapListener = renderer)
    }
}