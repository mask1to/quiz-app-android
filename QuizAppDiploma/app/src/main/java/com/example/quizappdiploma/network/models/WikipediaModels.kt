package com.example.quizappdiploma.network.models

data class WikipediaSummaryResponse(
    val title: String,
    val extract: String?,
    val thumbnail: WikipediaThumbnail?
)

data class WikipediaThumbnail(
    val source: String?
)

// Wikipedia Action API (full article extract)
data class WikipediaActionResponse(
    val query: WikipediaActionQuery?
)

data class WikipediaActionQuery(
    val pages: Map<String, WikipediaActionPage>?
)

data class WikipediaActionPage(
    val title: String?,
    val extract: String?
)
