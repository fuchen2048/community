package com.nowcoder.community;

import com.nowcoder.community.entity.Scenic;
import com.nowcoder.community.mapper.FavoriteMapper;
import com.nowcoder.community.mapper.ScenicMapper;
import javafx.beans.binding.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;

/**
 * @Author 伏辰
 * @Date 2022/11/27
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
public class ScenicTest {
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private ScenicMapper scenicMapper;

    @Test
    public void ListScenicAllTest(){
        List<Scenic> scenicList = favoriteMapper.selectCollectionAllByUserId(151, 0, 4);
        Iterator<Scenic> iterator = scenicList.iterator();
        while (iterator.hasNext()) {
            log.info(iterator.next().getScenicName());
        }
    }

    @Test
    public void ListScenicCountTest(){
        List<Scenic> scenicList = favoriteMapper.selectCollectionByCount(0, 0);
        Iterator<Scenic> iterator = scenicList.iterator();
        while (iterator.hasNext()) {
            log.info(iterator.next().getScenicName());
        }
    }

    @Test
    public void ListScenicCountAllTest(){
        List<Scenic> scenicList = favoriteMapper.selectCollectionCountASC(0);
        Iterator<Scenic> iterator = scenicList.iterator();
        while (iterator.hasNext()) {
            log.info(iterator.next().getScenicName());
        }
    }
}
