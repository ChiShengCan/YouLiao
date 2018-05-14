package com.youyue.csc.youliao.base

/**
 * Created by csc on 2018/5/12.
 * explain：漫画列表实体
 */

 class CartoonListBean{

	public val comic_arr: List<ComicArr>?=null
	val page_num: String?=null

	data class ComicArr(
			val id: String,
			val comic_name: String,
			val comic_pic_240_320: String,
			val comic_desc: String,
			val comic_tag_name: String,
			val painter_user_nickname: String,
			val script_user_nickname: String,
			val click_score: String,
			val comic_last_orderidx: String,
			val comic_theme_id_1: String,
			val comic_theme_id_2: String,
			val comic_theme_id_3: String
	)
}