package com.nowcoder.community;

import com.fuchen.travel.TravelApplication;
import com.fuchen.travel.entity.DiscussPost;
import com.fuchen.travel.mapper.DiscussPostMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TravelApplication.class)
public class ElasticsearchTest {

	@Autowired
	private DiscussPostMapper discussPostMapper;

	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	
	
	@Test
	public void testInsertList(){
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101, 0, 100));
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102, 0, 100));
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, 0, 100));
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111, 0, 100));
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(131, 0, 100));
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(112, 0, 100));
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(132, 0, 100));
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(133, 0, 100));
		//discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(134, 0, 100));
		
	}
	
	@Test
	public void testSearchByRepository(){
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery("管理员","title", "content"))
				.withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
				.withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
				.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
				.withPageable(PageRequest.of(0, 10))
				.withHighlightFields(
					new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
					new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
				).build();
		//Page<DiscussPost> page = discussPostRepository.search(searchQuery);
		//System.out.println(page.getTotalElements());
		//System.out.println(page.getTotalPages());
		//System.out.println(page.getNumber());
		//System.out.println(page.getSize());
		
		//for (DiscussPost post : page) {
		//	System.out.println(post);
		//}
		
	}
	
	@Test
	public void testSearchByTemplate() {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery("测试","title", "content"))
				.withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
				.withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
				.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
				.withPageable(PageRequest.of(0, 10))
				.withHighlightFields(
						new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
						new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
				).build();
		
		Page page = elasticsearchTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchResultMapper() {
			@Override
			public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
				SearchHits hits = searchResponse.getHits();
				if (hits.getTotalHits() <= 0) {
					return  null;
				}
				List<DiscussPost> list = new ArrayList<>();
				
				for (SearchHit hit : hits) {
					DiscussPost discussPost = new DiscussPost();
					String id = hit.getSourceAsMap().get("id").toString();
					discussPost.setId(Integer.valueOf(id));
					
					String userId = hit.getSourceAsMap().get("userId").toString();
					discussPost.setUserId((Integer.valueOf(userId)));
					String title = hit.getSourceAsMap().get("title").toString();
					discussPost.setTitle(title);
					String content = hit.getSourceAsMap().get("content").toString();
					discussPost.setContent(content);
					String status = hit.getSourceAsMap().get("status").toString();
					discussPost.setStatus(Integer.valueOf(status));
					String createTime = hit.getSourceAsMap().get("createTime").toString();
					discussPost.setCreateTime(new Date(Long.valueOf(createTime)));
					
					String commentCount = hit.getSourceAsMap().get("commentCount").toString();
					discussPost.setCommentCount(Integer.valueOf(commentCount));
					
					//处理高亮显示结果
					HighlightField titleField = hit.getHighlightFields().get("title");
					if (titleField != null) {
					    discussPost.setTitle(titleField.getFragments()[0].toString());
					}
					
					HighlightField contentField = hit.getHighlightFields().get("content");
					if (contentField != null) {
						discussPost.setTitle(contentField.getFragments()[0].toString());
					}
					list.add(discussPost);
				}
				return new AggregatedPageImpl(list, pageable, hits.getTotalHits(), searchResponse.getAggregations(), searchResponse.getScrollId(), hits.getMaxScore());
			}
		});
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		System.out.println(page.getNumber());
		System.out.println(page.getSize());
		
		for (Object post : page) {
			System.out.println(post);
		}
	}
}
