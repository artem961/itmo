package web.tags.accordion;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import lombok.Setter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
public class Accordion extends SimpleTagSupport {
    private String mode;
    private String expanded;
    private String id;


    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        StringWriter writer = new StringWriter();
        getJspBody().invoke(writer);
        String data = writer.toString();

        this.id = this.id == null ? String.valueOf(Math.abs(new Random(System.nanoTime()).nextInt())) : this.id;

        out.print(String.format("<div class=\"accordion\" id=\"%s\">", id));

        List<AccordionItem> items = parseData(data);
        if (items.isEmpty()) {

        } else{
            items.forEach(item -> {
                try {
                    out.print(item.generateHTML());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        out.print("</div>");

        out.print(initJSAccordionObject(mode, expanded));
    }

    private List<AccordionItem> parseData(String data) {
        List<AccordionItem> items = new ArrayList<>();

        String regex = "\\[TITLE\\](.*?)\\[/TITLE\\]\\s*\\[CONTENT\\](.*?)\\[/CONTENT\\]";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            String title = matcher.group(1);
            String content = matcher.group(2);
            items.add(new AccordionItem(title, content));
        }

        return items;
    }

    private String initJSAccordionObject(String mode, String expanded) {
        expanded = expanded == null? "[]" : "[" + expanded + "]";

        StringBuilder js = new StringBuilder();
        js.append("\n<script>");
        js.append(String.format("\tnew Accordion(document.getElementById(\"%s\"), \"%s\", \"%s\");", this.id, mode, expanded));
        js.append("</script>\n");

        return js.toString();
    }
}
