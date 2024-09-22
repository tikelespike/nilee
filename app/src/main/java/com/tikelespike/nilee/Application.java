package com.tikelespike.nilee;

import com.tikelespike.nilee.core.data.service.UserRepository;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "nilee", variant = Lumo.DARK)
@NpmPackage(value = "line-awesome", version = "1.3.0")
@NpmPackage(value = "@vaadin-component-factory/vcf-nav", version = "1.0.6")
@PWA(name = "Nilee Character Sheet", shortName = "Nilee", backgroundColor = "--lumo-base-color",
        themeColor = "--lumo-base-color")
@Meta(name = "viewport",
        content = "user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, "
                + "height=device-height, target-densitydpi=device-dpi")
@Push
public class Application implements AppShellConfigurator {

    /**
     * Entry point for the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Initializes the database with the default data if no users exist.
     *
     * @param dataSource the data source to use
     * @param properties the properties to use
     * @param repository the repository to check for existing data
     *
     * @return the initializer bean
     */
    @Bean
    SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
                                                                               SqlInitializationProperties properties,
                                                                               UserRepository repository) {
        // This bean ensures the database is only initialized when empty
        return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
            @Override
            public boolean initializeDatabase() {
                if (repository.count() == 0L) {
                    return super.initializeDatabase();
                }
                return false;
            }
        };
    }
}
