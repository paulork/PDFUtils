package br.com.paulork.pdfutils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.openhtmltopdf.DOMBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

public class PDFUtils {

    public static byte[] converter(byte[] entrada) {
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document,baos);

            document.open();

            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(entrada));

            document.close();
            return baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gera um PDF a partir de uma string comum.
     *
     * @param text String representando um arquivo TXT.
     * @return Retorna um Array de Bytes representando o arquivo PDF. Em caso de
     * erro será retornado <b>{@code null }</b>.
     * @see savePdf(pdf, destino)
     */
    private static byte[] textToPdf(String text) {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        PdfWriter writer = null;
        ByteArrayOutputStream bos = null;

        try {
            bos = new ByteArrayOutputStream();
            writer = PdfWriter.getInstance(document, bos);
            writer.open();
            document.open();
            document.add(new Paragraph(text));
            document.close();
            writer.close();
            return bos.toByteArray();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Gera um PDF a partir de um arquivo texto físico.
     *
     * @param text File representando um arquivo físico.
     * @return Retorna um Array de Bytes representando o arquivo PDF. Em caso de
     * erro será retornado <b>{@code null }</b>.
     * @see savePdf(pdf, destino)
     */
    private static byte[] textToPdf(File text) {
        try {
            return textToPdf(FileUtils.readFileToString(text));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Gera um PDF a partir de um arquivo texto na forma de bytes.
     *
     * @param text Arrays de bytes representando o arquivo texto.
     * @return Retorna um Array de Bytes representando o arquivo PDF. Em caso de
     * erro será retornado <b>{@code null }</b>.
     * @see savePdf(pdf, destino)
     */
    private byte[] textToPdf(byte[] text) {
        try {
            return textToPdf(new String(text));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Transforma um texto HTML em um Array de Bytes PDF. O retorno pode ser
     * devolvido ao Browser ou salvo em arquivo, por exemplo.
     *
     * @param html Texto HTML.
     * @return Retorna um Array de Bytes representando o arquivo PDF. Em caso de
     * erro será retornado <b>{@code null }</b>.
     * @see savePdf(pdf, destino)
     */
    public static byte[] htmlToPdf(String html) {
        try {
            return htmlToPdf(html.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Transforma um arquivo HTML fisico em um Array de Bytes PDF. O retorno
     * pode ser devolvido ao Browser ou salvo em arquivo, por exemplo.
     *
     * @param caminhoHtml Arquivo HTML fisico.
     * @return Retorna um Array de Bytes representando o arquivo PDF. Em caso de
     * erro será retornado <b>{@code null }</b>.
     * @see savePdf(pdf, destino)
     */
    private static byte[] htmlToPdf(File caminhoHtml) {
        try {
            return htmlToPdf(FileUtils.readFileToByteArray(caminhoHtml));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Transforma um Array de Bytes HTML para um Array de Bytes PDF. O retorno
     * pode ser devolvido ao Browser ou salvo em arquivo, por exemplo.
     *
     * @param html Array de Bytes representando o arquivo HTML.
     * @return Retorna um Array de Bytes representando o arquivo PDF. Em caso de
     * erro será retornado <b>{@code null }</b>.
     * @see savePdf(pdf, destino)
     */
    private static byte[] htmlToPdf(byte[] html) {
        ByteArrayOutputStream bos = null;
        Document document = null;
        try {
            bos = new ByteArrayOutputStream();
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, bos);
            document.open();
            XMLWorkerHelper xwh = XMLWorkerHelper.getInstance();
            xwh.parseXHtml(writer, document, new ByteArrayInputStream(html));
            document.close();
            return bos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static void html5ToPdf(String html_content, String out_path_file) {
        try {
            OutputStream os = new FileOutputStream(out_path_file);
            try {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withW3cDocument(DOMBuilder.jsoup2DOM(Jsoup.parse(html_content)), null);
                builder.toStream(os);
                builder.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Salva um Streaming de bytes puros para um arquivo pdf de destino.
     *
     * @param pdf Array de bytes representando o arquivo pdf.
     * @param destino Caminho de destino para o arquivo PDF.
     */
    public static void savePdf(byte[] pdf, String destino) {
        try {
            FileOutputStream fos = new FileOutputStream(destino);
            fos.write(pdf);
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Salva um Streaming de bytes codificados em Base64 para um arquivo pdf de
     * destino.
     *
     * @param base64PDF String representando o arquivo pdf, em Base64.
     * @param destino Caminho de destino para o arquivo PDF.
     */
    public static void savePdf(String base64PDF, String destino) {
        savePdf(fromBase64(base64PDF), destino);
    }

    /**
     * Recebe os bytes de um arquivo codificado em base 64 (String) e os
     * converte para byte puro (byte[]).
     *
     * @param bytes Bytes codificados em base 64.
     * @return Retorna um array de bytes referente a string decodificada. Em
     * caso de erro será retornado <b>{@code null }</b>.
     */
    public static byte[] base64ToByte(String bytes) {
        return fromBase64(bytes);
    }

    /**
     * Recebe os bytes de um arquivo (byte[]) e os converte para a representação
     * em base 64 (String) dos mesmos bytes.
     *
     * @param bytes Bytes codificados em base 64.
     * @return Retorna um array de bytes referente a string decodificada. Em
     * caso de erro será retornado <b>{@code null }</b>.
     */
    public static String byteToBase64(byte[] bytes) {
        return toBase64(bytes);
    }

    /**
     * Converte um arquivo físico para um array de bytes.
     *
     * @param pdf File representando o arquivo.
     * @return Retorna um array de bytes correspondente ao arquivo. Em caso de
     * erro será retornado <b>{@code null }</b>.
     */
    public static byte[] fileToByte(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Converte um arquivo físico para um array de bytes.
     *
     * @param pdf Caminho do arquivo.
     * @return Retorna um array de bytes correspondente ao arquivo. Em caso de
     * erro será retornado <b>{@code null }</b>.
     */
    public static byte[] fileToByte(String file) {
        try {
            return fileToByte(new File(file));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private static String toBase64(byte[] fileBytes) {
        return new String(Base64.encodeBase64(fileBytes));
    }

    private static byte[] fromBase64(String base64String) {
        return Base64.decodeBase64(base64String);
    }

}
