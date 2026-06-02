package io.github.ooo1208.test.infrastructure;

import com.alibaba.fastjson.JSON;
import io.github.ooo1208.infrastructure.persistent.dao.IAwardDao;
import io.github.ooo1208.infrastructure.persistent.po.Award;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {

    @Resource
    private IAwardDao awardDao;

    @Test
    public void testQueryAwardList() {
        List<Award> awards = awardDao.queryAwardList();
        log.info("查询奖品列表：{}", JSON.toJSONString( awards));
    }
}
