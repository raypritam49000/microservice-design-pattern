package com.user.service.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hotel {
    private String id;
    private String name;
    private String location;
    private String about;
}
