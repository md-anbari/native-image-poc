package dev.anbari.native_image_poc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class NativeImagePocApplicationTests {

	@Test
	void contextLoads() {
	}

}
