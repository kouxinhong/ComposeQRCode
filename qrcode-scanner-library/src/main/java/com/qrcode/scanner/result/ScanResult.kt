package com.qrcode.scanner.result

import android.os.Parcel
import android.os.Parcelable

/**
 * 扫描结果封装类
 */
sealed class ScanResult : Parcelable {
    /**
     * 扫描成功
     * @param content 扫描到的内容
     */
    data class Success(val content: String) : ScanResult()

    /**
     * 用户取消扫描
     */
    data object Cancelled : ScanResult()

    /**
     * 扫描错误
     * @param message 错误信息
     */
    data class Error(val message: String) : ScanResult()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        when (this) {
            is Success -> {
                parcel.writeInt(0)
                parcel.writeString(content)
            }
            is Cancelled -> {
                parcel.writeInt(1)
            }
            is Error -> {
                parcel.writeInt(2)
                parcel.writeString(message)
            }
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ScanResult> {
        override fun createFromParcel(parcel: Parcel): ScanResult {
            return when (parcel.readInt()) {
                0 -> Success(parcel.readString() ?: "")
                1 -> Cancelled
                2 -> Error(parcel.readString() ?: "")
                else -> Error("Unknown result type")
            }
        }

        override fun newArray(size: Int): Array<ScanResult?> {
            return arrayOfNulls(size)
        }
    }
}