jeecg-boot-module-flyway
=========================

This module integrates Flyway migrations into JEECG Boot. It will run SQL migration files located under
`classpath:db/migration` (standard Flyway location) on application startup.

How to use
1. Add this module to your application module dependencies or include it in your parent build (already added under `jeecg-boot-module`).
2. Configure your datasource as usual (Spring Boot properties) so Flyway can connect to the database.
3. Optionally set properties in `application.properties` or `application.yml`:

   jeecg.flyway.enabled=true  # enable/disable automatic migration (default: true)

4. Place migration scripts in `src/main/resources/db/migration` following Flyway naming (e.g., `V1__init.sql`, `V2__add_table.sql`).

Notes
- This module uses Flyway Core and will execute migrations on Spring context refresh.
- If you use multiple datasources or advanced Flyway configuration, you may need to customize the `FlywayAutoConfig` class.

