package in.co.application.chutneyApplication.service;

import in.co.application.chutneyApplication.dto.CartDto;
import in.co.application.chutneyApplication.entity.Cart;
import in.co.application.chutneyApplication.entity.Product;
import in.co.application.chutneyApplication.repository.CartRepository;
import in.co.application.chutneyApplication.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartDto addToCart(CartDto dto) {

        Optional<Cart> existing = cartRepository.findByProductId(dto.getProductId());

        if (existing.isPresent()) {

            Cart cart = existing.get();
            cart.setQuantity(cart.getQuantity() + dto.getQuantity());

            Cart updated = cartRepository.save(cart);

            dto.setId(updated.getId());
            dto.setQuantity(updated.getQuantity());

            return dto;

        } else {

            Cart cart = new Cart();
            cart.setProductId(dto.getProductId());
            cart.setQuantity(dto.getQuantity());

            Cart saved = cartRepository.save(cart);

            dto.setId(saved.getId());
            return dto;
        }
    }

    public List<CartDto> getCartItems() {

        List<Cart> cartItems = cartRepository.findAll();

        return cartItems.stream().map(cart -> {

            Product product = productRepository
                    .findById(cart.getProductId())
                    .orElseThrow();

            CartDto dto = new CartDto();

            dto.setId(cart.getId());
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setImage(product.getImage());
            dto.setQuantity(cart.getQuantity());

            dto.setSubtotal(product.getPrice() * cart.getQuantity());

            return dto;

        }).toList();
    }

    public void removeItem(Long id) {
        cartRepository.deleteById(id);
    }

    public CartDto updateQuantity(Long id, int quantity) {
        Cart cart = cartRepository.findById(id).orElseThrow();
        cart.setQuantity(quantity);
        cartRepository.save(cart);

        Product product = productRepository.findById(cart.getProductId()).orElseThrow();
        CartDto dto = new CartDto();

        dto.setId(cart.getId());
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImage());
        dto.setQuantity(cart.getQuantity());
        dto.setSubtotal(product.getPrice() * cart.getQuantity());

        return dto;
    }
}