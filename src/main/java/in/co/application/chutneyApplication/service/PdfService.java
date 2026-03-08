package in.co.application.chutneyApplication.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateInvoice(Long orderId, String name, Double total) {

        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("GHAR KI RASOI"));
            document.add(new Paragraph("Order Invoice"));
            document.add(new Paragraph("Order ID: " + orderId));
            document.add(new Paragraph("Customer: " + name));
            document.add(new Paragraph("Total: ₹" + total));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}