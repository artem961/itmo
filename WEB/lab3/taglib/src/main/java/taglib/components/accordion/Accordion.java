package taglib.components.accordion;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
@FacesComponent("accordionTag")
public class Accordion extends UIComponentBase {
    private String mode;
    private String expanded;

    @Override
    public String getFamily() {
        return "accordion";
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        log.info("starting rendering accordion");
        ResponseWriter writer = context.getResponseWriter();
        initAttributes();

        writer.startElement("div", this);
        writer.writeAttribute("id", getId(), null);
        writer.writeAttribute("class", "accordion", null);
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        for (UIComponent child : getChildren()) {
            if (child instanceof AccordionSection) {
                child.encodeAll(context);
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");


        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        String script = String.format("\tnew Accordion(document.getElementById(\"%s\"), \"%s\", \"%s\");", getId(), mode, expanded);
        writer.writeText(script, null);
        writer.endElement("script");
    }

    private void initAttributes() {
     //   this.id = (String) getAttributes().get("id");
        this.mode = (String) getAttributes().get("mode");
        this.expanded = (String) getAttributes().get("expanded");

       // if (this.id == null) {
       //     this.id = "accordion" + String.valueOf(System.currentTimeMillis());
        //}
    }
}
