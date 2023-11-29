package com.farmted.commentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //수정일자,생성일자,수정자,생성자 와 같은 필드들을 자동으로 관리하고 업데이트 한다.CreatedDate,LastModifiedDate,CreatedBy,LastModifiedBy
@SpringBootTest
class CommentServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
