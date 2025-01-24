package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> index() {
        return productRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    /*GET /products/{id} — просмотр конкретного товара
PUT /products/{id} — обновление данных товара*/
    // BEGIN
    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                getNotFoundMessage(id)));
    }

    private static String getNotFoundMessage(long id) {
        return String.format("Product with id %d not found", id);
    }

    @PutMapping(path = "/{id}")
    public Product update(@PathVariable long id, @RequestBody Product product) {
        productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(getNotFoundMessage(id)));
        product.setId(id);
        productRepository.save(product);
        return product;
    }

    // END

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
}
