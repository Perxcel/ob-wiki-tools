package com.perxcel.confluence.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class ConfluenceCopyApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceCopyApi.class);

    private final String longTaskUrl;

    private String copyHierarchyUrl;

    //private String expandParams = "?expand=space,body.storage,childTypes.attachment,childTypes.page&limit=999";

    private RestTemplate restTemplate;

    private List<String> pagesCopied = new ArrayList<>();

    ConfluenceCopyApi(String baseUrl) {
        this.copyHierarchyUrl = baseUrl + "/content/{sourceId}/pagehierarchy/copy";
        this.longTaskUrl = baseUrl + "/longtask/{longTaskId}";

        this.restTemplate = new RestTemplateBuilder().build();
    }

    /**
     * curl --request POST \ --url
     * 'https://your-domain.atlassian.net/wiki/rest/api/content/{id}/pagehierarchy/copy'
     * \ --user 'email@example.com:<api_token>' \ --header 'Content-Type:
     * application/json' \ --data '{ "copyAttachments": true, "copyPermissions":
     * true, "copyProperties": true, "copyLabels": true, "originalPageId":
     * "<string>", "destinationPageId": "<string>", "titleOptions": { "prefix":
     * "<string>", "replace": "<string>", "search": "<string>" } }'
     */

    public CopyPageResult copyHierarchy(String authToken, String sourceId, CopyHierarchyParams request) {

        ResponseEntity<CopyPageResult> responseEntity;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + authToken);

            responseEntity = restTemplate.postForEntity(this.copyHierarchyUrl, request, CopyPageResult.class, sourceId);

            CopyPageResult pageResult = responseEntity.getBody();
            LOGGER.debug("Page Result Id:  ", pageResult.getId());
            LOGGER.debug("Page Result links:  ", pageResult.getLinks());
            return pageResult;
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error : \n" + e.getResponseBodyAsString());
        }
        return null;
    }

    public String getLongTaskStatus(String authToken, String longTaskId) {
        ResponseEntity<String> responseEntity;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + authToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            responseEntity = restTemplate.exchange(this.longTaskUrl, HttpMethod.GET, entity, String.class, longTaskId);

            String pageResult = responseEntity.getBody();
            LOGGER.debug("Page Result:  ", pageResult);
            return pageResult;
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error : \n" + e.getResponseBodyAsString());
        }
        return null;
    }
}
