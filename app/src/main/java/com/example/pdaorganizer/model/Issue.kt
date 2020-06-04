package com.example.pdaorganizer.model

data class Issue(val id: Int = -1, val name: String, val user: Int, val category: String, val description: String, val importance: String, val deadline: String) {
}