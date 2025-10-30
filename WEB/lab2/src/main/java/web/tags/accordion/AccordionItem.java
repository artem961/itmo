package web.tags.accordion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccordionItem {
    private String title;
    private String content;

    public String generateHTML() {
        return String.format("""
                <div class="accordion-item">
                    <div class="accordion-header">
                        <span class="accordion-title">%s</span>
                        <span class="accordion-icon">▶</span>
                    </div>
                    <div class="accordion-content">
                        <p>%s</p>
                    </div>
                </div>
                """, title, content);
    }
}
