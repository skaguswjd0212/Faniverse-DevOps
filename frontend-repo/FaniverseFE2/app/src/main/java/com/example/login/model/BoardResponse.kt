package com.example.login.model


data class BoardResponse(
    val boardId: Long,
    val name: String,
    val description: String? = null // 게시판 설명이 있을 수 있다고 가정
) {
    companion object {
        fun fromEntity(board: BoardResponse): BoardResponse {
            return BoardResponse(
                boardId = board.boardId,
                name = board.name,
                description = board.description
            )
        }
    }
}