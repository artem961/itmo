package taglib.components.accordion;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import java.io.IOException;

@FacesComponent("accordionSection")
public class AccordionSection extends UIComponentBase {
    private String title;

    @Override
    public String getFamily() {
        return "taglib/components";
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        initAttributes();

        writer.startElement("div", this);
        writer.writeAttribute("class", "accordion-item", null);

        writer.startElement("div", this);
        writer.writeAttribute("class", "accordion-header", null);

        writer.startElement("span", this);
        writer.writeAttribute("class", "accordion-title", null);
        writer.writeText(title, null);
        writer.endElement("span");

        writer.startElement("span", this);
        writer.writeAttribute("class", "accordion-icon", null);
        writer.writeText("▶", null);
        writer.endElement("span");

        writer.endElement("div");

        writer.startElement("div", this);
        writer.writeAttribute("class", "accordion-content", null);
        writer.startElement("p", this);
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        for (UIComponent child : getChildren()) {
            child.encodeAll(context);
        }
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("p");
        writer.endElement("div");
        writer.endElement("div");
    }

    private void initAttributes() {
        this.title = (String) getAttributes().get("title");
    }
}
