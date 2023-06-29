package com.example.vueback.repository;

import com.example.vueback.entity.Item;
import com.example.vueback.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    Member findByEmailAndPassword(String email, String password);

}
