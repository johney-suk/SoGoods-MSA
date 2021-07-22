package org.thesix.attach.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UuidRequestDTO {

    private String type;

    private String keyValue;

    private boolean main;

}
