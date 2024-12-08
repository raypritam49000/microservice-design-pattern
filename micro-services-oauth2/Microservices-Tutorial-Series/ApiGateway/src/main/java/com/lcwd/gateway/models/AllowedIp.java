package com.lcwd.gateway.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllowedIp {
    private String id;
    private String name;
    private String ipAddress;
    private String description;
}