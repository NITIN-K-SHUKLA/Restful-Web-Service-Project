package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.entities.Post;
import com.in28minutes.rest.webservices.restfulwebservices.entities.User;
import com.in28minutes.rest.webservices.restfulwebservices.repository.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserResource {

//	private UserDaoService service;

	private UserRepository userRepository;
	private PostRepository postRepository;

	public UserResource(UserRepository userRepository, PostRepository postRepository) {
		super();
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}

	@GetMapping("/jpa/users/{id}")
	public User retrieveUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		return user.get();
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

		//user can be possible without any post 
		User savedUser = userRepository.save(user);

		// ServletUriComponentsBuilder.fromCurrentRequest() gives the current uri of the
		// request i.e /users
		// path() will add the inside uri to main current uri and buildAndExpand will
		// expand the id and toUri will convert
		// all into single uri then this uri will send to user as a location to fetch
		// value in future.
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		// It is used to send response as 201 status not 200
		return ResponseEntity.created(location).build();
	}

	// Posts of users are fetching and creating

	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> fetchPost(@PathVariable int id) {

		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		return user.get().getPost();
//		return postRepository.findByUserId(id);
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public void createPost(@PathVariable int id, @RequestBody Post post)
	{
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		//we are inserting value of user to post class because
		//post cannot be made without the user therefore user is required for every post
		//vice versa is not true that means while inserting value for user we don't need to add the post value in List<post>
		// post has one to many relationship with user
		post.setUser(user.get());
		postRepository.save(post);
	}
	
	@PutMapping("/jpa/users/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User user)
	{
		User user_value = userRepository.findById(id).get();
		if(user.getBirthDate() != null)
		   user_value.setBirthDate(user.getBirthDate());
		if(user.getName() != null)
		   user_value.setName(user.getName());
		userRepository.save(user_value);
		return user_value;
	}
	
	@PutMapping("/jpa/users/{id}/posts/{p_id}")
	public void updatePost(@PathVariable("p_id")int p_id, @RequestBody Post post)
	{
		Post post_value = postRepository.findById(p_id).get();
		if(post.getDescription() != null)
			post_value.setDescription(post.getDescription());
		postRepository.save(post);
	}
	
	@DeleteMapping("/jpa/users/{id}/posts/{p_id}")
	public void deletePost(@PathVariable("p_id") int id)
	{
		postRepository.deleteById(id);
	}
}
