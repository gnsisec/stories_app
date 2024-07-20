package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.remote.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..14) {
            val quote = ListStoryItem(
                i.toString(),
                "http://www.photo_url_$i",
                "created at $i",
                "name $i",
                "description $i",
                null, null
            )
            items.add(quote)
        }
        return items
    }
}