package com.daryl;

import com.daryl.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class FunkoShopApplication extends Application<FunkoShopConfiguration> {

    public static Jdbi jdbiCon;

    public static void main(final String[] args) throws Exception {
        new FunkoShopApplication().run("server", "config.yml");
    }

    @Override
    public String getName() {
        return "FunkoShop";
    }

    @Override
    public void initialize(final Bootstrap<FunkoShopConfiguration> bootstrap) {

    }

    @Override
    public void run(final FunkoShopConfiguration configuration,
                    final Environment environment) {
        setupJdbiConnection(environment, configuration.getDatabase());
        // REGISTER RESOURCES
        environment.jersey().register(new UserResource());
    }

    private void setupJdbiConnection(final Environment environment,
                                      DataSourceFactory dataSourceFactory){
        final JdbiFactory jdbiFactory = new JdbiFactory();
        jdbiCon = jdbiFactory.build(environment, dataSourceFactory, "postgresql");

    }

}
