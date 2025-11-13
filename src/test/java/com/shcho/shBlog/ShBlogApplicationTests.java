package com.shcho.shBlog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShBlogApplicationTests {

	@Test
	@Disabled("CI 환경에서 DB가 없어 실패하므로 비활성화")
	void contextLoads() {
	}

}
