package com.blueskylct.eread.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {
    /**
     * @author Blueskylct
     * @since 2025/3/29
     * 检查并请求读写权限
     */
    fun checkPermission(activity: Activity, permission: String, requestCode: Int): Boolean =
        checkPermission(activity, arrayOf(permission), requestCode)

    private fun checkPermission(activity: Activity, permissions: Array<String>, requestCode: Int): Boolean{
        var result = true
        var check = PackageManager.PERMISSION_GRANTED
        for (permission in permissions){
            check = ContextCompat.checkSelfPermission(activity, permission)
            if (check != PackageManager.PERMISSION_GRANTED)
                break
        }
        if (check != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
            result = false
        }
        return result
    }

    /**
     * @author Blueskylct
     * @since 2025/3/29
     * 检查权限请求结果
     */
    fun checkGrant(grantResult: IntArray): Boolean{
        var result = true
        for (grant in grantResult){
            if (grant != PackageManager.PERMISSION_GRANTED)
                result = false
        }
        return result
    }
}