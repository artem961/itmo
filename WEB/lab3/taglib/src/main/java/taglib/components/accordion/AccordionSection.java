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
        return "accordion";
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        initAttributes();

        // Accordion header
        writer.startElement("div", this);
        writer.writeAttribute("class", "accordion-header", null);

        writer.startElement("h3", this);
        writer.writeText(title, null);
        writer.endElement("h3");

        writer.endElement("div");

        // Accordion content
        writer.startElement("div", this);
        writer.writeAttribute("class", "accordion-content", null);
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        // Рендерим дочерние компоненты (контент секции)
        for (UIComponent child : getChildren()) {
            child.encodeAll(context);
        }
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div"); // закрываем accordion-content
    }

    private void initAttributes() {
        this.title = (String) getAttributes().get("title");
    }
}
