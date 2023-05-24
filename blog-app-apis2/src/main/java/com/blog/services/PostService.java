package com.blog.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;

@Service
public interface PostService {

	// create
	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

	// update
	PostDto updatePost(PostDto postDto, Integer postId);

	// delete
	void deletePost(Integer postId);

	//// get All posts
	// List<PostDto> getAllPost();
	//// Pagination
	// List<PostDto> getAllPost(Integer pageNumber, Integer pageSize);
//	//Pagination,postResponse
//	PostResponse getAllPost(Integer pageNumber, Integer pageSize);
////Pagination,postResponse, sorting
//	PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy);

////Pagination,postResponse, sorting, sorting direction
	PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	// get single post
	PostDto getPostById(Integer postId);

	// Get all posts by Category
	List<PostDto> getPostByCategory(Integer categoryId);

	// get all posts by user
	List<PostDto> getPostByUser(Integer userId);

	// Search posts

	List<PostDto> searchPost(String keyword);

}
