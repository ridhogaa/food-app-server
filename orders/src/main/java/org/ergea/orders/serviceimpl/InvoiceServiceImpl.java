package org.ergea.orders.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.ergea.orders.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private DataSource dataSource;

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JasperPrint generateInvoice() {
        try {
            InputStream fileReport = new ClassPathResource("OrderUser.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(fileReport);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, getConnection());
            OutputStream out = new FileOutputStream("./cdn/" + UUID.randomUUID() + ".pdf");
            out.close();
            return jasperPrint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JasperPrint generateReportingMerchant() {
        try {
            InputStream fileReport = new ClassPathResource("ReportingMerchant.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(fileReport);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, getConnection());
            OutputStream out = new FileOutputStream("./cdn/" + UUID.randomUUID() + ".pdf");
            out.close();
            return jasperPrint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}