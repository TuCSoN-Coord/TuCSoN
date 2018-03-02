package alice.tuplecentre.tucson.persistency;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.respect.core.RespectVMContext.ModType;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Lorenzo Pontellini, Vincenzo Scafuto
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class PersistencyXML {

    private static final String ACTION_ATTRIBUTE = "action";
    private static final String ADD_OPERATION = "addition";
    private static final String CLEAN_OPERATION = "clean";
    private static final String DEL_OPERATION = "deletion";
    private static final String PREDICATE_NODE = "predicate";
    private static final String PREDICATES_NODE = "predicates";
    private static final String ROOT_NODE = "persistency";
    private static final String SNAPSHOT_NODE = "snapshot";
    private static final String SPEC_NODE = "spec";
    private static final String SPEC_TUPLES_NODE = "specTuples";
    private static final String SUBJECT_ATTRIBUTE = "subject";
    private static final String TC_ATTRIBUTE = "tc";
    private static final String TIME_ATTRIBUTE = "time";
    private static final String TUPLE_NODE = "tuple";
    private static final String TUPLES_NODE = "tuples";
    private static final String UPDATE_NODE = "update";
    private static final String UPDATES_NODE = "updates";
    private String pDate;
    private TucsonTupleCentreId pFileName;
    private String pPath;
    private File xmlFile;

    public PersistencyXML(final String fileName) {
        this.xmlFile = new File(fileName);
    }

    public PersistencyXML(final String path, final TucsonTupleCentreId fileName) {
        this.pFileName = fileName;
        this.pPath = path;
    }

    public List<String> getNodeInfo(final Node node, final String childName) {
        final List<String> nodeInfo = new LinkedList<String>();
        final NodeList childs = node.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            final Node n = childs.item(i);
            if (n.getNodeName().equals(childName)) {
                nodeInfo.add(n.getTextContent());
            }
        }
        return nodeInfo;
    }

    public String getUpdateInfo(final Element elem) {
        String nodeInfo = null;
        final String attrAction = elem
                .getAttribute(PersistencyXML.ACTION_ATTRIBUTE);
        final String attrSubject = elem
                .getAttribute(PersistencyXML.SUBJECT_ATTRIBUTE);
        final String body = elem.getTextContent();
        String subject = null;
        if (attrSubject.equals(PersistencyXML.TUPLE_NODE)) {
            subject = "t";
        } else if (attrSubject.equals(PersistencyXML.SPEC_NODE)) {
            subject = "s";
        } else if (attrSubject.equals(PersistencyXML.PREDICATE_NODE)) {
            subject = "p";
        }
        String operation = null;
        if (attrAction.equals(PersistencyXML.ADD_OPERATION)) {
            operation = "+";
        } else if (attrAction.equals(PersistencyXML.DEL_OPERATION)) {
            operation = "-";
        } else if (attrAction.equalsIgnoreCase(PersistencyXML.CLEAN_OPERATION)) {
            operation = "e";
        }
        if (subject != null && operation != null && body != null) {
            nodeInfo = "(" + operation + subject + ")" + " " + body;
        }
        return nodeInfo;
    }

    public ArrayList<String> listByRemoving(final List<String> updates,
            final String value) {
        boolean found = false;
        String search = null;
        if (value.equals(PersistencyXML.TUPLE_NODE)) {
            search = "(et)";
        } else if (value.equals(PersistencyXML.SPEC_NODE)) {
            search = "(es)";
        } else if (value.equals(PersistencyXML.PREDICATE_NODE)) {
            search = "(ep)";
        }
        for (int i = updates.size() - 1; i > 0; i--) {
            final String obj = updates.get(i);
            if (obj.contains(search)) {
                found = true;
            }
            if (found) {
                if ((obj.contains("(+t)") || obj.contains("(-t)") || obj
                        .contains("(et)"))
                        && value.equals(PersistencyXML.TUPLE_NODE)
                        || (obj.contains("(+s)") || obj.contains("(-s)") || obj
                                .contains("(es)"))
                                && value.equals(PersistencyXML.SPEC_NODE)
                                || (obj.contains("(+p)") || obj.contains("(-p)") || obj
                                        .contains("(ep)"))
                                        && value.equals(PersistencyXML.PREDICATE_NODE)) {
                    updates.remove(i);
                }
            }
        }
        return new ArrayList<String>(updates);
    }

    public PersistencyData parse() {
        PersistencyData pData = new PersistencyData();
        List<String> tuples = new ArrayList<String>();
        List<String> specTuple = new ArrayList<String>();
        List<String> predicates = new ArrayList<String>();
        List<String> updates = new ArrayList<String>();
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(this.xmlFile);
            // SNAPSHOT
            final NodeList snapshotList = document
                    .getElementsByTagName(PersistencyXML.SNAPSHOT_NODE);
            final Node snapshotNode = snapshotList.item(0);
            final NodeList snapshotChilds = snapshotNode.getChildNodes();
            for (int i = 0; i < snapshotChilds.getLength(); i++) {
                final Node node = snapshotChilds.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    final Element elem = (Element) node;
                    if (elem.getNodeName().equals(PersistencyXML.TUPLES_NODE)) {
                        tuples = this.getNodeInfo(node,
                                PersistencyXML.TUPLE_NODE);
                    } else if (elem.getNodeName().equals(
                            PersistencyXML.SPEC_TUPLES_NODE)) {
                        specTuple = this.getNodeInfo(node,
                                PersistencyXML.SPEC_NODE);
                    } else if (elem.getNodeName().equals(
                            PersistencyXML.PREDICATES_NODE)) {
                        predicates = this.getNodeInfo(node,
                                PersistencyXML.PREDICATE_NODE);
                    }
                }
            }
            // UPDATES
            final NodeList updatesList = document
                    .getElementsByTagName(PersistencyXML.UPDATES_NODE);
            final Node updatesNode = updatesList.item(0);
            final NodeList updatesChilds = updatesNode.getChildNodes();
            for (int i = 0; i < updatesChilds.getLength(); i++) {
                final Node node = updatesChilds.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    final Element elem = (Element) node;
                    if (elem.getNodeName().equals(PersistencyXML.UPDATE_NODE)) {
                        final String update = this.getUpdateInfo(elem);
                        if (update != null) {
                            updates.add(update);
                        }
                    }
                }
            }
            updates = this.listByRemoving(updates, PersistencyXML.TUPLE_NODE);
            updates = this.listByRemoving(updates, PersistencyXML.SPEC_NODE);
            updates = this.listByRemoving(updates,
                    PersistencyXML.PREDICATE_NODE);
            pData = new PersistencyData(tuples, specTuple, predicates, updates);
        } catch (final ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pData;
    }

    public void write(final PersistencyData pData) {
        try {
            final long now = System.currentTimeMillis();
            final Date d = new Date(now);
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd_HH.mm.ss");
            final String date = sdf.format(d);
            this.pDate = date;
            final String pXMLFileName = this.pFileName.getName() + "_at_"
                    + this.pFileName.getNode() + "_at_"
                    + this.pFileName.getPort();
            // Delete old persistency file
            final File dir = new File(this.pPath);
            final File[] files = dir.listFiles();
            for (final File file : files) {
                if (file.getName().startsWith("tc_" + pXMLFileName)) {
                    if (file.delete()) {
                        System.out.println("....old persistency file '"
                                + file.getName() + "' has been deleted");
                    } else {
                        System.err
                        .println("....error while deleting old persistency file '"
                                + file.getName() + "' :/");
                    }
                }
            }
            this.xmlFile = new File(this.pPath, "tc_" + pXMLFileName + "_"
                    + date + ".xml");
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            final DocumentBuilder documentBuilder = documentBuilderFactory
                    .newDocumentBuilder();
            final Document document = documentBuilder.newDocument();
            final Element rootElement = document
                    .createElement(PersistencyXML.ROOT_NODE);
            document.appendChild(rootElement);
            final Element snapshotElement = document
                    .createElement(PersistencyXML.SNAPSHOT_NODE);
            // Set attribute tc to rootElement
            snapshotElement.setAttribute(PersistencyXML.TC_ATTRIBUTE,
                    this.pFileName.toString());
            // Set attribute time to rootElement
            snapshotElement.setAttribute(PersistencyXML.TIME_ATTRIBUTE,
                    this.pDate);
            rootElement.appendChild(snapshotElement);
            final Element tuples = document
                    .createElement(PersistencyXML.TUPLES_NODE);
            final Iterator<LogicTuple> it = pData.getTupleSet().getIterator();
            while (it.hasNext()) {
                final Element tuple = document
                        .createElement(PersistencyXML.TUPLE_NODE);
                tuple.setTextContent(it.next().toString());
                tuples.appendChild(tuple);
            }
            snapshotElement.appendChild(tuples);
            final Element specTuples = document
                    .createElement(PersistencyXML.SPEC_TUPLES_NODE);
            final Iterator<LogicTuple> itS = pData.getTupleSpecSet()
                    .getIterator();
            while (itS.hasNext()) {
                final Element spec = document
                        .createElement(PersistencyXML.SPEC_NODE);
                spec.setTextContent(itS.next().toString());
                specTuples.appendChild(spec);
            }
            snapshotElement.appendChild(specTuples);
            final Element predicates = document
                    .createElement(PersistencyXML.PREDICATES_NODE);
            final Iterator<LogicTuple> itP = pData.getPrologPredicates()
                    .getIterator();
            while (itP.hasNext()) {
                final Element predicate = document
                        .createElement(PersistencyXML.PREDICATE_NODE);
                predicate.setTextContent(itP.next().toString());
                predicates.appendChild(predicate);
            }
            snapshotElement.appendChild(predicates);
            // write the content into xml file
            final TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            final DOMSource source = new DOMSource(document);
            final StreamResult result = new StreamResult(this.xmlFile);
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
        } catch (final ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeUpdate(final LogicTuple update, final ModType mode) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory
                    .newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document doc = db.parse(this.xmlFile);
            // final Element root = doc.getDocumentElement();
            final Element root = (Element) doc.getElementsByTagName(
                    PersistencyXML.ROOT_NODE).item(0);
            Element updates;
            final NodeList updsElement = doc
                    .getElementsByTagName(PersistencyXML.UPDATES_NODE);
            if (updsElement.getLength() == 0) {
                updates = doc.createElement(PersistencyXML.UPDATES_NODE);
                root.appendChild(updates);
            } else {
                updates = (Element) updsElement.item(0);
            }
            final Element upd = doc.createElement(PersistencyXML.UPDATE_NODE);
            switch (mode) {
                case ADD_TUPLE:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.TUPLE_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.ADD_OPERATION);
                    upd.setTextContent(update.toString());
                    break;
                case ADD_SPEC:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.SPEC_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.ADD_OPERATION);
                    upd.setTextContent(update.toString());
                    break;
                case ADD_PRED:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.PREDICATE_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.ADD_OPERATION);
                    upd.setTextContent(update.toString());
                    break;
                case DEL_TUPLE:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.TUPLE_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.DEL_OPERATION);
                    upd.setTextContent(update.toString());
                    break;
                case DEL_SPEC:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.SPEC_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.DEL_OPERATION);
                    upd.setTextContent(update.toString());
                    break;
                case DEL_PRED:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.PREDICATE_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.DEL_OPERATION);
                    upd.setTextContent(update.toString());
                    break;
                case EMPTY_TUPLES:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.TUPLE_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.CLEAN_OPERATION);
                    break;
                case EMPTY_SPEC:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.SPEC_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.CLEAN_OPERATION);
                    break;
                case EMPTY_PRED:
                    upd.setAttribute(PersistencyXML.SUBJECT_ATTRIBUTE,
                            PersistencyXML.PREDICATE_NODE);
                    upd.setAttribute(PersistencyXML.ACTION_ATTRIBUTE,
                            PersistencyXML.CLEAN_OPERATION);
                    break;
                default:
                    break;
            }
            updates.appendChild(upd);
            // Updates Time attribute
            final long now = System.currentTimeMillis();
            final Date d = new Date(now);
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd_HH.mm.ss");
            final String date = sdf.format(d);
            updates.setAttribute("time", date);
            // write the content into xml file
            final TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            final DOMSource source = new DOMSource(doc);
            final StreamResult result = new StreamResult(this.xmlFile);
            transformer.transform(source, result);
        } catch (final TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
