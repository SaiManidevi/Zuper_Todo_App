package com.zuperinterviewtest.todo.utils

object Constants {
    // Constants for the TodoResults URL - refer:
    // // http://167.71.235.242:3000/todo?_page=1&_limit=15&author=Manidevi
    const val TODO_BASE_URL = "http://167.71.235.242:3000/"
    const val PATH_TODO = "todo"
    const val QUERY_PAGE = "_page"
    const val QUERY_LIMIT = "_limit"
    const val QUERY_AUTHOR = "author"
    const val AUTHOR = "Manidevi"
    const val TAG = "tag"
    enum class PRIORITY{
        LOW,
        MEDIUM,
        HIGH
    }
}