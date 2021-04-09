package com.harshaapps.onestopforvi

import android.app.Application

class OneStopForVI:Application()
{
    override fun onTerminate() {
        super.onTerminate()
        cacheDir.delete()
    }

}