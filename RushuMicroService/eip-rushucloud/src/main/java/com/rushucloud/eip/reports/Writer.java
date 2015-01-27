package com.rushucloud.eip.reports;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Writes the report to the output stream
 * 
 * @author Krams at {@link http://krams915@blogspot.com}
 */
public class Writer
{

    private static Logger LOG = LogManager.getLogger(Writer.class);

    /**
     * Writes the report to the output stream
     */
    public static void write(HttpServletResponse response, ByteArrayOutputStream baos)
    {

        LOG.debug("Writing report to the stream");
        try {
            // Retrieve the output stream
            ServletOutputStream outputStream = response.getOutputStream();
            // Write to the output stream
            baos.writeTo(outputStream);
            // Flush the stream
            outputStream.flush();

        } catch (Exception e) {
            LOG.error("Unable to write report to the output stream");
        }
    }
}
