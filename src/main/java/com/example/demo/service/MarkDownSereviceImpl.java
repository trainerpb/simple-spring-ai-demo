package com.example.demo.service;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkDownSereviceImpl implements MarkDownSerevice {
    @Override
    public String parseMarkdown(String markdown) {
        {
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            return renderer.render(parser.parse(markdown));
        }
    }
}
