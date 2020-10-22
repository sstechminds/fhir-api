package com.flowsigma.ewocs.fhir.util;

import java.io.File;
import java.io.IOException;
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
public class TagLoader {
    /**
     * Load tags upto the image data from a file.
     *
     * @param inFile file to load
     * @return DICOM tags
     * @throws IOException if there is an error reading the file
     */
    public static DicomObject loadTags(File inFile) throws IOException {
        DicomInputStream din;
        final DicomObject dataset;

        // Open using DCM4CHE
        din = new DicomInputStream(inFile);
        din.setHandler(new StopTagInputHandler(Tag.PixelData));
        dataset = din.readDicomObject();
        din.close();

        return dataset;
    }
}
