package com.tequila.jwtutil.dto;

public class ImageDto {
    private Long id;
    private String title;
    private byte[] content;

    public ImageDto() {
        super();
    }
    
    public ImageDto(Long id, String title, byte[] content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getContent() {
        return content;
    }
}
