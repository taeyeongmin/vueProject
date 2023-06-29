package com.example.vueback.controller;

import com.example.vueback.entity.Cart;
import com.example.vueback.entity.Item;
import com.example.vueback.repository.CartRepository;
import com.example.vueback.repository.ItemRepository;
import com.example.vueback.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CartController {

    private final ItemRepository itemRepository;
    private final JwtService jwtService;
    private final CartRepository cartRepository;

    @Autowired
    public CartController(ItemRepository itemRepository, JwtService jwtService, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.jwtService = jwtService;
        this.cartRepository = cartRepository;
    }

    /**
     * 장바구니 항목 추가 및 장바구니 생성
     *
     * @param itemId
     * @param token
     * @return
     */
    @PostMapping("/api/cart/items/{itemId}")
    public ResponseEntity pushCartItem(
            @PathVariable("itemId") int itemId,
            @CookieValue(value = "token", required = false) String token
    ){

        // 토큰 유무 검사
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

        if(cart == null){
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            cartRepository.save(newCart);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * 특정 유저가 가지고 있는 장바구니 조회
     *
     * @param token
     * @return
     */
    @GetMapping("/api/cart/items")
    public ResponseEntity getCartItems(@CookieValue(value = "token",required = false)String token){

        // 로그인 관련 검증이 필요한 메서드마다 해당 코드를 사용해야 하지만, security를 사용하여 일괄 처리 가능
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);

        // 1.특정 회원의 장바구니 조회
        List<Cart> carts = cartRepository.findByMemberId(memberId);

        // 2.조회한 장바구니에서 아이템 아이디만 추출
        List<Integer> itemIds = carts.stream().map(Cart::getItemId).collect(Collectors.toList());

        // 3. 아이템 아이디를 통해 아이템 테이블에서 해당 정보 조회
        List<Item> items = itemRepository.findByIdIn(itemIds);


        return new ResponseEntity<>(items,HttpStatus.OK);
    }

    @DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity removeCartItem(
            @PathVariable("itemId") int itemId,
            @CookieValue(value = "token", required = false) String token
    ){
        // 토큰 유무 검사
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

        cartRepository.delete(cart);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
