package exercise.controller.users;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exercise.model.Post;
import exercise.Data;


// BEGIN
@RestController
@RequestMapping("/api/users")
public class PostsController {
    private  List<Post> posts=Data.getPosts();
    //GET /api/users/{id}/posts
    @GetMapping("/{id}/posts")
    public List<Post> getPosts(@PathVariable int id){
        return posts.stream().filter(x->x.getUserId()==id).collect(Collectors.toList());
    }
    //POST /api/users/{id}/posts
    @PostMapping("/{id}/posts")
    public ResponseEntity<Post>  createPost(@PathVariable int id,@RequestBody Post post ){
        post.setUserId(id);
        posts.add(post);
        return new ResponseEntity<>(post,HttpStatus.CREATED);
    }
}
// END
