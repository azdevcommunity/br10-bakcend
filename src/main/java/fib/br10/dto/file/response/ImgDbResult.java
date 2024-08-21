package fib.br10.dto.file.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgDbResult {
    private String id;
    private String title;
    @JsonProperty("url_viewer")
    private String urlViewer;
    private String url;
    @JsonProperty("display_url")
    private String displayUrl;
    private Integer width;
    private Integer height;
    private Integer size;
    private Long time;
    private Integer expiration;
    private ImageDbDetails image;
    private ImageDbDetails thumb;
    private ImageDbDetails medium;
    @JsonProperty("delete_url")
    private String deleteUrl;
}