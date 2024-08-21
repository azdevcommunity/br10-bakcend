package fib.br10.dto.file.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDbDetails {
    private String filename;
    private String name;
    private String mime;
    private String extension;
    private String url;
}