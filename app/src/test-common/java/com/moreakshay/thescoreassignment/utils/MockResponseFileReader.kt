package com.moreakshay.thescoreassignment.utils

import java.io.InputStreamReader

class MockResponseFileReader(fileName: String) {
    val content: String
    init {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val reader = InputStreamReader(inputStream)
        content = reader.readText()
        reader.close()
    }
}