package in.co.application.chutneyApplication.service;
import in.co.application.chutneyApplication.dto.ProductDto;
import in.co.application.chutneyApplication.entity.Product;
import in.co.application.chutneyApplication.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setImage(product.getImage());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto save(ProductDto dto) {

        Product product = new Product();

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setImage(dto.getImage());

        Product saved = productRepository.save(product);

        dto.setId(saved.getId());

        return dto;
    }
}