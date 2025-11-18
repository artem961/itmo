package taglib.components.accordion;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import java.io.IOException;

@FacesComponent("accordionSection")
public class AccordionSection extends UIComponentBase {
    @Override
    public String getFamily() {
        return "accordion";
    }

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("h", this);

        writer.writeText("text", null);

        writer.endElement("h");
    }
}
