package com.blog.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

	private Integer categoryId;
	@NotEmpty
	@Size(min = 4)
	private String categoryTitle;
	@Size(min = 10)
	@NotEmpty
	private String categoryDescription;
}
