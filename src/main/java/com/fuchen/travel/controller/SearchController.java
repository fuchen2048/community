package com.fuchen.travel.controller;

import com.fuchen.travel.entity.DiscussPost;
import com.fuchen.travel.entity.Page;
import com.fuchen.travel.entity.Scenic;
import com.fuchen.travel.service.*;
import com.fuchen.travel.util.TravelConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 伏辰
 * @date 2022/7/21
 * 搜索
 */
@Controller
public class SearchController implements TravelConstant {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LikeService likeService;

	@Autowired
	private SearchService searchService;

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private DiscussPostService discussPostService;
	/**
	 * 搜索
	 * @param keyword 关键字
	 * @param model 模型
	 * @param page 页面
	 * @return
	 */
	@GetMapping("/group/search")
	public String search(String keyword, Model model, Page page) {

		Integer keywordCount = discussPostService.findDiscussPostToKeywordCount(keyword);
		//分页信息
		page.setLimit(10);
		page.setPath("/search?keyword=" + keyword);
		page.setRows(keywordCount);

		//搜索帖子
		List<DiscussPost> searchResult = discussPostService.findToDiscussPost(keyword, page.getOffset(), page.getLimit());

		//聚合数据
		List<Map<String, Object>> discussPosts = new ArrayList<>();
		if (searchResult != null) {
			for (DiscussPost post : searchResult) {
				Map<String, Object> map = new HashMap<>();
				//帖子
				map.put("post", post);
				//作者
				map.put("user", userService.findById(post.getUserId()));
				//点赞数量
				map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
				discussPosts.add(map);
			}
		}
		model.addAttribute("discussPosts", discussPosts);
		model.addAttribute("keyword", keyword);

		return "/site/group-search";
	}

	/**
	 * 搜索
	 * @param keyword 关键字
	 * @param model 模型
	 * @param page 页面
	 * @return
	 */
	@GetMapping("/scenic-list/search")
	public String searchScenic(String keyword, Model model, Page page) {

		Integer resultCount = searchService.findToScenicCount(keyword);

		page.setLimit(5);
		page.setPath("/scenic-list/search?keyword=" + keyword);
		page.setRows(resultCount);

		//搜索景区
		List<Scenic> toScenic = searchService.findToScenic(keyword, page.getOffset(), page.getLimit());

		//聚合数据
		List<Map<String, Object>> toScenicList = new ArrayList<>();
		if (toScenic != null) {
			for (Scenic scenic : toScenic) {
				Map<String, Object> map = new HashMap<>();
				//景区
				map.put("scenic", scenic);
				toScenicList.add(map);
			}
		}
		model.addAttribute("SearchScenicResult", toScenicList);
		model.addAttribute("scenicName", keyword);
		model.addAttribute("SearchScenicCount", toScenic.size());

		//获取热门景点
		List<Scenic> scenicConllectionList = favoriteService.findCollectionByCount(0, 4);
		List<Map<String, Object>> scenicListByCount = new ArrayList<>();

		if (scenicConllectionList != null && scenicConllectionList.size() > 0) {
			for (Scenic scenic : scenicConllectionList) {
				Map<String, Object> map = new HashMap<>();
				map.put("scenic", scenic);
				scenicListByCount.add(map);
			}
		}

		model.addAttribute("scenicsHot", scenicListByCount);
		return "/site/search-scenic-list";
	}
}
