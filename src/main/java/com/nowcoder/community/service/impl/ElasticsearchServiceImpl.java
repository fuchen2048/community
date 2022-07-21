package com.nowcoder.community.service.impl;

import com.nowcoder.community.dao.elasticesearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.ElasticsearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 伏辰
 * @date 2022/7/21
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
	@Autowired
	private DiscussPostRepository discussPostRepository;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	private DiscussPostMapper discussPostMapper;
	
	@Override
	public void saveDiscussPost(DiscussPost post) {
		discussPostRepository.save(post);
	}
	
	@Override
	public void deleteDiscussPost(Integer id) {
		discussPostRepository.delete(discussPostMapper.selectDiscussPost(id));
	}
	
	@Override
	public Page<DiscussPost> searchDiscussPost(String keyword, Integer current, Integer limit) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(keyword,"title", "content"))
				.withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
				.withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
				.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
				.withPageable(PageRequest.of(current, limit))
				.withHighlightFields(
						new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
						new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
				).build();
		
		return elasticsearchTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchResultMapper() {
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
	}
}
