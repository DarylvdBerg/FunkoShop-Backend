package com.daryl;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FunkoShopApplication extends Application<FunkoShopConfiguration> {

    public static void main(final String[] args) throws Exception {
        new FunkoShopApplication().run(args);
    }

    @Override
    public String getName() {
        return "FunkoShop";
    }

    @Override
    public void initialize(final Bootstrap<FunkoShopConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final FunkoShopConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
