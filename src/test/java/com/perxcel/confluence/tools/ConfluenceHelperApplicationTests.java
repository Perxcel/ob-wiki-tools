package com.perxcel.confluence.tools;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfluenceHelperApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceHelperApplicationTests.class);

    @Autowired
    private ConfluenceContentResource contentResource;

    private Model model = mock(Model.class);

    @Captor
    private ArgumentCaptor<Model> captor;


    @Test
    public void contextLoads() {
        Assert.assertNotNull(contentResource);
        String content = contentResource.getContent("1077805207", null, null, model);
        Assert.assertNotNull(content);

        System.out.println("Still has TABLES? --------- " + content.contains("<table>"));
        System.out.println("Still has form? --------- " + content.contains("<form>"));
        System.out.println("Still has IMG? --------- " + content.contains("<img"));
        System.out.println("Still has span? --------- " + content.contains("<span"));
        System.out.println("Still has h1? --------- " + content.contains("<h1"));
        System.out.println("Still has li? --------- " + content.contains("<li"));
        verify(model).addAttribute(eq("markdown"), captor.capture());
        final String markdown = String.valueOf(captor.getValue());
        LOGGER.info("Markdown----------\n\n\n\n");
        LOGGER.info(markdown);
    }
}
