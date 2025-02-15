package com.won.myongjiCamp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CompleteDto {
    private Long id; //userId

    @NotEmpty
    @Length(max = 20)
    private String title;

    @NotEmpty
    @Length(max = 500)
    private String content;

    List<MultipartFile> images;
}