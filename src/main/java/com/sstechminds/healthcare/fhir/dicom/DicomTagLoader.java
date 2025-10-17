package com.sstechminds.healthcare.fhir.dicom;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.StopTagInputHandler;

/**
 * Provides a convience class to load tags from a file.
 *
 * @author Daniel Blezek
 * @see DicomInputStream
 * @see DicomObject
 */
@Slf4j
public class DicomTagLoader {
    /**
     * Load tags, upto the image data, from a file.
     *
     * @param inFile file to load
     * @return DICOM tags
     */
    public static DicomObject loadTags(File inFile) {
        DicomInputStream din = null;
        final DicomObject dataset;

        try {
            din = new DicomInputStream(inFile);
            din.setHandler(new StopTagInputHandler(Tag.PixelData));
            dataset = din.readDicomObject();
            din.close();
        } catch (IOException ioe) {
            log.error("Failed to open dicom file:", ioe);
            throw new RuntimeException(ioe);
        } finally {
            if(din != null) {
                try { din.close(); } catch(Exception e){}
            }
        }
        return dataset;
    }
}
