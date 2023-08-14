package com.example.board.domain;


import jakarta.persistence.Entity;

// 추후에 @Convert로 리팩토링해보자.
public enum Category {
    QUESTION, // 질문
    FREE
}
