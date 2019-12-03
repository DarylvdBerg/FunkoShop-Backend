package com.daryl;

import com.daryl.api.User;
import com.daryl.core.JwtAuthenticator;
import com.daryl.core.JwtHelper;
import com.daryl.resources.ProductResource;
import com.daryl.resources.UserResource;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

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
        registerJwtAuthentication(configuration, environment);
        setupJdbiConnection(environment, configuration.getDatabase());

        //Set jwtSecret
        JwtHelper.jwtSecret = configuration.getJwtSecret();

        // REGISTER RESOURCES
        environment.jersey().register(new UserResource());
        environment.jersey().register(new ProductResource());
    }

    private void setupJdbiConnection(final Environment environment,
                                      DataSourceFactory dataSourceFactory){
        final JdbiFactory jdbiFactory = new JdbiFactory();
        jdbiCon = jdbiFactory.build(environment, dataSourceFactory, "postgresql");
    }

    private void registerJwtAuthentication(final FunkoShopConfiguration configuration, final Environment environment){
        final byte[] jwtSecret = configuration.getJwtSecret();
        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30)
                .setRequireExpirationTime()
                .setVerificationKey(new HmacKey(jwtSecret))
                .build();

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<User>()
                .setJwtConsumer(consumer)
                .setPrefix("Bearer")
                .setAuthenticator(new JwtAuthenticator())
                .buildAuthFilter()
        ));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

}
