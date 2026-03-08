package in.co.application.chutneyApplication.controller;

import in.co.application.chutneyApplication.commons.ApiResponse;
import in.co.application.chutneyApplication.dto.ProductDto;
import in.co.application.chutneyApplication.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<ProductDto>> getProducts() {

        List<ProductDto> products = productService.getProducts();
        return new ApiResponse<>(200, true, "Products fetched successfully", products
        );
    }

    @PostMapping
    public ApiResponse<ProductDto> createProduct(@Valid @RequestBody ProductDto dto
    ) {

        ProductDto product = productService.save(dto);
        return new ApiResponse<>(200, true, "Product created successfully", product
        );
    }
}