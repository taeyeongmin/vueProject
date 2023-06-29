package com.example.vueback.controller;

import com.example.vueback.entity.Item;
import com.example.vueback.repository.ItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/api/items")
    public List<Item> getItems(){

        List<Item> items = itemRepository.findAll();

        return items;
    }

}
