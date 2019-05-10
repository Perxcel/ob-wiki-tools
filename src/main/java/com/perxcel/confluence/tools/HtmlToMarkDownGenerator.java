package com.perxcel.confluence.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlToMarkDownGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlToMarkDownGenerator.class);

    private static final String COL_ENDING = " |";

    private static final String ROW_BEGINNING = "| ";

    private static final String TABLE_HEADER_UNDERLINE = "---";

    private String buildMarkdown(Elements elements) {
        List<String> ignoreTagNames = Arrays.asList("details", "toc");

        StringBuilder markDownBuilder = new StringBuilder();

        boolean skipNextFlag = false;

        for (Element child : elements) {
            String name = child.attributes().get("ac:name");
            String nodeName = child.nodeName();
            System.out
                .println("----Tag-----" + child.tagName() + "----Node" + nodeName + "------" + child.attributes());

            if (skipNextFlag) {
                skipNextFlag = false;
                continue;
            }

            if (nodeName.equalsIgnoreCase("ac:structured-macro") && ignoreTagNames.contains(name)) {
                continue;
            }
            LOGGER.info("Child name: {}", nodeName);
            if (child.childNodeSize() > 0) {

                if (nodeName.equalsIgnoreCase("h1")) {
                    if (child.text().contains("Version Control")) {
                        skipNextFlag = true;
                        continue;
                        // skip processing this and next child/ probably the table

                    }
                }
                for (int i = 0; i < child.childNodeSize(); i++) {
                    markDownBuilder.append(buildMarkdown(child.children()));
                }
            }
        }
        return markDownBuilder.toString();
    }

    public String replaceTableWithMarkdown(String html) {
        Document document = Jsoup.parse(html);
        Elements tables = document.select("table");

        ArrayList<String> tableMarkdowns = new ArrayList<>();

        for (int t = 0; t < tables.size(); t++) {
            Element table = tables.get(t);

            StringBuilder markDownBuilder = new StringBuilder("\n" + ROW_BEGINNING);

            Elements rows = table.select("tr");

            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);

                // Builder Header row and it's underline as per markdown
                Elements columnHeadings = row.select("th");// select header names
                if (columnHeadings.size() > 0) {
                    StringBuilder headerRowUnderlines = new StringBuilder(ROW_BEGINNING);
                    for (Element columnHeading : columnHeadings) {
                        String header = columnHeading.text();
                        markDownBuilder.append(header).append(COL_ENDING);

                        headerRowUnderlines.append(TABLE_HEADER_UNDERLINE);

                        headerRowUnderlines.append(COL_ENDING);
                    }

                    markDownBuilder.append("\\n").append(headerRowUnderlines.toString()).append("\\n");
                }
                // Header row finished

                // Normal rows processing start
                Elements columnValues = row.select("td");// select column values

                if (columnValues.size() > 0) {
                    markDownBuilder.append(ROW_BEGINNING);

                    for (Element columnValue : columnValues) {
                        String columnValueStr = columnValue.text();
                        markDownBuilder.append(columnValueStr).append(COL_ENDING);
                    }
                    markDownBuilder.append("\\n");
                }
            }

            tableMarkdowns.add(markDownBuilder.toString());

            // replace elem
            TextNode textNode = new TextNode(markDownBuilder.toString());
            table.replaceWith(textNode);
        }

        System.out.println("......................complete: ");
        document.outputSettings().prettyPrint(false);
        return document.toString();
    }

    public String generateTables(Document doc) {

        LOGGER.info(doc.title());

        Elements tables = doc.select("table");

        ArrayList<String> tableMarkdowns = new ArrayList<>();

        for (int t = 0; t < tables.size(); t++) {
            Element table = tables.get(t);// Skip the first 2 tables

            StringBuilder markDownBuilder = new StringBuilder(ROW_BEGINNING);

            Elements rows = table.select("tr");

            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);

                // Builder Header row and it's underline as per markdown
                Elements columnHeadings = row.select("th");// select header names
                if (columnHeadings.size() > 0) {
                    StringBuilder headerRowUnderlines = new StringBuilder(ROW_BEGINNING);
                    for (Element columnHeading : columnHeadings) {
                        String header = columnHeading.text();
                        markDownBuilder.append(header).append(COL_ENDING);

                        headerRowUnderlines.append(TABLE_HEADER_UNDERLINE);

                        headerRowUnderlines.append(COL_ENDING);
                    }

                    markDownBuilder.append("\\\\n").append(headerRowUnderlines.toString()).append("\\\\n");
                }
                // Header row finished

                // Normal rows processing start
                Elements columnValues = row.select("td");// select column values

                if (columnValues.size() > 0) {
                    markDownBuilder.append(ROW_BEGINNING);

                    for (Element columnValue : columnValues) {
                        String columnValueStr = columnValue.text();
                        markDownBuilder.append(columnValueStr).append(COL_ENDING);
                    }
                    markDownBuilder.append("<br/>");
                }
            }

            tableMarkdowns.add(markDownBuilder.toString());
        }
        for (String tableMd : tableMarkdowns) {
            LOGGER.info("{} {}", "\\\\n", tableMd);
        }

        LOGGER.info("Total tables parsed: {}", tableMarkdowns.size());
        return tableMarkdowns.toString();
    }


    public String convertImageToMarkdown(String html) {

        Document document = Jsoup.parse(html);
        document.outputSettings().prettyPrint(false);

        Elements images = document.select("img");

        for (int t = 0; t < images.size(); t++) {
            Element image = images.get(t);

            StringBuilder markDownBuilder = new StringBuilder("\n!");

            String imageAttachmentName = image.attributes().get("data-linked-resource-default-alias");

            markDownBuilder.append("[ ").append(imageAttachmentName).append(" ]( ").append("images/")
                           .append(imageAttachmentName).append(" )\n");

            // replace elem
            TextNode textNode = new TextNode(markDownBuilder.toString());
            image.replaceWith(textNode);
        }

        System.out.println("......................complete");
        document.outputSettings().prettyPrint(false);
        return document.toString();
    }

    public String convertPreToMDCode(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().prettyPrint(false);

        Elements codes = document.select("pre");

        for (int t = 0; t < codes.size(); t++) {
            Element code = codes.get(t);

            StringBuilder markDownBuilder = new StringBuilder("\n```json\n").append(code.text()).append("\n```");

            // replace elem
            TextNode textNode = new TextNode(markDownBuilder.toString());
            code.replaceWith(textNode);
        }

        System.out.println("......................complete");
        document.outputSettings().prettyPrint(false);
        return document.toString();
    }

    public String convertStrongToMDBold(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().prettyPrint(false);

        Elements codes = document.select("strong");

        for (int t = 0; t < codes.size(); t++) {
            Element code = codes.get(t);

            StringBuilder markDownBuilder = new StringBuilder(" **").append(code.text()).append("** ");

            // replace elem
            TextNode textNode = new TextNode(markDownBuilder.toString());
            code.replaceWith(textNode);
        }

        System.out.println("......................complete");
        document.outputSettings().prettyPrint(false);
        return document.toString();
    }

    public String removeSpanTag(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().prettyPrint(false);

        Elements spans = document.select("span");

        for (int t = 0; t < spans.size(); t++) {
            Element span = spans.get(t);

            // replace elem with content of span tag
            TextNode textNode = new TextNode(span.text());
            span.replaceWith(textNode);
        }

        System.out.println("......................span nuked");
        document.outputSettings().prettyPrint(false);
        return document.toString();
    }

    public String convertHtmlListToMarkdown(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().prettyPrint(false);

        Elements lists = document.select("ol li");

        final String listStarter = "\n* ";
        for (int t = 0; t < lists.size(); t++) {
            Element listItem = lists.get(t);

            // replace elem
            String text = listStarter + listItem.text();
            TextNode textNode = new TextNode(text);
            listItem.replaceWith(textNode);
        }

        Elements lists1 = document.select("ul li");
        for (Element list : lists1) {
            // replace elem
            String text = listStarter + list.text();
            TextNode textNode = new TextNode(text);
            list.replaceWith(textNode);
        }

        System.out.println("......................list items replaced");
        document.outputSettings().prettyPrint(false);
        return document.toString();
    }

    public String removeFormTag(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().prettyPrint(false);

        Elements forms = document.select("form");

        forms.remove();

        System.out.println("......................span nuked");
        document.outputSettings().prettyPrint(false);
        return document.toString();
    }
}
