package com.example.lightmagic

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import com.example.lightmagic.blinker.Torch


class CameraTorch(context: Context) : Torch {
    private val cameraManager     : CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private lateinit var cameraId : String

    private var _lightOn : Boolean = false

    var lightEnabled : Boolean = false
        private set

    var lightOn : Boolean = _lightOn
        get() = lightEnabled && _lightOn
        private set

    var lightListener : LightListener? = null


    init {
        for (cameraId in cameraManager.cameraIdList) {
            lightEnabled = hasFlash(cameraId)

            if (lightEnabled) {
                this.cameraId = cameraId
                break
            }
        }

        initTorchCallback()
    }


    override fun on() {
        if (!_lightOn)
            setLight(true)
    }

    override fun off() {
        if (_lightOn)
            setLight(false)
    }


    fun setLightListener(listener : (Boolean) -> Unit) {
        lightListener = object : LightListener {
            override fun onLightChanged(on: Boolean) {
                listener(on)
            }
        }
    }


    private fun hasFlash(cameraId: String) : Boolean {
        return try {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        } catch (e : CameraAccessException) {
            false
        }
    }


    private fun initTorchCallback() {
        if (lightEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager.registerTorchCallback(object : CameraManager.TorchCallback() {

                override fun onTorchModeChanged(id: String, enabled: Boolean) {
                    if (cameraId == id) {
                        _lightOn = enabled
                        lightListener?.onLightChanged(_lightOn)
                    }
                }
            }, null)
        }
    }

    private fun setLight(on: Boolean) {
        if (lightEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager.setTorchMode(cameraId, on)
        }
    }


    interface LightListener {
        fun onLightChanged(on : Boolean)
    }
}