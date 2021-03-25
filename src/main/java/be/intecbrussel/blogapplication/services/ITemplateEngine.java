package be.intecbrussel.blogapplication.services;

import org.thymeleaf.context.IContext;

public interface ITemplateEngine {

    String process(String template, IContext context);

}
