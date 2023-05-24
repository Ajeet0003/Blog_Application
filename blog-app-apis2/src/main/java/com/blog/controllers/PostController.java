package com.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.config.AppConstants;
import com.blog.payloads.ApiResponse;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.services.FileService;
import com.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);

	}

	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostByUser(@Valid @PathVariable Integer userId) {
		List<PostDto> postDtos = this.postService.getPostByUser(userId);
		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
	}

	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostByCategory(@Valid @PathVariable Integer categoryId) {
		List<PostDto> postDtos = this.postService.getPostByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
	}

//	@GetMapping("/posts")
//	public ResponseEntity<List<PostDto>> getAllPosts() {
//		List<PostDto> postDtos = this.postService.getAllPost();
//		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
//	}

//	// Applying pagination
//
//	@GetMapping("/posts")
//	public ResponseEntity<List<PostDto>> getAllPosts(
//			@Valid @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
//			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
//		List<PostDto> postDtos = this.postService.getAllPost(pageNumber, pageSize);
//		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
//	}

//	// Applying pagination, PostResponse
//
//	@GetMapping("/posts")
//	public ResponseEntity<PostResponse> getAllPosts(
//			@Valid @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
//			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
//		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize);
//		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
//	}

//	// Applying pagination, PostResponse, sorting
//
//	@GetMapping("/posts")
//	public ResponseEntity<PostResponse> getAllPosts(
//			@Valid @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
//			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
//			@RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy) {
//		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize,sortBy);
//		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
//	}

	// Applying pagination, PostResponse, sorting, sorting direction

	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@Valid @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_Size, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	@GetMapping("/post/{postId}")
	public ResponseEntity<PostDto> getPostById(@Valid @PathVariable Integer postId) {
		PostDto postDto = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
	}

	@PutMapping("/post/{postId}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable Integer postId) {
		PostDto updatedPostDto = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPostDto, HttpStatus.OK);
	}

	@DeleteMapping("/post/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@Valid @PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Deleted successfully", true), HttpStatus.OK);
	}

	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchKeyword(@Valid @PathVariable String keyword) {
		List<PostDto> postDtos = this.postService.searchPost(keyword);
		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
	}

	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadImage(@RequestParam("image") MultipartFile image, @PathVariable Integer postId)
			throws IOException {
		PostDto postDto = this.postService.getPostById(postId);

		String fileName = this.fileService.uploadImage(path, image);
		postDto.setImageName(fileName);
		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);

	}

	// method to serve files

	@GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
