package com.datadoghq.workshops.samplevulnerablejavaapp.http;

import lombok.Data;

@Data
public class WebsiteTestRequest {
    public String url;
    public String customHeaderKey;
    public String customHeaderValue;
}
