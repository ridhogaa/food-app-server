package org.ergea.foodapp.sp;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class UserQuerySP {
    public String readById = "CREATE OR REPLACE FUNCTION get_user_by_id(\n" +
            "    p_id UUID\n" +
            ")\n" +
            "RETURNS TABLE(\n" +
            "    id UUID,\n" +
            "    username VARCHAR,\n" +
            "    email_address VARCHAR,\n" +
            "    password VARCHAR\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT users.id, users.username, users.email_address, users.password\n" +
            "    FROM users\n" +
            "    WHERE users.id = p_id;\n" +
            "END;\n" +
            "$$;\n";

    public String create = "CREATE OR REPLACE PROCEDURE insert_user(\n" +
            "    p_id UUID,\n" +
            "    p_username VARCHAR,\n" +
            "    p_email_address VARCHAR,\n" +
            "    p_password VARCHAR\n" +
            ")\n" +
            "AS $$\n" +
            "BEGIN\n" +
            "    INSERT INTO users (id, username, email_address, password)\n" +
            "    VALUES (p_id, p_username, p_email_address, p_password);\n" +
            "END;\n" +
            "$$ LANGUAGE plpgsql;\n";

    public String update = "CREATE OR REPLACE PROCEDURE update_user(\n" +
            "    p_id UUID,\n" +
            "    p_username VARCHAR,\n" +
            "    p_email_address VARCHAR,\n" +
            "    p_password VARCHAR\n" +
            ")\n" +
            "AS $$\n" +
            "BEGIN\n" +
            "    UPDATE users\n" +
            "    SET username = p_username,\n" +
            "        email_address = p_email_address,\n" +
            "        password = p_password\n" +
            "    WHERE id = p_id;\n" +
            "END;\n" +
            "$$ LANGUAGE plpgsql;\n";

    public String delete = "CREATE OR REPLACE PROCEDURE delete_user(\n" +
            "    p_id UUID\n" +
            ")\n" +
            "AS $$\n" +
            "BEGIN\n" +
            "    DELETE FROM users WHERE users.id = p_id;\n" +
            "END;\n" +
            "$$ LANGUAGE plpgsql;\n";
}
