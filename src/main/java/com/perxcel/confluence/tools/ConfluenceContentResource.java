package com.perxcel.confluence.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Component
@Controller
public class ConfluenceContentResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceContentResource.class);

    @Autowired
    private ConfluenceReadApi confluenceReadApi;

    @Value("${atlassian.email.address}")
    private String email;

    @Value("${atlassian.api.token}")
    private String apiToken;

    @Value("${atlassian.conflience.baseurl}")
    private String basurl;

    private HtmlToMarkDownGenerator htmlToMarkDownGenerator = new HtmlToMarkDownGenerator();

    @PostMapping(value = "/content", produces = "text/markdown")
    public String getContent(@ModelAttribute Data data, BindingResult errors, Model model) {
        System.out.println("Binding result--- " + errors);
        Assert.notNull(data, "Page Id is required");
        Assert.hasText(data.getPageId(), "Page Id is required");
        String accessToken = AuthTokenBuilder.buildAuthHeader(email, apiToken);

        String markdown = getMarkdown(data.getPageId(), accessToken);

        System.out.println("Model? " + model);

        if (model != null) {
            model.addAttribute("markdown", markdown);
            System.out.println("MARKDOWN");
        }
        return "markdown";
    }

    @GetMapping(value = "/content/{pageId}", produces = "text/markdown")
    public String getContent(@PathVariable("pageId") String pageId,
                             @RequestParam(value = "email", required = false) String emailParam,
                             @RequestParam(value = "token", required = false) String apiTokenParam, Model model) {

        Assert.hasText(pageId, "Page Id is required");
        Assert.hasText(basurl, "Properties not loaded");
        if (StringUtils.isEmpty(emailParam)) {
            emailParam = email;
        }
        if (StringUtils.isEmpty(apiTokenParam)) {
            apiTokenParam = apiToken;
        }

        String accessToken = AuthTokenBuilder.buildAuthHeader(emailParam, apiTokenParam);

        String markdown = getMarkdown(pageId, accessToken);
        System.out.println("Model? " + model);

        if (model != null) {
            model.addAttribute("markdown", markdown);
            System.out.println("MARKDOWN");
        }
        return "markdown";
    }

    private String getMarkdown(String pageId, String accessToken) {

        PageResult content = confluenceReadApi.getContent(accessToken, pageId);

        String html = content.getBody().getView().getValue();

        String markdown = htmlToMarkDownGenerator.replaceTableWithMarkdown(html);

        markdown = htmlToMarkDownGenerator.convertImageToMarkdown(markdown);

        markdown = htmlToMarkDownGenerator.convertPreToMDCode(markdown);

        markdown = htmlToMarkDownGenerator.convertStrongToMDBold(markdown);

        markdown = htmlToMarkDownGenerator.removeSpanTag(markdown);
        markdown = htmlToMarkDownGenerator.removeFormTag(markdown);

        markdown = htmlToMarkDownGenerator.convertHtmlListToMarkdown(markdown);

        // required for clearing JSoup text nodes.
        markdown = markdown.replaceAll("\\\\n", "\n");

        markdown = replaceHtmlTags(markdown);
        markdown = markdown.replaceAll("\n\n\n\n\n", "\n");

        return markdown;
    }

    private String replaceHtmlTags(String html) {

        String markdown = replaceHtmlHeaders(html);
        markdown = markdown.replaceAll("<p[^>]*>", "\n");
        markdown = markdown.replaceAll("</p>", "");

        markdown = markdown.replaceAll("<br />", "\n");
        markdown = markdown.replaceAll("<strong>([^<]*)</strong>", "**$1**");
        markdown = markdown.replaceAll("<b>([^<]*)</b>", "**$1**");

        markdown = markdown.replaceAll("<em>([^<]*)</em>\"", "_$1_");

        markdown = markdown.replaceAll("<body[^>]*>", "");
        markdown = markdown.replaceAll("</body>", "\n");

        markdown = markdown.replaceAll("<div[^>]*>", "");
        markdown = markdown.replaceAll("</div>", "\n");

        markdown = markdown.replaceAll("<ul[^>]*>", "");
        markdown = markdown.replaceAll("</ul>", "\n");

        markdown = markdown.replaceAll("<ol[^>]*>", "");
        markdown = markdown.replaceAll("</ol>", "\n");

        markdown = markdown.replaceAll("<time[^>]*>", " ");
        markdown = markdown.replaceAll("</time>", "");

        markdown = markdown.replaceAll("<code[^>]*>", "`");
        markdown = markdown.replaceAll("</code>", "`");

        markdown = markdown.replaceAll("</pre>", "");
        markdown = markdown.replaceAll("</html>", "");

        markdown = markdown.replaceAll("&nbsp;", " ");
        markdown = markdown.replaceAll("<html><head></head>", "");

        return markdown;
    }

    private String replaceHtmlHeaders(String html) {

        String x = html.replaceAll("<h1[^>]*>([^<]*)</h1>", "\n# $1\n");
        x = x.replaceAll("<h2[^>]*>([^<]*)</h2>", "\n## $1\n");
        x = x.replaceAll("<h3[^>]*>([^<]*)</h3>", "\n### $1\n");
        x = x.replaceAll("<h4[^>]*>([^<]*)</h4>", "\n##### $1\n");
        x = x.replaceAll("<h5[^>]*>([^<]*)</h5>", "\n###### $1\n");
        x = x.replaceAll("<h6[^>]*>([^<]*)</h6>", "\n####### $1\n");

        return x;
    }
}
