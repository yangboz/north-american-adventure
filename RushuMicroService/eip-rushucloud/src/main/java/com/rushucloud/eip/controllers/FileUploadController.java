package com.rushucloud.eip.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rushucloud.eip.models.Invoice;
import com.rushucloud.eip.models.InvoiceDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class FileUploadController {
	//
	private static Logger LOG = LoggerFactory.getLogger(FileUploadController.class);
	//
	@RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
	
	// Autowire an object of type CompanyDao
	@Autowired
	private InvoiceDao _invoiceDao;
		
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileExt  = FilenameUtils.getExtension(file.getOriginalFilename());
                String filePrefix = "src/main/resources/";
                String anewFileName = "uploads/"+new SimpleDateFormat("yyyy-MM-dd-hh-mm").format(new Date())+"."+fileExt;
                String fullFileName = filePrefix.concat(anewFileName);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fullFileName)));
                stream.write(bytes);
                stream.close();
                //Save to database.
                try {
                	Invoice invoice = new Invoice(file.getOriginalFilename(),fullFileName);
        			_invoiceDao.save(invoice);
        			LOG.info("_invoiceDao save success.");
        		} catch (Exception ex) {
        			LOG.error(ex.toString());
        		}
                //TODO:image convert options; @see: http://paxcel.net/blog/java-thumbnail-generator-imagescalar-vs-imagemagic/
                return "You successfully uploaded " + name + " into " + fullFileName;
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
}
