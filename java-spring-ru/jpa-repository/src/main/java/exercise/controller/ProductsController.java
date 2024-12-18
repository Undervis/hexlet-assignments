package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping
    public ResponseEntity<List<Product>> getAll(@RequestParam(defaultValue = "-1") int min, @RequestParam(defaultValue = "-1") int max) {
        if (min < 0 && max < 0) {
            return ResponseEntity.ok(productRepository.findAll().stream().sorted(Comparator.comparing(Product::getPrice)).toList());
        } else if (min > 0) {
            return ResponseEntity.ok(
                    productRepository.findProductsByPriceGreaterThan(min).stream().sorted(Comparator.comparing(Product::getPrice)).toList()
            );
        } else if (max > 0) {
            return ResponseEntity.ok(
                    productRepository.findProductsByPriceLessThan(max).stream().sorted(Comparator.comparing(Product::getPrice)).toList()
            );
        } else {
            return ResponseEntity.ok(
                    productRepository.findProductsByPriceBetween(min, max).stream().sorted(Comparator.comparing(Product::getPrice)).toList()
            );
        }
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
