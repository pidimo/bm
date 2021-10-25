package com.piramide.elwis.cmd.common;

import com.piramide.elwis.cmd.utils.CustomEncrypt;
import com.piramide.elwis.domain.common.FileFreeText;
import com.piramide.elwis.domain.common.FileFreeTextHome;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hpsf.*;
import org.apache.poi.hpsf.wellknown.PropertyIDMap;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSDocumentPath;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*
 *
 * @author Tayes
 * @version $Id: DownloadFreeTextCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class DownloadFreeTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    private OutputStream outputStream;
    private static final String TYPE_TEMPLATE = "temp";
    private static final String TYPE_CAMPAIGNTEMPLATE = "camp";
    private static final String TYPE_COMMUNICATION = "comm";

    public OutputStream getOutputStream() {
        return outputStream;
    }


    public void executeInStateless(SessionContext ctx) {
        Exception exception = null;
        try {
            log.debug("****************************BEGIN DOWNLOAD CMD*******************************");
            Integer version;
            InputStream stream;
            String type = paramDTO.getAsString("type");
            Integer id = new Integer(paramDTO.getAsInt("fid"));
            //Integer company = new Integer(paramDTO.getAsInt("company"));
            FileFreeTextHome fileHome = (FileFreeTextHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_FILEFREETEXT);
            FileFreeText file = fileHome.findByPrimaryKey(id);
            version = file.getVersion();
            stream = new ByteArrayInputStream(file.getValue());
            StringBuffer propertie = new StringBuffer();
            if ("temp".equals(type)) {
                propertie.append(FreeTextTypes.FREETEXT_TEMPLATE_TEXT);
            } else if ("camp".equals(type)) {
                propertie.append(FreeTextTypes.FREETEXT_CAMPAIGN_TEXT);
            } else if ("comm".equals(type)) {
                propertie.append(FreeTextTypes.FREETEXT_CONTACT);
            }
            propertie.append(':')
                    .append(id)
                    .append('-')
                    .append(version);
            /*.append('-')
            .append(company);*/
            String code = CustomEncrypt.i.encode(propertie.toString());
            log.debug("CODE:" + code);
            POIFSReader r = new POIFSReader();
            //ModifySICopyTheRest msrl = new ModifySICopyTheRest(code, paramDTO.getAsString("update"));
            ModifySICopyTheRest msrl = new ModifySICopyTheRest(code, "");
            r.registerListener(msrl);
            r.read(stream);
            msrl.close();
            outputStream = msrl.getOut();
            /*FileOutputStream fileOutputStream = new FileOutputStream("/home/alejandro/trash/file"+System.currentTimeMillis()+".doc");
            fileOutputStream.write(((ByteArrayOutputStream)outputStream).toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();*/
            log.debug("**************************** END DOWNLOAD CMD *******************************");
            //fileHome = null;
            //file = null;
            log.debug("Success Set properties in document");
        } catch (NumberFormatException e) {
            resultDTO.addResultMessage("Common.error.parameters");
            log.debug("Number Format Exception on DownloadFreeTextAction", e);
            exception = e;
        } catch (FinderException e) {
            resultDTO.addResultMessage("Document.error.notfound");
            log.debug("Finder Exception on DownloadFreeTextAction");
            resultDTO.setResultAsFailure();
            exception = e;
            return;
        } catch (FileNotFoundException e) {
            log.debug("File Not Found on DownloadFreeTextAction", e);

            exception = e;
        } catch (IOException e) {
            resultDTO.addResultMessage("Template.error.file.invalid", "template");
            log.debug("IO Exception on DownloadFreeTextAction", e);
            exception = e;
        }
        if (exception != null) {
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }


    static class ModifySICopyTheRest implements POIFSReaderListener {
        String id;
        String updateValues;
        OutputStream out;
        POIFSFileSystem poiFs;

        public OutputStream getOut() {
            return out;
        }

        public ModifySICopyTheRest(String id, String updateValues) {
            this.id = id;
            this.updateValues = updateValues;
            poiFs = new POIFSFileSystem();
        }

        /**
         * <p>The method is called by POI's eventing API for each file in the
         * origin POIFS.</p>
         */
        public void processPOIFSReaderEvent(final POIFSReaderEvent event) {
            /* The following declarations are shortcuts for accessing the
             * "event" object. */
            final POIFSDocumentPath path = event.getPath();
            final String name = event.getName();
            final DocumentInputStream stream = event.getStream();

            Throwable t = null;

            try {
                /* Find out whether the current document is a property set
                 * stream or not. */
                if (PropertySet.isPropertySetStream(stream)) {
                    /* Yes, the current document is a property set stream.
                     * Let's create a PropertySet instance from it. */
                    PropertySet ps = null;
                    try {
                        ps = PropertySetFactory.create(stream);
                    } catch (NoPropertySetStreamException ex) {
                        /* This exception will not be thrown because we already
                         * checked above. */
                    }

                    /* Now we know that we really have a property set. The next
                     * step is to find out whether it is a summary information
                     * or not. */
                    if (ps.isSummaryInformation())
                        /* Yes, it is a summary information. We will modify it
               * and write the result to the destination POIFS. */ {
                        editSI(poiFs, path, name, ps);
                    } else
                        /* No, it is not a summary information. We don't care
                     * about its internals and copy it unmodified to the
                     * destination POIFS. */ {
                        copy(poiFs, path, name, ps);
                    }
                } else
                    /* No, the current document is not a property set stream. We
            * copy it unmodified to the destination POIFS. */ {
                    copy(poiFs, event.getPath(), event.getName(), stream);
                }
            } catch (MarkUnsupportedException ex) {
                t = ex;
            } catch (IOException ex) {
                t = ex;
            } catch (WritingNotSupportedException ex) {
                t = ex;
            }

            /* According to the definition of the processPOIFSReaderEvent method
             * we cannot pass checked exceptions to the caller. The following
             * lines check whether a checked exception occured and throws an
             * unchecked exception. The message of that exception is that of
             * the underlying checked exception. */
            if (t != null) {
                throw new HPSFRuntimeException
                        ("Could not read file \"" + path + "/" + name +
                                //"\". Reason: " + Util.toString(t));
                                "\". Reason: " + t);
            }
        }


        /**
         * <p>Receives a summary information property set modifies (or creates)
         * its "author" and "title" properties and writes the result under the
         * same path and name as the origin to a destination POI filesystem.</p>
         *
         * @param poiFs The POI filesystem to write to.
         * @param path  The original (and destination) stream's path.
         * @param name  The original (and destination) stream's name.
         * @param si    The property set. It should be a summary information
         *              property set.
         */
        public void editSI(final POIFSFileSystem poiFs,
                           final POIFSDocumentPath path,
                           final String name,
                           final PropertySet si)
                throws WritingNotSupportedException, IOException {
            /* Get the directory entry for the target stream. */
            final DirectoryEntry de = getPath(poiFs, path);

            /* Create a mutable property set as a copy of the original read-only
             * property set. */
            final MutablePropertySet mps = new MutablePropertySet(si);

            /* Retrieve the section containing the properties to modify. A
             * summary information property set contains exactly one section. */
            final MutableSection s = (MutableSection) mps.getSections().get(0);

            /* Set the properties. */
            s.setProperty(PropertyIDMap.PID_AUTHOR, Variant.VT_LPSTR,
                    id);
            if (updateValues != null) {
                s.setProperty(PropertyIDMap.PID_COMMENTS, Variant.VT_LPWSTR,
                        updateValues);
            }
            /* Create an input stream containing the bytes the property set
             * stream consists of. */
            final InputStream pss = mps.toInputStream();

            /* Write the property set stream to the POIFS. */
            de.createDocument(name, pss);
        }


        /**
         * <p>Writes a {@link PropertySet} to a POI filesystem. This method is
         * simpler than {@link #editSI} because the origin property set has just
         * to be copied.</p>
         *
         * @param poiFs The POI filesystem to write to.
         * @param path  The file's path in the POI filesystem.
         * @param name  The file's name in the POI filesystem.
         * @param ps    The property set to write.
         */
        public void copy(final POIFSFileSystem poiFs,
                         final POIFSDocumentPath path,
                         final String name,
                         final PropertySet ps)
                throws WritingNotSupportedException, IOException {
            final DirectoryEntry de = getPath(poiFs, path);
            final MutablePropertySet mps = new MutablePropertySet(ps);
            de.createDocument(name, mps.toInputStream());
        }


        /**
         * <p>Copies the bytes from a {@link DocumentInputStream} to a new
         * stream in a POI filesystem.</p>
         *
         * @param poiFs  The POI filesystem to write to.
         * @param path   The source document's path.
         * @param stream The stream containing the source document.
         */
        public void copy(final POIFSFileSystem poiFs,
                         final POIFSDocumentPath path,
                         final String name,
                         final DocumentInputStream stream) throws IOException {
            final DirectoryEntry de = getPath(poiFs, path);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            int c;
            while ((c = stream.read()) != -1) {
                out.write(c);
            }
            stream.close();
            out.close();
            final InputStream in =
                    new ByteArrayInputStream(out.toByteArray());
            de.createDocument(name, in);
        }


        /**
         * <p>Writes the POI file system to a disk file.</p>
         *
         * @throws FileNotFoundException
         * @throws IOException
         */
        public void close() throws FileNotFoundException, IOException {
            //out = new FileOutputStream(dstName);
            out = new ByteArrayOutputStream();

            poiFs.writeFilesystem(out);
            //out.close();
        }


        /**
         * Contains the directory paths that have already been created in the
         * output POI filesystem and maps them to their corresponding
         * {@link org.apache.poi.poifs.filesystem.DirectoryNode}s.
         */
        private final Map paths = new HashMap();


        /**
         * <p>Ensures that the directory hierarchy for a document in a POI
         * fileystem is in place. When a document is to be created somewhere in
         * a POI filesystem its directory must be created first. This method
         * creates all directories between the POI filesystem root and the
         * directory the document should belong to which do not yet exist.</p>
         * <p/>
         * <p>Unfortunately POI does not offer a simple method to interrogate
         * the POIFS whether a certain child node (file or directory) exists in
         * a directory. However, since we always start with an empty POIFS which
         * contains the root directory only and since each directory in the
         * POIFS is created by this method we can maintain the POIFS's directory
         * hierarchy ourselves: The {@link DirectoryEntry} of each directory
         * created is stored in a {@link Map}. The directories' path names map
         * to the corresponding {@link DirectoryEntry} instances.</p>
         *
         * @param poiFs The POI filesystem the directory hierarchy is created
         *              in, if needed.
         * @param path  The document's path. This method creates those directory
         *              components of this hierarchy which do not yet exist.
         * @return The directory entry of the document path's parent. The caller
         *         should use this {@link DirectoryEntry} to create documents in it.
         */
        public DirectoryEntry getPath(final POIFSFileSystem poiFs,
                                      final POIFSDocumentPath path) {
            try {
                /* Check whether this directory has already been created. */
                final String s = path.toString();
                DirectoryEntry de = (DirectoryEntry) paths.get(s);
                if (de != null)
                    /* Yes: return the corresponding DirectoryEntry. */ {
                    return de;
                }

                int l = path.length();
                if (l == 0) {
                    de = poiFs.getRoot();
                } else {
                    de = getPath(poiFs, path.getParent());
                    de = de.createDirectory(path.getComponent
                            (path.length() - 1));
                }
                paths.put(s, de);
                return de;
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                throw new RuntimeException(ex.toString());
            }
        }
    }
}
