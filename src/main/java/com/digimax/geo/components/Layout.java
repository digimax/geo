package com.digimax.geo.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.services.AssetSource;

import java.util.Locale;

/**
 * Layout component for pages of application geo.
 */
@Import(stylesheet = "context:layout/layout.css")
public class Layout
{

    @Inject
    private AssetSource assetSource;

    @Inject
    private Locale locale;
    /**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    private String pageName;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String sidebarTitle;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block sidebar;

    @Inject
    private ComponentResources resources;

    @Property(read = false)
    @Inject
    @Symbol(SymbolConstants.APPLICATION_VERSION) //@TODO figure this out
    private String appVersion;
    public String getAppVersion() {
        return "0.5-SNAPSHOT";
    }


    public String getClassForPageName()
    {
        return resources.getPageName().equalsIgnoreCase(pageName)
                ? "current_page_item"
                : null;
    }

    public String[] getPageNames()
    {
        return new String[]{"Index", "About", "Contact"};
    }
}
