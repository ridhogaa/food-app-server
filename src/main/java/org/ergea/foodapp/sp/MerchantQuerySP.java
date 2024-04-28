package org.ergea.foodapp.sp;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class MerchantQuerySP {
    public String readById = "CREATE OR REPLACE FUNCTION get_merchant_by_id(\n" +
            "    p_id UUID\n" +
            ")\n" +
            "RETURNS TABLE(\n" +
            "    id UUID,\n" +
            "    merchant_name VARCHAR,\n" +
            "    merchant_location VARCHAR,\n" +
            "    open BOOLEAN\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT merchant.id, merchant.merchant_name, merchant.merchant_location, merchant.open\n" +
            "    FROM merchant\n" +
            "    WHERE merchant.id = p_id;\n" +
            "END;\n" +
            "$$;\n";

    public String create = "CREATE OR REPLACE PROCEDURE insert_merchant(\n" +
            "    p_id UUID,\n" +
            "    p_merchant_name VARCHAR,\n" +
            "    p_merchant_location VARCHAR,\n" +
            "    p_open BOOLEAN\n" +
            ")\n" +
            "AS $$\n" +
            "BEGIN\n" +
            "    INSERT INTO merchant (id, merchant_name, merchant_location, open)\n" +
            "    VALUES (p_id, p_merchant_name, p_merchant_location, p_open);\n" +
            "END;\n" +
            "$$ LANGUAGE plpgsql;\n";

    public String update = "CREATE OR REPLACE PROCEDURE update_merchant(\n" +
            "    p_merchant_id UUID,\n" +
            "    p_merchant_name VARCHAR,\n" +
            "    p_merchant_location VARCHAR,\n" +
            "    p_open BOOLEAN\n" +
            ")\n" +
            "AS $$\n" +
            "BEGIN\n" +
            "    UPDATE merchant\n" +
            "    SET merchant_name = p_merchant_name,\n" +
            "        merchant_location = p_merchant_location,\n" +
            "        open = p_open\n" +
            "    WHERE id = p_merchant_id;\n" +
            "END;\n" +
            "$$ LANGUAGE plpgsql;\n";

    public String delete = "CREATE OR REPLACE PROCEDURE delete_merchant(\n" +
            "    p_merchant_id UUID\n" +
            ")\n" +
            "AS $$\n" +
            "BEGIN\n" +
            "    DELETE FROM merchant WHERE id = p_merchant_id;\n" +
            "END;\n" +
            "$$ LANGUAGE plpgsql;\n";
}
