package fib.br10.dto.file.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgDbResponse {

    private Integer status;
    private Boolean success;
    private ImgDbResult data;

}
