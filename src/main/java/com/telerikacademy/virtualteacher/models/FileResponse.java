package com.telerikacademy.virtualteacher.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileResponse {
    private String name;
    private String uri;
    private String type;
    private long size;

    @Override
    public String toString() {
        return "FileResponse{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                '}';
    }
}