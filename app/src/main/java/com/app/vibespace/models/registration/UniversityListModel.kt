package com.app.vibespace.models.registration

data class UniversityListModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val totalCount: Int,
        val universities: List<University>
    ){
        data class University(
            val _id: String,
            val insertDate: Int,
            val universityName: String
        )
    }
}