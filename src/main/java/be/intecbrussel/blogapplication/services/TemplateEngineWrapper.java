package be.intecbrussel.blogapplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class TemplateEngineWrapper implements ITemplateEngine {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public String process(String templateName, IContext context) {
        return templateEngine.process(templateName, context);
    }

}