package com.rushucloud.eip.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rushucloud.eip.consts.FileUploadConstants;
import com.rushucloud.eip.dto.JsonString;
import com.rushucloud.eip.models.Company;
import com.rushucloud.eip.models.CompanyDao;
import com.rushucloud.eip.models.Invoice;
import com.rushucloud.eip.models.InvoiceDao;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
	//Enum for image size.
	enum ImageSize{
		ori,sml,ico
	}
	//
	private static Logger LOG = LogManager
			.getLogger(FileUploadController.class);
	// Autowire an object of type InvoiceDao
	@Autowired
	private InvoiceDao _invoiceDao;

	//
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a string describing invoices pictures is successfully get or not.")
	public Invoice provideUploadInfo(@PathVariable("id") long id) {
		return _invoiceDao.findById(id);
	}

	//
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "Response a string describing invoices pictures is successfully uploaded or not.")
	public @ResponseBody Invoice handleFileUpload(
			@RequestParam("name") String name,
			@RequestParam("owner") String owner,
			@RequestParam("file") MultipartFile file) {
		Invoice invoiceResp = null;
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String fileExt = FilenameUtils.getExtension(file
						.getOriginalFilename());
				String fileNameAppendix = new SimpleDateFormat(
						"yyyy-MM-dd-HH-mm-ss-SSS").format(new Date())
						+ "."
						+ fileExt;
				String dbFileName = FileUploadConstants.FILE_PATH_UPLOAD
						+ fileNameAppendix;
				String fullFileName = FileUploadConstants.FILE_PATH_SOURCE
						+ dbFileName;
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(fullFileName)));
				stream.write(bytes);
				stream.close();
				LOG.info("Upload file success."+fullFileName);
				// ImageMagick convert options; @see:
				// http://paxcel.net/blog/java-thumbnail-generator-imagescalar-vs-imagemagic/
				Map<String, String> _imageMagickOutput = new HashMap<String, String>();
				_imageMagickOutput.put(ImageSize.ori.toString(), fileNameAppendix);
				_imageMagickOutput.put(ImageSize.sml.toString(),
						thumbnailImage(150, 150, fullFileName));
				_imageMagickOutput.put(ImageSize.ico.toString(),
						thumbnailImage(32, 32, fullFileName));
				// Save to database.
				try {
					invoiceResp = new Invoice(file.getOriginalFilename(),
							owner, _imageMagickOutput);
					_invoiceDao.save(invoiceResp);
					LOG.info("_invoiceDao save success.");
				} catch (Exception ex) {
					LOG.error(ex.toString());
				}
				return invoiceResp;
			} catch (Exception e) {
				LOG.error("You failed to upload " + name + " => "
						+ e.toString());
			}
		} else {
			LOG.error("You failed to upload " + name
					+ " because the file was empty.");

		}
		return invoiceResp;
	}

	//
	private String thumbnailImage(int width, int height, String source)
			throws IOException, InterruptedException, IM4JavaException {
		//
		String small4dbBase = FilenameUtils.getBaseName(source) + "_"
				+ String.valueOf(width) + "x" + String.valueOf(height) + "."
				+ FilenameUtils.getExtension(source);
		String small4db = FileUploadConstants.FILE_PATH_UPLOAD + small4dbBase;
		String small = FileUploadConstants.FILE_PATH_SOURCE + small4db;
		// @see:
		// http://paxcel.net/blog/java-thumbnail-generator-imagescalar-vs-imagemagic/
		ConvertCmd cmd = new ConvertCmd();
		// cmd.setSearchPath("");
		File thumbnailFile = new File(small);
		if (!thumbnailFile.exists()) {
			IMOperation op = new IMOperation();
			op.addImage(source);
			op.thumbnail(width);
			op.addImage(small);
			cmd.run(op);
			LOG.info("ImageMagick success result:"+small);
		}
		return small4dbBase;
	}
}
