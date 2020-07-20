package com.eastnine.livelifecycle.util

import android.content.Context
import android.graphics.*
import android.net.ConnectivityManager
import android.opengl.GLES30
import android.os.Build
import android.util.TypedValue
import androidx.annotation.DimenRes
import kotlin.math.ceil

object Util {
    //디바이스 너비
    fun getDeviceWidth(context: Context): Int {
        val resources = context.resources
        val dm = resources.displayMetrics
        return dm.widthPixels
    }
    //디바이스 높이
    fun getDeviceHeight(context: Context): Int {
        val resources = context.resources
        val dm = resources.displayMetrics

        return dm.heightPixels
    }
    //상태바 높이
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0)
            resources.getDimensionPixelSize(resourceId)
        else
            ceil(((if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) 24 else 25) * resources.displayMetrics.density).toDouble()).toInt()
    }
    //네비게이션바 높이
    fun getNavigationHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    //dp값을 pixel값으로 전환
    fun dpToPixel(context: Context, size: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size.toFloat(), context.resources.displayMetrics).toInt()
    }

    //dp값을 pixel값으로 전환
    fun dpToPixel(context: Context, size: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics)
    }

    //dp값을 pixel값으로 전환
    fun pixelToDp(context: Context, pixel: Float): Float {
        val metrics = context.resources.displayMetrics
        return pixel / (metrics.densityDpi / 160f)
    }

    //dp값을 pixel값으로 전환
    fun pixelToDp(context: Context, pixel: Int): Int {
        val metrics = context.resources.displayMetrics
        return (pixel.toFloat() / (metrics.densityDpi / 160f)).toInt()
    }

    //dimen값을 pixel값으로 전환
    fun dimenToDp(context: Context, @DimenRes dimen: Int): Float {
        return context.resources.getDimension(dimen) / context.resources.displayMetrics.density
    }

    // network 연결 상태 확인
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnected
    }

    //text의 높이
    fun getTextHeight(context: Context, typeface: Typeface? = null, text: String, textSize: Float, layoutSize: Int): Int {
        var mTypeface = typeface
        if(mTypeface == null) {
            mTypeface = Typeface.DEFAULT
        }
        val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.typeface = mTypeface
        mPaint.color = Color.parseColor("#000000")
        mPaint.textSize = textSize

        val rect = Rect()
        mPaint.getTextBounds(text, 0, text.length, rect)

        val lines = ((rect.height().toFloat() + dpToPixel(context, 4)) / layoutSize.toFloat()).toInt() +1
        val textHeight = textSize + context.resources.getDimension(R.dimen.text_spacing)
        return textHeight.toInt() * lines
    }

    //text의 너비
    fun getTextWidth(typeface: Typeface? = null, text: String, textSize: Float): Int {
        var mTypeface = typeface
        if(mTypeface == null) {
            mTypeface = Typeface.DEFAULT
        }
        val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.typeface = mTypeface
        mPaint.color = Color.parseColor("#000000")
        mPaint.textSize = textSize

        val rect = Rect()
        mPaint.getTextBounds(text, 0, text.length, rect)

        return rect.width()
    }

    //이미지 최대 메모리 넘지 않도록 크기 재설정
    fun resizeBitmap(context: Context, bitmap: Bitmap): Bitmap {
        val max = GLES30.GL_MAX_TEXTURE_SIZE

        if(bitmap.width > max || bitmap.height > max) {
            val resizedWidth: Int
            val resizedHeight: Int

            if(bitmap.width > bitmap.height) {
                val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

                resizedHeight = getDeviceHeight(context)
                resizedWidth = (resizedHeight.toDouble() * aspectRatio.toDouble()).toInt()
            } else {
                val aspectRatio = bitmap.height.toFloat() / bitmap.width.toFloat()
                resizedWidth = getDeviceWidth(context)
                resizedHeight = (resizedWidth.toDouble() * aspectRatio.toDouble()).toInt()
            }

            return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
        } else {
            return bitmap
        }
    }
}