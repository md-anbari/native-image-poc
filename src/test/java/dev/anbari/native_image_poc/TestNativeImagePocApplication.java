package dev.anbari.native_image_poc;

import org.springframework.boot.SpringApplication;

public class TestNativeImagePocApplication {

	public static void main(String[] args) {
		SpringApplication.from(NativeImagePocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
