package com.digimax.geo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.digimax.geo.services.app.GeoDatabaseReader;
import com.digimax.geo.services.app.WebRefresher;
import com.digimax.geo.services.domain.LocationService;
import org.apache.tapestry5.*;
import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateSymbols;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule
{

    private final static Logger LOGGER = LoggerFactory.getLogger(AppModule.class);

    public static void bind(ServiceBinder binder)
    {
        binder.bind(GeoDatabaseReader.class);
        binder.bind(LocationService.class);
        binder.bind(WebRefresher.class);
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, "0.5-SNAPSHOT");
        configuration.override(HibernateSymbols.DEFAULT_CONFIGURATION, false);
    }

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
    }


    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    public RequestFilter buildTimingFilter(final Logger log)
    {
        return new RequestFilter()
        {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException
            {
                long startTime = System.currentTimeMillis();

                try
                {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally
                {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @Local
                                         RequestFilter filter)
    {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Timing", filter);
    }

    @Startup
    public static void scheduleJobs(
            final WebRefresher webRefresher) {
        List<String> webUrls = new ArrayList<>();
        webUrls.add("http://www.wwps.ca");
        webUrls.add("http://www.digimax.com");
        webUrls.add("http://embrace.humanity.cc");
        webUrls.add("http://this.ncapsuld.info");
        webUrls.add("http://portfolio.h2o-elements.com");
        webUrls.add("http://ianrawlinson-portfolio.appspot.com");
        webRefresher.spawnRefresher(webUrls);
        LOGGER.debug("Web Refresher finished spawning");
    }

    public void contributeHibernateSessionSource(
            OrderedConfiguration<HibernateConfigurer> configurer) {

        configurer.add("hibernate-session-source", new HibernateConfigurer() {
            public void configure(org.hibernate.cfg.Configuration configuration) {
                configuration.configure("dev.hibernate.cfg.xml");
            }
        });
    }
}
