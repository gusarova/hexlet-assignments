package exercise;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import jakarta.websocket.server.PathParam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

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
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }

    //GET /posts/{id}
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id){
        ResponseEntity<Post> result;
        Optional<Post> found = findPostById(id);
        if(!found.isPresent()){
            result=ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else{
            result=ResponseEntity.of(found);
        }
        return result;
    }

    private Optional<Post> findPostById(String id) {
        Optional<Post> found = posts.stream().filter(x -> x.getId().equals(id)).findFirst();
        return found;
    }

    //POST /posts – создание нового поста. Должен возвращаться статус 201
    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post){
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/posts/{id}")
    //PUT /posts/{id} – Обновление поста. Должен возвращаться статус 200. Если пост уже не существует, то должен возвращаться 204
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody Post post){
        ResponseEntity<Post> result;
        Optional<Post> found = findPostById(id);

        if(found.isEmpty()){
            result=ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else{
            Post p = found.get();
            p.setBody(post.getBody());
            p.setTitle(post.getTitle());
            result = ResponseEntity.ok(p);
        }
        return result;
    }

    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
