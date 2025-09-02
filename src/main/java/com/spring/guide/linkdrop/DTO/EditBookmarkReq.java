package com.spring.guide.linkdrop.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class EditBookmarkReq {
    @URL
    @NotBlank
    private String url;

    @NotBlank
    private String title;

    private String description;
}
