package in.co.application.chutneyApplication.controller;

import in.co.application.chutneyApplication.commons.ApiResponse;
import in.co.application.chutneyApplication.dto.CartDto;
import in.co.application.chutneyApplication.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ApiResponse<CartDto> addToCart(@Valid @RequestBody CartDto dto) {

        CartDto cart = cartService.addToCart(dto);
        return new ApiResponse<>(200, true, "Item added to cart", cart
        );
    }

    @GetMapping
    public ApiResponse<List<CartDto>> getCart() {
        List<CartDto> cartItems = cartService.getCartItems();
        return new ApiResponse<>(200, true, "Cart fetched successfully", cartItems
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
        return new ApiResponse<>(200, true, "Item removed from cart", null
        );
    }
    @PutMapping("/{id}")
    public ApiResponse<CartDto> updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {

        CartDto updated = cartService.updateQuantity(id, quantity);

        return new ApiResponse<>(200, true, "Cart updated", updated);
    }
}