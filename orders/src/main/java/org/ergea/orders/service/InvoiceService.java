package org.ergea.orders.service;

import net.sf.jasperreports.engine.JasperPrint;

public interface InvoiceService {
    JasperPrint generateInvoice();

    JasperPrint generateReportingMerchant();
}
