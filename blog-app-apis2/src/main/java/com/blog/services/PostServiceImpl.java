package com.blog.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));

		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post newPost = this.postRepo.save(post);

		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "PostId", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post updatedPost = this.postRepo.save(post);

		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "PostId", postId));
		this.postRepo.delete(post);

	}

//	@Override
//	public List<PostDto> getAllPost() {
//		List<Post> posts = this.postRepo.findAll();
//		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//		return postDtos;
//	}

//	//Applying pagination
//	@Override
//	public List<PostDto> getAllPost(Integer pageNumber,Integer pageSize ) {
//		Pageable pageable=PageRequest.of(pageNumber, pageSize);
//		Page<Post> pagePost = this.postRepo.findAll(pageable);
//		List<Post> posts =	pagePost.getContent();
//		
//		//List<Post> posts = this.postRepo.findAll();
//		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//		return postDtos;
//	}

//	// Applying postResponse ,pagination
//	@Override
//	public PostResponse getAllPost(Integer pageNumber, Integer pageSize) {
//		Pageable pageable = PageRequest.of(pageNumber, pageSize);
//		Page<Post> pagePost = this.postRepo.findAll(pageable);
//		List<Post> posts = pagePost.getContent();
//
//		// List<Post> posts = this.postRepo.findAll();
//		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//
//		PostResponse postResponse = new PostResponse();
//		postResponse.setContent(postDtos);
//		postResponse.setPageNumber(pageNumber);
//		postResponse.setPageSize(pageSize);
//		postResponse.setTotalElements(pagePost.getNumberOfElements());
//		postResponse.setTotalPages(pagePost.getTotalPages());
//		postResponse.setLastPage(pagePost.isLast());
//
//		return postResponse;
//	}

//	// Applying postResponse ,pagination ,sorting
//	@Override
//	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy) {
//		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
//		Page<Post> pagePost = this.postRepo.findAll(pageable);
//		List<Post> posts = pagePost.getContent();
//
//		// List<Post> posts = this.postRepo.findAll();
//		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//
//		PostResponse postResponse = new PostResponse();
//		postResponse.setContent(postDtos);
//		postResponse.setPageNumber(pageNumber);
//		postResponse.setPageSize(pageSize);
//		postResponse.setTotalElements(pagePost.getNumberOfElements());
//		postResponse.setTotalPages(pagePost.getTotalPages());
//		postResponse.setLastPage(pagePost.isLast());
//
//		return postResponse;
//	}

	// Applying postResponse ,pagination ,sorting, sortin direction
	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? sort = Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		// Sort sort = null;
//		if (sortDir.equalsIgnoreCase("asc")) {
//			sort = Sort.by(sortBy).ascending();
//		} else {
//			Sort.by(sortBy).descending();
//		}

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pagePost = this.postRepo.findAll(pageable);
		List<Post> posts = pagePost.getContent();

		// List<Post> posts = this.postRepo.findAll();
		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pageNumber);
		postResponse.setPageSize(pageSize);
		postResponse.setTotalElements(pagePost.getNumberOfElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "PostId", postId));

		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostByCategory(Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		List<Post> posts = this.postRepo.findByCategory(category);
		List<PostDto> postDtos = posts.stream().map(post -> (this.modelMapper.map(post, PostDto.class)))
				.collect(Collectors.toList());

		return postDtos;
	}

	@Override
	public List<PostDto> getPostByUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postDtos = posts.stream().map(post -> (this.modelMapper.map(post, PostDto.class)))
				.collect(Collectors.toList());

		return postDtos;
	}

	@Override
	public List<PostDto> searchPost(String keyword) {
		//List<Post> posts = this.postRepo.findByTitleContaining("%" + keyword + "%");
		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postDtos;
	}

}
