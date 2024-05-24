package org.ergea.foodapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.ergea.foodapp.dto.BaseResponse;
import org.ergea.foodapp.serviceimpl.InvoiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Invoice")
@RestController
@RequestMapping("v1/generates")
public class InvoiceController {

    @Autowired
    private InvoiceServiceImpl invoiceService;

    @Autowired
    private HttpServletResponse response;

    @PostMapping("invoice")
    public ResponseEntity<?> generateInvoice() throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=\"" + UUID.randomUUID() + ".pdf\"");
        JasperPrint jasperPrint = invoiceService.generateInvoice();
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        return ResponseEntity.ok(BaseResponse.success(invoiceService.generateInvoice(), "Success"));
    }

    @PostMapping("merchants")
    public ResponseEntity<?> generateReportingMerchant() throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=\"" + UUID.randomUUID() + ".pdf\"");
        JasperPrint jasperPrint = invoiceService.generateReportingMerchant();
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        return ResponseEntity.ok(BaseResponse.success(invoiceService.generateReportingMerchant(), "Success"));
    }

}
