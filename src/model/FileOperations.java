package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileOperations {


    public static ArrayList<InvoiceHeader> readFile() {
        ArrayList<InvoiceHeader> invoices = new ArrayList<>();
        String invoiceHeaderFilePath = "resources\\InvoiceHeader.csv";
        String invoiceLineFilePath = "resources\\InvoiceLine.csv";
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(invoiceHeaderFilePath));
            while ((line = reader.readLine()) != null) {
                InvoiceHeader invoice;
                String[] row = line.split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(row[1]);
                invoice = new InvoiceHeader(Integer.parseInt(row[0]), date, row[2]);
                ArrayList<InvoiceLines> invoiceItems = getItemsForInvoice(invoice, invoiceLineFilePath);
                invoice.setInvoiceLines(invoiceItems);
                invoices.add(invoice);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File is not found with this path");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return invoices;
    }

    public static ArrayList<InvoiceHeader> readFile(String invoicesFilePath, String itemsFilePath) {
        ArrayList<InvoiceHeader> invoices = new ArrayList<>();

        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(invoicesFilePath));
            while ((line = reader.readLine()) != null) {
                InvoiceHeader invoice;
                String[] row = line.split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(row[1]);
                invoice = new InvoiceHeader(Integer.parseInt(row[0]), date, row[2]);
                ArrayList<InvoiceLines> invoiceItems = getItemsForInvoice(invoice, itemsFilePath);
                invoice.setInvoiceLines(invoiceItems);
                invoices.add(invoice);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File is not found with this path");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return invoices;
    }

    public static void writeFile(ArrayList<InvoiceHeader> invoices) {

        String invoiceHeaderFilePath = "resources\\Invoices.csv";
        String invoiceLineFilePath = "resources\\InvoiceItems.csv";
        PrintWriter headerWriter = null;
        PrintWriter lineWriter = null;

        try {

            headerWriter = getFileWriter(invoiceHeaderFilePath);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (InvoiceHeader invoice : invoices) {
                String formattedDate = sdf.format(invoice.getInvoiceDate());
                headerWriter.println(invoice.getInvoiceNum() + "," + formattedDate + "," + invoice.getCustomerName());
            }

            headerWriter.flush();

            lineWriter = getFileWriter(invoiceLineFilePath);
            ArrayList<InvoiceLines> invoiceItems;

            for (InvoiceHeader invoice : invoices) {
                invoiceItems = invoice.getInvoiceLines();
                for (InvoiceLines item : invoiceItems) {
                    lineWriter.println(invoice.getInvoiceNum() + "," + item.getItemName() + ","
                            + String.valueOf(item.getItemPrice()) + "," + String.valueOf(item.getCount()));
                }

            }

            lineWriter.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            headerWriter.close();
            lineWriter.close();
        }

    }

    public static void writeFile(ArrayList<InvoiceHeader> invoices, String invoicesFilePath, String itemsFilePath) {

        PrintWriter headerWriter = null;
        PrintWriter lineWriter = null;

        try {

            headerWriter = getFileWriter(invoicesFilePath);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (InvoiceHeader invoice : invoices) {
                String formattedDate = sdf.format(invoice.getInvoiceDate());
                headerWriter.println(invoice.getInvoiceNum() + "," + formattedDate + "," + invoice.getCustomerName());
            }

            headerWriter.flush();

            lineWriter = getFileWriter(itemsFilePath);
            ArrayList<InvoiceLines> invoiceItems;

            for (InvoiceHeader invoice : invoices) {
                invoiceItems = invoice.getInvoiceLines();
                for (InvoiceLines item : invoiceItems) {
                    lineWriter.println(invoice.getInvoiceNum() + "," + item.getItemName() + ","
                            + String.valueOf(item.getItemPrice()) + "," + String.valueOf(item.getCount()));
                }

            }

            lineWriter.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            headerWriter.close();
            lineWriter.close();
        }

    }

    // Helper Method to organize the code in a good way ( Retrieve items for each invoice)
    private static ArrayList<InvoiceLines> getItemsForInvoice(InvoiceHeader invoice, String filePath) {
        ArrayList<InvoiceLines> invoiceItems = new ArrayList<>();
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                int num = Integer.parseInt(row[0]);
                if (num == invoice.getInvoiceNum()) {
                    invoiceItems.add(new InvoiceLines(invoice, row[1],
                            Double.parseDouble(row[2]), Integer.parseInt(row[3])));
                }

            }
        } catch (FileNotFoundException ex) {
            ex = new FileNotFoundException("File is not found with this path");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return invoiceItems;
    }

    // Helper Method to organize the code in a good way ( Retrieve writer for a specifc file path)
    private static PrintWriter getFileWriter(String filePath) throws IOException {

        return new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
    }
}

