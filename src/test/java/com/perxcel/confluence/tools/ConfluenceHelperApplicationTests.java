package com.perxcel.confluence.tools;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfluenceHelperApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceHelperApplicationTests.class);

    @Autowired
    private ConfluenceContentResource contentResource;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(contentResource);
        String content = contentResource.getContent("1077806166", null, null, null);
        Assert.assertNotNull(content);

        System.out.println("Still has TABLES? --------- " + content.contains("<table>"));
        System.out.println("Still has IMG? --------- " + content.contains("<img"));
        System.out.println("Still has span? --------- " + content.contains("<span"));
        System.out.println("Still has h1? --------- " + content.contains("<h1"));
        System.out.println("Still has li? --------- " + content.contains("<li"));

        LOGGER.info("Markdown----------\n\n\n\n");
        LOGGER.info(content);
    }
}
