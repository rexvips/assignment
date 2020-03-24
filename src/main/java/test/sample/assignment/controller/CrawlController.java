package test.sample.assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.sample.assignment.components.ResponseBean;
import test.sample.assignment.service.CrawlService;

@RestController
public class CrawlController {

    @Autowired
    CrawlService crawlService;

    private static Logger LOGGER = LoggerFactory.getLogger(CrawlController.class.getName());

    @GetMapping(value = "/crawl", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean crawl(@RequestParam String url) {
        ResponseBean responseBean = null;
            responseBean =  crawlService.getCrawlData(url);
        return responseBean;
    }


}
