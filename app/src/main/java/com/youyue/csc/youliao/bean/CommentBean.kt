package com.youyue.csc.youliao.bean

/**
 * Created by csc on 2018/4/21.
 * explain：
 */

data class CommentBean(
		val comments: List<Comment>
)

data class Comment(
		val name: String,
		val photo: String,
		val comment: String,
		val time: String,
		val commentCount: String
)