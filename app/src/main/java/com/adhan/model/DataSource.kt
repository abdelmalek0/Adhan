package com.adhan.model

import com.adhan.R

class DataSource {
    companion object {
        fun createdataSet(): ArrayList<Audio>{
            val list = ArrayList<Audio>()
            list.add(Audio(
                R.raw.azan1
            ))
            list.add(Audio(
                R.raw.azan2
            ))
            list.add(Audio(
                R.raw.azan3
            ))
            list.add(Audio(
                R.raw.azan4
            ))
            list.add(Audio(
                R.raw.azan5
            ))
            list.add(Audio(
                R.raw.azan6
            ))
            list.add(Audio(
                R.raw.azan7
            ))
            return list
        }
    }
}