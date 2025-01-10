package exercise;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    //GET /posts
    // /posts?page=2&limit=10.
    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer limit) {
        return posts.stream().skip((page-1)*limit).limit(limit).collect(Collectors.toList());
    }

    //GET /posts/{id}
    @GetMapping("/posts/{id}")
    public Optional<Post> getPost(@PathVariable String id) {
        return getPostById(id);
    }

    private Optional<Post> getPostById(String id) {
        return posts.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    //POST /posts
    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post) {
        posts.add(post);
        return post;
    }

    //PUT /posts/{id}
    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable String id, @RequestBody Post post) {
        Optional<Post> foundPost = getPostById(id);
        if (foundPost.isPresent()) {
            Post p = foundPost.get();
            p.setBody(post.getBody());
            p.setTitle(post.getTitle());
        }
        return post;
    }

    //DELETE /posts/{id}
    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
    // END
}
